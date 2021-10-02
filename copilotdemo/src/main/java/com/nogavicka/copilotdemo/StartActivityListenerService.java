package com.nogavicka.copilotdemo;

import android.content.Intent;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * Listens to DataItems and Messages from the local node. This is necessary to start the
 * app on the watch, if the app is not already running.
 */
public class StartActivityListenerService extends WearableListenerService {
    private static final String START_ACTIVITY_PATH = "/start-activity";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        // Check to see if the message is to start an activity.
        if (messageEvent.getPath().equals(START_ACTIVITY_PATH)) {
            Intent startIntent = new Intent(this, MainActivity.class);
            startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startIntent);
        }
    }
}
