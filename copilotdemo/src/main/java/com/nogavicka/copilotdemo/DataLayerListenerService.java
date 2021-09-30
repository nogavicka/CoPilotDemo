package com.nogavicka.copilotdemo;

import android.content.Intent;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

/** Listens to DataItems and Messages from the local node. */
public class DataLayerListenerService extends WearableListenerService {
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
