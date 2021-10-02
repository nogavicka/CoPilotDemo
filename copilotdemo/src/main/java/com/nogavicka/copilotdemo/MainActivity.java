package com.nogavicka.copilotdemo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.health.services.client.ExerciseClient;
import androidx.health.services.client.ExerciseUpdateListener;
import androidx.health.services.client.HealthServices;
import androidx.health.services.client.HealthServicesClient;
import androidx.health.services.client.data.DataPoint;
import androidx.health.services.client.data.DataType;
import androidx.health.services.client.data.ExerciseCapabilities;
import androidx.health.services.client.data.ExerciseConfig;
import androidx.health.services.client.data.ExerciseInfo;
import androidx.health.services.client.data.ExerciseLapSummary;
import androidx.health.services.client.data.ExerciseState;
import androidx.health.services.client.data.ExerciseTrackedStatus;
import androidx.health.services.client.data.ExerciseType;
import androidx.health.services.client.data.ExerciseUpdate;

import com.google.common.collect.ImmutableSet;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import com.nogavicka.copilotdemo.databinding.ActivityMainBinding;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends Activity {
    private ActivityMainBinding binding;
    private TextView calorieBurnTextView;
    private TextView heartRateTextView;

    // This is something that should persist in a view model.
    private double totalCaloriesSum = 0;
    // Subscribe to health data for exercises.
    ExerciseClient exerciseClient;
    // Reports exercise information back to UI.
    ExerciseUpdateListener listener;
    // Used for executing Exercise Client events on the background thread.
    ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        calorieBurnTextView = binding.calorieBurnTitle;
        heartRateTextView = binding.heartRateTitle;

        executorService = Executors.newFixedThreadPool(10);

        checkHealthServices();
        registerExerciseUpdateListener();
        checkIfOtherAppsAreUsingExerciseClient();
    }

    @Override
    protected void onStart() {
        super.onStart();
        exerciseClient.setUpdateListener(listener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopExercise();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startExercise();
    }

    @Override
    protected void onDestroy() {
        exerciseClient.clearUpdateListener(listener);
        super.onDestroy();
    }

    /**
     * For each ExerciseType, certain data types are supported for metrics and for exercise goals.
     * Check these capabilities at startup.
     */
    private void checkHealthServices() {
        HealthServicesClient healthClient = HealthServices.getClient(this /*context*/);
        exerciseClient = healthClient.getExerciseClient();
        ListenableFuture<ExerciseCapabilities> capabilities = exerciseClient.getCapabilities();
        Futures.addCallback(capabilities,
                new FutureCallback<ExerciseCapabilities>() {
                    @Override
                    public void onSuccess(ExerciseCapabilities result) {
                        if (result.getSupportedExerciseTypes().contains(ExerciseType.RUNNING)) {
                            // Proceed.
                            result.getExerciseTypeCapabilities(ExerciseType.RUNNING);
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        // TODO: Terminate if the exercise it not supported and inform user.
                    }
                },
                executorService);
    }

    /**
     * Health Services supports at most one exercise at a time across all apps on the device.
     * If an app starts an exercise, it will cause any current exercise in another app to be
     * terminated.
     */
    private void checkIfOtherAppsAreUsingExerciseClient() {
        ListenableFuture<ExerciseInfo> exerciseInfo = exerciseClient.getCurrentExerciseInfo();
        Futures.addCallback(
                exerciseInfo,
                new FutureCallback<ExerciseInfo>() {
                    @Override
                    public void onSuccess(ExerciseInfo result) {
                        if (result.getExerciseTrackedStatus()
                                == ExerciseTrackedStatus.OTHER_APP_IN_PROGRESS) {
                            // In the future: ask the user for confirmation to terminate the old
                            // exercise. For now, just terminate the other exercise.
                            stopExercise();
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        // Looks like no other app is running the exercies, so we're good!
                    }
                },
                executorService
        );
    }

    private void startExercise() {
        // Types for which we want to receive metrics.
        Set<DataType> dataTypeSet = ImmutableSet.of(
                DataType.HEART_RATE_BPM,
                DataType.TOTAL_CALORIES);

        ExerciseConfig config = ExerciseConfig.builder()
                .setExerciseType(ExerciseType.RUNNING)
                .setDataTypes(dataTypeSet)
                .build();
        // Start the exercise.
        HealthServices.getClient(this /*context*/)
                .getExerciseClient()
                .startExercise(config);
    }

    private void stopExercise() {
        // End the exercise.
        HealthServices.getClient(this /*context*/)
                .getExerciseClient()
                .endExercise();
    }

    /** Listener that updates the UI when exercise updates happen. */
    private void registerExerciseUpdateListener() {
        listener = new ExerciseUpdateListener() {
            @Override
            public void onExerciseUpdate(@NonNull ExerciseUpdate exerciseUpdate) {
                // Process the latest information about the exercise. Should probably do some
                // smart things around the exercise state.
                Map<DataType, List<DataPoint>> latestMetrics = exerciseUpdate.getLatestMetrics();

                List<DataPoint> heartRate = latestMetrics.get(DataType.HEART_RATE_BPM);
                if (heartRate != null) {
                    double heartRateValue = heartRate.get(0).getValue().asDouble();
                    heartRateTextView.setText(
                            String.format(Locale.US, "%.0f", heartRateValue));
                }

                List<DataPoint> totalCalories = latestMetrics.get(DataType.TOTAL_CALORIES);
                if (totalCalories != null) {
                    calorieBurnTextView.setText(
                            String.format(Locale.US, "%.0f",
                                    calculateTotalCalories(totalCalories)));
                }
            }

            @Override
            public void onLapSummary(@NonNull ExerciseLapSummary exerciseLapSummary) {
                // Do something smart.
            }
        };
    }

    /** Sums up all the {@code DataPoint}s that were collected in this session. */
    private double calculateTotalCalories(List<DataPoint> totalCalories) {
        for (DataPoint calories : totalCalories) {
            totalCaloriesSum += calories.getValue().asDouble();
        }
        return totalCaloriesSum;
    }
}