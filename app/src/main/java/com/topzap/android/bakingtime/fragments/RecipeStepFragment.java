package com.topzap.android.bakingtime.fragments;


import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;
import com.topzap.android.bakingtime.R;
import com.topzap.android.bakingtime.model.RecipeStep;
import com.topzap.android.bakingtime.utils.Config;
import com.topzap.android.bakingtime.utils.Utils;
import java.util.ArrayList;

public class RecipeStepFragment extends Fragment {

  private static final String TAG = RecipeStepFragment.class.getName();

  @BindView(R.id.tv_recipe_step_description) TextView mDescription;
  @BindView(R.id.btn_back) Button mBackButton;
  @BindView(R.id.btn_next) Button mNextButton;
  @BindView(R.id.exo_player_frame) FrameLayout mExoPlayerFrame;
  @BindView(R.id.exo_player_view) SimpleExoPlayerView mPlayerView;
  @BindView(R.id.iv_no_video) ImageView mImageViewNoVideo;

  // ButterKnife Resource binding
  @BindDrawable(R.mipmap.icon_cupcake) Drawable mRecipeDefaultImage;

  private SimpleExoPlayer mExoPlayer;

  private RecipeStep mCurrentStep;
  private ArrayList<RecipeStep> mRecipeSteps;
  private int mPosition;
  private int mMaxSteps;
  private long mPlayerPosition;
  private String mRecipeDescription;
  private String mThumbnailUrl;
  private String mVideoUrl;

  public RecipeStepFragment() {
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    if (savedInstanceState != null) {

      // Get the current step parameters
      mPosition = savedInstanceState.getInt(Config.KEY_SELECTED_STEP);
      mPlayerPosition = savedInstanceState.getLong(Config.KEY_EXO_PLAYER_POSITION);
      setCurrentRecipeStepData(mPosition);
      mDescription.setText(mRecipeDescription);

      initializeButtons();
      setPlayerVisibility();
    } else {
      setPlayerVisibility();
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_recipe_step, container, false);
    ButterKnife.bind(this, rootView);

    mRecipeSteps = getArguments().getParcelableArrayList(Config.KEY_SELECTED_RECIPE_STEPS);

    if (savedInstanceState == null) {
      Log.d(TAG, "onCreateView: NEW FRAG");
      mPosition = getArguments().getInt(Config.KEY_SELECTED_STEP);

      setCurrentRecipeStepData(mPosition);
      mDescription.setText(mRecipeDescription);

      initializeButtons();

    } else {
      mPlayerPosition = savedInstanceState.getLong(Config.KEY_EXO_PLAYER_POSITION);
    }

    mBackButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        releasePlayer();
        if (mPosition > 0) {
          mPosition--;
          setCurrentRecipeStepData(mPosition);
          mDescription.setText(mRecipeDescription);
          setPlayerVisibility();

          if (!Utils.isEmptyString(mVideoUrl)) {
            initializePlayer(Uri.parse(mVideoUrl));
          }
        }
        initializeButtons();
      }
    });

    mNextButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        releasePlayer();

        if (mPosition + 1 < mMaxSteps) {
          mPosition++;
          setCurrentRecipeStepData(mPosition);
          mDescription.setText(mRecipeDescription);
          setPlayerVisibility();

          if (!Utils.isEmptyString(mVideoUrl)) {
            initializePlayer(Uri.parse(mVideoUrl));
          }
        }
        initializeButtons();
      }
    });

    return rootView;
  }

  private void setCurrentRecipeStepData(int position) {
    mCurrentStep = mRecipeSteps.get(position);
    mVideoUrl = mCurrentStep.getVideoURL();
    mRecipeDescription = mCurrentStep.getDescription();
    mMaxSteps = mRecipeSteps.size();
    mThumbnailUrl = mCurrentStep.getThumbnailURL();
  }

  /**
   * Initialize video player if there is a video url else show an image instead
   */
  private void setPlayerVisibility() {
    if (!Utils.isEmptyString(mVideoUrl)) {
      Log.d(TAG, "setPlayerVisibility: Launching Player " + mVideoUrl);
      ButterKnife.apply(mImageViewNoVideo, Utils.VISIBILITY, View.GONE);
      initializePlayer(Uri.parse(mCurrentStep.getVideoURL()));
    } else {
      Log.d(TAG, "setPlayerVisibility: Setting ImageView");
      ButterKnife.apply(mImageViewNoVideo, Utils.VISIBILITY, View.VISIBLE);

      if (!Utils.isEmptyString(mThumbnailUrl)) {
        Picasso.with(getContext())
            .load(mThumbnailUrl)
            .placeholder(mRecipeDefaultImage)
            .error(mRecipeDefaultImage)
            .into(mImageViewNoVideo);
      } else {
        mImageViewNoVideo.setImageDrawable(mRecipeDefaultImage);
      }
    }
  }

  private void initializeButtons() {
    if (mCurrentStep.getId() == 0) {
      mBackButton.setVisibility(View.INVISIBLE);
    } else {
      mBackButton.setVisibility(View.VISIBLE);
    }

    if (mCurrentStep.getId() == mRecipeSteps.size() - 1) {
      mNextButton.setVisibility(View.INVISIBLE);
    } else {
      mNextButton.setVisibility(View.VISIBLE);
    }
  }

  private void initializePlayer(Uri mediaUri) {
    if (mExoPlayer == null) {
      TrackSelector trackSelector = new DefaultTrackSelector();
      LoadControl loadControl = new DefaultLoadControl();
      mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
      mPlayerView.setPlayer(mExoPlayer);

      String userAgent = Util.getUserAgent(getContext(), "Baking Time");
      MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
          getContext(), userAgent), new DefaultExtractorsFactory(), null, null);

      mExoPlayer.prepare(mediaSource);
      mExoPlayer.setPlayWhenReady(true);
      mExoPlayer.seekTo(mPlayerPosition);

      mPlayerView.setDefaultArtwork(BitmapFactory.decodeResource
          (getResources(), R.color.colorText));
    }
  }

  private void releasePlayer() {
    if (mExoPlayer != null) {
      mExoPlayer.stop();
      mExoPlayer.release();
      mExoPlayer = null;
    }
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putInt(Config.KEY_SELECTED_STEP, mPosition);
    outState.putLong(Config.KEY_EXO_PLAYER_POSITION, mPlayerPosition);
    outState.putString(Config.KEY_VIDEO_URL, mVideoUrl);
  }

  @Override
  public void onPause() {
    super.onPause();
    if (mExoPlayer != null) {
      mPlayerPosition = mExoPlayer.getCurrentPosition();
    }
    //releasePlayer();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    releasePlayer();
  }
}
