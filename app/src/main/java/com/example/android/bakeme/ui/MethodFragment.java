package com.example.android.bakeme.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakeme.R;
import com.example.android.bakeme.data.Recipe;
import com.example.android.bakeme.data.Recipe.Steps;
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
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * {@link MethodFragment} is a {@link Fragment} showing the detailed step in question of the current
 * recipe selected.
 */
public class MethodFragment extends Fragment implements ExoPlayer.EventListener {

    //description views
    @BindView(R.id.nav_prev_bt)
    ImageButton navPrevBt;
    @BindView(R.id.nav_next_bt)
    ImageButton navNextBt;
    @BindView(R.id.recipe_step_tv)
    TextView recipeStep;

    //For the Videoplayer
    @BindView(R.id.exo_play)
    SimpleExoPlayerView exoPlayerView;
    @BindView(R.id.video_thumbnail_iv)
    ImageView videoThumbnailIv;
    private SimpleExoPlayer exoPlayer;
    private MediaSessionCompat videoSession;
    private PlaybackStateCompat.Builder stateBuilder;
    private static final String TAG = MethodFragment.class.getSimpleName();
    Handler handler;

    //passed on data & setters to do so.
    Recipe recipe;
    Steps step;
    ArrayList<Steps> stepsList;

    public void setStepsList(ArrayList<Steps> stepsList) {
        this.stepsList = stepsList;
    }

    public void setStep(Steps step) {
        this.step = step;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public MethodFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_method, container, false);
        ButterKnife.bind(this, root);

        handler = new Handler();

        String thumbnail = step.getThumbnailurl();
        //if there are not thumbnails set image to null so app icon is shown
        assert thumbnail != null;
        if (thumbnail.isEmpty()) thumbnail = null;

        Picasso.with(getActivity()).load(thumbnail)
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
                .into(videoThumbnailIv);

        // Initialize the Media Session to display the video.
        initializeVideoSession();

        // Initialize the player.
        initializePlayer();

        updateNavButtons();

        updateStepText();

        //users wants to go one step back
        navPrevBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int stepId = step.getId() - 1;
                Steps prevStep = recipe.getSteps().get(stepId);
                setStep(prevStep);
                updateStepText();
                // Stop previous playback, prepare and play new
                startChosenVideo();
                updateNavButtons();
            }
        });

        //user wants to go one step forward
        navNextBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int stepId = step.getId() + 1;
                Steps nextStep = recipe.getSteps().get(stepId);
                setStep(nextStep);
                updateStepText();
                // Stop previous playback, prepare and play new
                startChosenVideo();
                updateNavButtons();
            }
        });

        return root;
    }

    private void startChosenVideo() {
        exoPlayer.stop();
        if (!step.getVideourl().isEmpty()) {
            MediaSource mediaSource = getMediaSource();
            exoPlayer.prepare(mediaSource);
            exoPlayer.setPlayWhenReady(true);
        } else {
            videoThumbnailIv.setVisibility(View.VISIBLE);
        }

    }

    @NonNull
    private MediaSource getMediaSource() {
        String userAgent = Util.getUserAgent(getActivity(), "BakeMe");
        return new ExtractorMediaSource(Uri.parse(step.getVideourl()),
                new DefaultDataSourceFactory(getActivity(), userAgent),
                new DefaultExtractorsFactory(), null, null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
        videoSession.setActive(false);
    }

    //Initializes the Media Session to be enabled with media buttons, transport controls, callbacks
    //and media controller.
    private void initializeVideoSession() {

        // Create a MediaSessionCompat for the videos to be viewed.
        videoSession = new MediaSessionCompat(getActivity(), TAG); //TODO: crashes on this line in Genymotion emulator (API 16)

        // Enable mediaButton ~ and transportControls callbacks
        videoSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // Do not let MediaButtons restart the player when the app is not visible.
        videoSession.setMediaButtonReceiver(null);

        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player.
        stateBuilder = new PlaybackStateCompat.Builder().setActions(
                PlaybackStateCompat.ACTION_PLAY |
                        PlaybackStateCompat.ACTION_PAUSE |
                        PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                        PlaybackStateCompat.ACTION_PLAY_PAUSE);

        videoSession.setPlaybackState(stateBuilder.build());

        // MySessionCallback has methods that handle callbacks from a media controller.
        videoSession.setCallback(new MethodFragment.MySessionCallback());

        // Start the Media Session since the activity is active.
        videoSession.setActive(true);
    }

    //Initialize ExoPlayer.
    public void initializePlayer() {
        if (exoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveVideoTrackSelection.Factory(new DefaultBandwidthMeter());
            DefaultTrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
            LoadControl loadControl = new DefaultLoadControl();

            //            TrackSelector trackSelect = new DefaultTrackSelector();
//            LoadControl loader = new DefaultLoadControl();
            exoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
            exoPlayerView.requestFocus();
            exoPlayerView.setPlayer(exoPlayer);

            // Set the ExoPlayer.EventListener to this activity.
            exoPlayer.addListener(this);

            // Prepare the MediaSource and start playing
            exoPlayer.prepare(getMediaSource());
            exoPlayer.setPlayWhenReady(true);
        }
    }

    // ExoPlayer Event Listeners
    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {
    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
    }

    /**
     * Method that is called when the ExoPlayer state changes. Used to update the MediaSession
     * PlayBackState to keep in sync, and post the media notification.
     *
     * @param playWhenReady true if ExoPlayer is playing, false if it's paused.
     * @param playbackState int describing the state of ExoPlayer. Can be STATE_READY, STATE_IDLE,
     *                      STATE_BUFFERING, or STATE_ENDED.
     */
    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if ((playbackState == ExoPlayer.STATE_READY) && playWhenReady) {
            stateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    exoPlayer.getCurrentPosition(), 1f);
            videoThumbnailIv.setVisibility(View.INVISIBLE); //hide thumbnail to play
        } else if ((playbackState == ExoPlayer.STATE_READY)) {
            stateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    exoPlayer.getCurrentPosition(), 1f);
            videoThumbnailIv.setVisibility(View.VISIBLE); // show thumbnail when paused
        }
        videoSession.setPlaybackState(stateBuilder.build());
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
    }

    @Override
    public void onPositionDiscontinuity() {
    }

    //Media Session Callbacks, where all external clients control the player.
    public class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            exoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            exoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            exoPlayer.seekTo(0);
        }
    }

    /**
     * {@link MediaButtonClickReceiver} is a {@link BroadcastReceiver} which will receive the
     * MEDIA_BUTTON intent from clients.
     */
    public class MediaButtonClickReceiver extends BroadcastReceiver {

        public MediaButtonClickReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            MediaButtonReceiver.handleIntent(videoSession, intent);
        }
    }

    //Release ExoPlayer when it is no longer needed.
    private void releasePlayer() {
        exoPlayer.stop();
        exoPlayer.release();
        exoPlayer = null;
    }

    private void updateStepText() {
        if (!step.getDescription().isEmpty()) {
            recipeStep.setText(step.getDescription());
        } else {
            recipeStep.setText(R.string.no_detail_step_available);
        }
    }

    //Ensure that the previous and next button only show when there is something to navigate to
    private void updateNavButtons() {
        if (step.getId() == 0) {
            navPrevBt.setVisibility(View.INVISIBLE);
        } else if (stepsList.size() == step.getId() + 1) {
            navNextBt.setVisibility(View.INVISIBLE);
        } else {
            navPrevBt.setVisibility(View.VISIBLE);
            navNextBt.setVisibility(View.VISIBLE);
        }
    }
}