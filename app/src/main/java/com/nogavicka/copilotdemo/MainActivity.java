package com.nogavicka.copilotdemo;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.exoplayer2.SimpleExoPlayer;

/** Main Android activity. */
public class MainActivity extends AppCompatActivity
        implements WorkoutListFragment.OnExerciseSelectedListener {

    SimpleExoPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        player = new SimpleExoPlayer.Builder(this).build();
        inflateWorkoutListFragment();
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
}
