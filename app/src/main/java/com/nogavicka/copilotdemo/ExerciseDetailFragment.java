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

/**  Fragment that displays the exercise video and other details. */
public class ExerciseDetailFragment extends Fragment {

    private final SimpleExoPlayer player;
    private final int videoResorceId;
    private final String title;
    private final String coachHint;

    public ExerciseDetailFragment(SimpleExoPlayer player, String title, String coachHint, int videoResorceId) {
        this.player = player;
        this.title = title;
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
        View view = inflater.inflate(R.layout.fragment_exercise_detail, container, false);
        setUpExoPlayer(view, videoResorceId);

        TextView exerciseHint = view.findViewById(R.id.exercise_hint);
        exerciseHint.setText(coachHint);

        return view;
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
}
