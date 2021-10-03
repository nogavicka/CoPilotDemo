package com.nogavicka.copilotdemo;

import androidx.annotation.WorkerThread;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.gms.wearable.MessageClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;
import com.google.gson.Gson;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutionException;

/** Main Android activity. */
public class MainActivity extends AppCompatActivity
        implements WorkoutListFragment.OnExerciseSelectedListener,
        MessageClient.OnMessageReceivedListener {
    private static final String TAG = MainActivity.class.toString();

    private static final String START_ACTIVITY_PATH = "/start-activity";

    SimpleExoPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        player = new SimpleExoPlayer.Builder(this).build();
        inflateWorkoutListFragment();
        new StartWearableActivityTask().execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Instantiates clients without member variables, as clients are inexpensive to create and
        // won't lose their listeners. (They are cached and shared between GoogleApi instances.)
        Wearable.getMessageClient(this).addListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Wearable.getMessageClient(this).removeListener(this);
    }

    @Override
    protected void onDestroy() {
        if (player != null) {
            player.release();
            player = null;
        }
        super.onDestroy();
    }

    /** Infates the workout list fragment on the left. */
    private void inflateWorkoutListFragment() {
        WorkoutListFragment workoutListFragment = new WorkoutListFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.exercises, workoutListFragment)
                .commit();
    }

    @Override
    public void onExerciseSelected(String title, String coachHint, int videoResource) {
        ExerciseDetailFragment detailFragment = new ExerciseDetailFragment(
                player, title, coachHint, videoResource);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.details, detailFragment)
                    .commit();
    }

    /**
     * Task that starts the companion app on a Wearable that's connected to the phone.
     * Note: AsyncTasks were deprecated, but Kotlin offers some awesome lifecycle awareness:
     * https://developer.android.com/topic/libraries/architecture/coroutines.
     */
    private class StartWearableActivityTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... args) {
            Collection<String> nodes = getNodes();
            for (String node : nodes) {
                sendStartActivityMessage(node);
            }
            return null;
        }
    }

    @WorkerThread
    private void sendStartActivityMessage(String node) {
        Task<Integer> sendMessageTask =
                Wearable.getMessageClient(this).sendMessage(node, START_ACTIVITY_PATH, new byte[0]);

        try {
            // Block on a task and get the result synchronously (because this is on a background
            // thread).
            Integer result = Tasks.await(sendMessageTask);
            Log.d(TAG, "Message sent: " + result);
        } catch (ExecutionException exception) {
            Log.e(TAG, "Task failed: " + exception);
        } catch (InterruptedException exception) {
            Log.e(TAG, "Interrupt occurred: " + exception);
        }
    }

    @WorkerThread
    private Collection<String> getNodes() {
        HashSet<String> results = new HashSet<>();

        Task<List<Node>> nodeListTask =
                Wearable.getNodeClient(getApplicationContext()).getConnectedNodes();

        try {
            // Block on a task and get the result synchronously (because this is on a background
            // thread).
            List<Node> nodes = Tasks.await(nodeListTask);

            for (Node node : nodes) {
                results.add(node.getId());
            }
        } catch (ExecutionException exception) {
            Log.e(TAG, "Task failed: " + exception);
        } catch (InterruptedException exception) {
            Log.e(TAG, "Interrupt occurred: " + exception);
        }

        return results;
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
       Log.d(TAG,
                "onMessageReceived() A message from watch was received:"
                        + messageEvent.getRequestId()
                        + " "
                        + messageEvent.getPath());
       byte[] data = messageEvent.getData();
       String dataString = new String(data);
       Log.d(TAG, "Received the following string: " + dataString);
       Gson gson = new Gson();
       ExerciseStats exerciseStats = gson.fromJson(dataString, ExerciseStats.class);
       Log.d(TAG, "Received the following string: " + exerciseStats.toString());
    }

}
