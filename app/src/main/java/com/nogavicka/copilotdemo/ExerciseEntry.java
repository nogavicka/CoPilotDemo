package com.nogavicka.copilotdemo;

import java.util.ArrayList;

/**
 * Exercise entry for the workout. Contains information such as resources for thumbnail and video,
 * title, number of reps, and pounds.
 */
public class ExerciseEntry {
    private static final String HAPPY = String.valueOf(Character.toChars(0x1F60A));
    public final int thumbnailResource;
    public final int videoResource;
    public final String title;
    public final String coachHint;
    public final int reps;
    public final int pounds;

    public ExerciseEntry(int thumbnailResource, int videoResource, String title,
                         String coachHint, int reps, int pounds) {
        this.thumbnailResource = thumbnailResource;
        this.videoResource = videoResource;
        this.title = title;
        this.coachHint = coachHint;
        this.reps = reps;
        this.pounds = pounds;
    }

    /** Creates a dummy workout. */
    public static ArrayList<ExerciseEntry> initWorkoutEntryList() {
        ArrayList<ExerciseEntry> exerciseEntryArrayList = new ArrayList<>();
        exerciseEntryArrayList.add(
                new ExerciseEntry(R.drawable.dumbbell_step_up_to_reverse_lunge,
                        R.raw.dumbbell_step_up_to_reverse_lunge,
                        "Dumbbell step up to reverse lunge",
                        "Don't worry, be happy " + HAPPY,
                        10, 15));
        exerciseEntryArrayList.add(
                new ExerciseEntry(R.drawable.front_rack_box_squat,
                        R.raw.front_rack_box_squat,
                        "Front Rack Box Squat",
                        "Don't worry, be happy " + HAPPY,
                        10, 15));
        exerciseEntryArrayList.add(
                new ExerciseEntry(R.drawable.half_kneeling_kettlebell_lumberjack,
                        R.raw.half_kneeling_kettlebell_lumberjack,
                        "Half Kneeling Kettlebell Lumberjack",
                        "Don't worry, be happy " + HAPPY,
                        10, 15));
        exerciseEntryArrayList.add(
                new ExerciseEntry(R.drawable.kettlebell_jump_squat,
                        R.raw.kettlebell_jump_squat,
                        "Kettlebell Jump Squat",
                        "Don't worry, be happy " + HAPPY,
                        10, 15));
        exerciseEntryArrayList.add(
                new ExerciseEntry(R.drawable.unilateral_dumbbell_hip_thrust,
                        R.raw.unilateral_dumbbell_hip_thrust,
                        "Unilateral Dumbbell Hip Thrust",
                        "Don't worry, be happy " + HAPPY,
                        10, 15));
        return exerciseEntryArrayList;
    }
}
