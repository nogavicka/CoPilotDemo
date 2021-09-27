package com.nogavicka.copilotdemo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/** Adapter used to show simple list of exercises in a workout. */
public class WorkoutListRecyclerViewAdapter extends
        RecyclerView.Adapter<WorkoutListRecyclerViewAdapter.ExerciseCardViewHolder> {

    private final ArrayList<ExerciseEntry> workoutList;
    private final WorkoutListFragment.OnExerciseSelectedListener onClickListener;
    private int selectedItem = -1;

    WorkoutListRecyclerViewAdapter(
            ArrayList<ExerciseEntry> workoutList,
            WorkoutListFragment.OnExerciseSelectedListener onClickListener) {
        this.workoutList = workoutList;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ExerciseCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.exercise_card, parent, false);
        return new ExerciseCardViewHolder(layoutView, onClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseCardViewHolder holder, int position) {
        if (workoutList != null && position < workoutList.size()) {
            ExerciseEntry workout = workoutList.get(position);
            holder.bindExercise(workout, position);
            if (selectedItem == position){
                holder.selectText();
            } else {
                holder.clearTextColor();
            }
        }
    }

    @Override
    public int getItemCount() {
        return workoutList.size();
    }

    /** View holder for each item in the workout's exercise list. */
    class ExerciseCardViewHolder extends RecyclerView.ViewHolder {

        private final WorkoutListFragment.OnExerciseSelectedListener listener;
        private final View itemView;

        public ExerciseCardViewHolder(@NonNull View itemView, WorkoutListFragment.OnExerciseSelectedListener listener) {
            super(itemView);
            this.itemView = itemView;
            this.listener = listener;
        }

        /**
         * Binds {@code ExerciseEntry} to the view at {@code position}.
         */
        public void bindExercise(ExerciseEntry exerciseEntry, int position) {
            // Populate item with correct text.
            ImageView thumbnail = itemView.findViewById(R.id.exercise_thumbnail);
            thumbnail.setImageDrawable(AppCompatResources.getDrawable(
                    itemView.getContext(), exerciseEntry.thumbnailResource));
            TextView exerciseTitle = itemView.findViewById(R.id.exercise_title);
            exerciseTitle.setText(exerciseEntry.title);
            TextView exerciseReps = itemView.findViewById(R.id.exercise_reps);
            exerciseReps.setText(String.valueOf(exerciseEntry.reps));
            TextView exercisePounds = itemView.findViewById(R.id.exercise_pounds);
            exercisePounds.setText(String.valueOf(exerciseEntry.pounds));

            // Set on click listener.
            itemView.setOnClickListener(
                    view -> {
                        if (position < workoutList.size()) {
                           listener.onExerciseSelected(
                                    exerciseEntry.title, exerciseEntry.coachHint, exerciseEntry.videoResource);

                            // Update the colors of items. Set the current one to blue, and the rest
                            // to default text color.
                            int previousItem = selectedItem;
                            selectedItem = position;
                            notifyItemChanged(previousItem);
                            notifyItemChanged(selectedItem);
                        }
                    }
            );
        }

        /** Colors all text blue to make it looks selected. */
        public void selectText() {
            ((TextView) itemView.findViewById(R.id.exercise_title))
                    .setTextColor(itemView.getResources().getColor(R.color.maya_blue));
            ((TextView) itemView.findViewById(R.id.exercise_reps))
                    .setTextColor(itemView.getResources().getColor(R.color.maya_blue));
            ((TextView) itemView.findViewById(R.id.exercise_reps_desc))
                    .setTextColor(itemView.getResources().getColor(R.color.maya_blue));
            ((TextView) itemView.findViewById(R.id.exercise_pounds))
                    .setTextColor(itemView.getResources().getColor(R.color.maya_blue));
            ((TextView) itemView.findViewById(R.id.exercise_pounds_desc))
                    .setTextColor(itemView.getResources().getColor(R.color.maya_blue));
        }

        /** Reselts the text color to black .*/
        public void clearTextColor() {
            ((TextView) itemView.findViewById(R.id.exercise_title))
                    .setTextColor(itemView.getResources().getColor(R.color.black));
            ((TextView) itemView.findViewById(R.id.exercise_reps))
                    .setTextColor(itemView.getResources().getColor(R.color.black));
            ((TextView) itemView.findViewById(R.id.exercise_reps_desc))
                    .setTextColor(itemView.getResources().getColor(R.color.black));
            ((TextView) itemView.findViewById(R.id.exercise_pounds))
                    .setTextColor(itemView.getResources().getColor(R.color.black));
            ((TextView) itemView.findViewById(R.id.exercise_pounds_desc))
                    .setTextColor(itemView.getResources().getColor(R.color.black));
        }
    }
}
