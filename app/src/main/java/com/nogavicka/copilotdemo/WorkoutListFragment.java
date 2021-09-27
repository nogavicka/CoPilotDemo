package com.nogavicka.copilotdemo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/** Fragment that displays the workout. */
public class WorkoutListFragment extends Fragment {
    OnExerciseSelectedListener onExcerciseSelectedListener;

    /**
     * Container activity must implement this in order to inflate the fragment in the right
     * container.
     */
    public interface OnExerciseSelectedListener {
        void onExerciseSelected(String title, String coachHint, int videoResource);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            onExcerciseSelectedListener = (OnExerciseSelectedListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement OnExcerciseSelectedListener");
        }
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workout_list, container, false);

        // Set up the RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        WorkoutListRecyclerViewAdapter adapter = new WorkoutListRecyclerViewAdapter(
                ExerciseEntry.initWorkoutEntryList(), onExcerciseSelectedListener);
        recyclerView.setAdapter(adapter);
        return view;
    }
}

