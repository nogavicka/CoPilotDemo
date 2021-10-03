package com.nogavicka.copilotdemo;

/**
 * Exercise stats for the ongoing exercise. Contains information for back and forth communication
 * between Wear OS and Phone.
 */
public class ExerciseStats {
    public final double heartRate;
    public final double calorieBurn;

    ExerciseStats(double heartRate, double calorieBurn) {
        this.heartRate = heartRate;
        this.calorieBurn = calorieBurn;
    }
}
