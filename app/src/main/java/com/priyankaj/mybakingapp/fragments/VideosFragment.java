package com.priyankaj.mybakingapp.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.priyankaj.mybakingapp.R;
import com.priyankaj.mybakingapp.models.StepsModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideosFragment extends Fragment implements ExoPlayer.EventListener{
    private static final String PLAYER_POSITION = "PLAYER_POSITION";
    @BindView(R.id.playerView)
    SimpleExoPlayerView simpleExoPlayerView;
    @BindView(R.id.description)
    TextView description;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.prev_button)
    Button prevButton;
    @BindView(R.id.next_button)
    Button nextButton;
    @BindView(R.id.video_container)
    LinearLayout videoContainer;

    private List<StepsModel> list;
    private int position;
    private static final String LIST_INDEX = "position";
    private static final String STEP_ID_LIST = "step";
    @BindView(R.id.thumbnail)
    ImageView thumbnail;


    private SimpleExoPlayer player;

    private static MediaSessionCompat mediaSession;
    private PlaybackStateCompat.Builder stateBuilder;
    long play_position;
    private Uri video_uri;
    public VideosFragment() {
    }

    private static final String PLAY_WHEN_READY = "PLAY_WHEN_READY";

    private void previousStep() {
        if (position == 0) {
            Toast.makeText(getContext(), "No previous step", Toast.LENGTH_SHORT).show();
        } else {
            position--;
        }
        releasePlayer();
        video_uri = Uri.parse(list.get(position).getVideoURL());
        initializePlayer(video_uri);
        description.setText(list.get(position).getDescription());


    }

    private void nextstep() {
        if (position < list.size() - 1) {
            position++;
        } else {
            Toast.makeText(getContext(), "No more next steps", Toast.LENGTH_SHORT).show();
        }
        releasePlayer();
        video_uri = Uri.parse(list.get(position).getVideoURL());
        initializePlayer(video_uri);
        description.setText(list.get(position).getDescription());

    }

    private void initializeMediaSession() {

        mediaSession = new MediaSessionCompat(getContext(), "VideoFragment");

        mediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        mediaSession.setMediaButtonReceiver(null);

        stateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mediaSession.setPlaybackState(stateBuilder.build());

        mediaSession.setCallback(new MySessionCallback());


        mediaSession.setActive(true);
    }

    boolean playWhenReady;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.video_fragment,container,false);
        ButterKnife.bind(this,rootView);

        if (savedInstanceState != null) {
            play_position = savedInstanceState.getLong(PLAYER_POSITION);
            position = savedInstanceState.getInt(LIST_INDEX);
            list = savedInstanceState.getParcelableArrayList(STEP_ID_LIST);
            playWhenReady = savedInstanceState.getBoolean(PLAY_WHEN_READY);
            Log.v("player_position", "" + play_position);
        } else
            play_position = 0;

        if (list != null) {


            Log.v("test", "nothing");
            simpleExoPlayerView.setDefaultArtwork(BitmapFactory.decodeResource
                    (getResources(), R.drawable.video_placeholder));


            initializeMediaSession();
            Log.v("player_position2", "" + play_position);
            video_uri = Uri.parse(list.get(position).getVideoURL());
            Log.v("player_position3", "" + play_position); //
            initializePlayer(video_uri);
            Log.v("player_position4", "" + play_position);
            title.setText(list.get(position).getShortDescription());
            description.setText(list.get(position).getDescription());


            Glide.with(getContext()).load(list.get(position).getThumbnailURL()).placeholder(R.drawable.ic_food_placeholder)
                    .error(R.drawable.nothumb).into(thumbnail);

        }

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previousStep();
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextstep();
            }
        });
        return rootView;
    }

    private void initializePlayer(Uri videoURL) {
        if (player == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            player = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            simpleExoPlayerView.setPlayer(player);
            player.addListener(this);
            String userAgent = Util.getUserAgent(getContext(), getString(R.string.app_name));
            MediaSource mediaSource = new ExtractorMediaSource(videoURL, new DefaultDataSourceFactory(getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            Log.v("player_init", "" + play_position);
            player.seekTo(play_position);
            player.prepare(mediaSource);
            player.setPlayWhenReady(playWhenReady);
        }
    }

    private void saveState() {
        if (player != null) {
            playWhenReady = player.getPlayWhenReady();
            play_position = Math.max(0, player.getCurrentPosition());
        }
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setStepList(ArrayList<StepsModel> stepList) {
        list = stepList;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList(STEP_ID_LIST, (ArrayList<? extends Parcelable>) list);
        outState.putInt(LIST_INDEX, position);
        outState.putLong(PLAYER_POSITION, play_position);
        outState.putBoolean(PLAY_WHEN_READY, playWhenReady);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (video_uri != null)
            initializePlayer(video_uri);
        //player.setPlayWhenReady(false);
    }
    @Override
    public void onPause(){
        super.onPause();
        play_position = Math.max(0, player.getCurrentPosition());
        Log.v("play_pause", "" + play_position);
        releasePlayer();
        if (mediaSession != null) {
            mediaSession.setActive(false);
        }
    }

    @Override
    public void onStop(){
        super.onStop();
        // player_position=Math.max(0,player.getCurrentPosition());
        releasePlayer();
        if (mediaSession != null) {
            mediaSession.setActive(false);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //player_position=Math.max(0,player.getCurrentPosition());
        releasePlayer();
        if (mediaSession != null) {
            mediaSession.setActive(false);
        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if ((playbackState == ExoPlayer.STATE_READY) && playWhenReady) {
            stateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    player.getCurrentPosition(), 1f);
        } else if ((playbackState == ExoPlayer.STATE_READY)) {
            stateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    player.getCurrentPosition(), 1f);
        }
        mediaSession.setPlaybackState(stateBuilder.build());
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            player.setPlayWhenReady(true);
        }
        
        @Override
        public void onPause() {
            player.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            player.seekTo(0);
        }
    }

    public static class MediaReceiver extends BroadcastReceiver {
        public MediaReceiver() {
        }
        @Override
        public void onReceive(Context context, Intent intent) {
            MediaButtonReceiver.handleIntent(mediaSession, intent);
        }
    }

    private void releasePlayer() {
        saveState();
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }
    }
    
}
