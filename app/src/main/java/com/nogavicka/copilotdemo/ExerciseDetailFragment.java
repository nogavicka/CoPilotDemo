package com.nogavicka.copilotdemo;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.RawResourceDataSource;
import com.google.android.gms.wearable.MessageClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;
import com.google.gson.Gson;

import java.util.Locale;

/**  Fragment that displays the exercise video and other details. */
public class ExerciseDetailFragment extends Fragment implements
        MessageClient.OnMessageReceivedListener {

    private final SimpleExoPlayer player;
    private final int videoResorceId;
    private final String coachHint;
    private View view;

    public ExerciseDetailFragment(SimpleExoPlayer player, String coachHint, int videoResorceId) {
        this.player = player;
        this.coachHint = coachHint;
        this.videoResorceId = videoResorceId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_exercise_detail, container, false);
        setUpExoPlayer(view, videoResorceId);

        TextView exerciseHint = view.findViewById(R.id.exercise_hint);
        exerciseHint.setText(coachHint);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Instantiates clients without member variables, as clients are inexpensive to create and
        // won't lose their listeners. (They are cached and shared between GoogleApi instances.)
        Wearable.getMessageClient(getContext()).addListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        Wearable.getMessageClient(getContext()).removeListener(this);
    }

    /** Sets up Exo Player for looping the workout video. */
    private void setUpExoPlayer(View view, int videoResorceId) {
        // Loop.
        player.setRepeatMode(Player.REPEAT_MODE_ONE);

        PlayerView playerView = view.findViewById(R.id.exoplayer_video);
        // Bind the player to the view.
        playerView.setPlayer(player);
        // Don't turn off the screen while video is playing.
        playerView.setKeepScreenOn(true);
        // Set the play destination from resources.
        Uri uri = RawResourceDataSource.buildRawResourceUri(videoResorceId);
        // Build the media item.
        MediaItem mediaItem = MediaItem.fromUri(uri);
        // Set the media item to be played.
        player.setMediaItem(mediaItem);
        // Prepare the player.
        player.prepare();
        // Start the playback.
        player.setPlayWhenReady(true);
    }

    @Override
    public void onMessageReceived(@NonNull MessageEvent messageEvent) {
        byte[] data = messageEvent.getData();
        String dataString = new String(data);
        Gson gson = new Gson();
        ExerciseStats exerciseStats = gson.fromJson(dataString, ExerciseStats.class);

        TextView heartRateView = view.findViewById(R.id.heart_rate_text);
        heartRateView.setText(String.format(Locale.US, "%.0f", exerciseStats.heartRate));
        TextView calorieBurnView = view.findViewById(R.id.calorie_burn_text);
        calorieBurnView.setText(String.format(Locale.US, "%.0f", exerciseStats.calorieBurn));

    }
}
