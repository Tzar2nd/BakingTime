package com.topzap.android.bakingtime;


import android.content.Context;
import android.graphics.BitmapFactory;
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
import android.widget.TextView;
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
import com.topzap.android.bakingtime.POJO.RecipeStep;
import java.util.ArrayList;

public class RecipeStepFragment extends Fragment {

  private static final String TAG = RecipeStepFragment.class.getName();

  @BindView(R.id.tv_recipe_step_description)
  TextView description;
  @BindView(R.id.btn_back)
  Button backButton;
  @BindView(R.id.btn_next)
  Button nextButton;
  @BindView(R.id.exo_player_view)
  SimpleExoPlayerView playerView;

  private SimpleExoPlayer exoPlayer;

  RecipeStep currentStep;
  ArrayList<RecipeStep> recipeSteps;
  int position;
  int maxSteps;


  public RecipeStepFragment() {
    // Required empty public constructor
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putInt("CHOSEN_STEP_ID", position);
    Log.w(TAG, "onSaveInstanceState: Added bundle with position: " + position);
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    if(savedInstanceState != null) {
      position = savedInstanceState.getInt("CHOSEN_STEP_ID");
      Log.d(TAG, "onActivityCreated: Recieved bundle: " + position);

      currentStep = recipeSteps.get(position);
      maxSteps = recipeSteps.size();

      initializeButtons();

      description.setText(currentStep.getDescription());
      playerView.setDefaultArtwork(BitmapFactory.decodeResource
          (getResources(), R.drawable.nutella_pie));

      if (currentStep.getVideoURL() != null) {
        initializePlayer(Uri.parse(currentStep.getVideoURL()));
      }
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_recipe_step, container, false);
    ButterKnife.bind(this, rootView);

    recipeSteps = getArguments().getParcelableArrayList("STEPS");

    if(savedInstanceState == null) {
      Log.d(TAG, "onCreateView: NEW FRAG");
      position = getArguments().getInt("CHOSEN_STEP_ID");

      currentStep = recipeSteps.get(position);
      maxSteps = recipeSteps.size();

      initializeButtons();

      description.setText(currentStep.getDescription());
      playerView.setDefaultArtwork(BitmapFactory.decodeResource
          (getResources(), R.drawable.nutella_pie));

      if (currentStep.getVideoURL() != null) {
        initializePlayer(Uri.parse(currentStep.getVideoURL()));
      }
    }

      backButton.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          releasePlayer();
          if (position > 0) {
            position--;
            currentStep = recipeSteps.get(position);
            description.setText(currentStep.getDescription());

            if (currentStep.getVideoURL() != null) {
              initializePlayer(Uri.parse(currentStep.getVideoURL()));
            }
          }
          initializeButtons();
        }
      });

      nextButton.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          releasePlayer();

          if (position + 1 < maxSteps) {
            position++;
            currentStep = recipeSteps.get(position);
            description.setText(currentStep.getDescription());

            if (currentStep.getVideoURL() != null) {
              Log.d(TAG, "onClick: " + currentStep.getVideoURL());
              initializePlayer(Uri.parse(currentStep.getVideoURL()));
            }
          }
          initializeButtons();


        }
      });

    return rootView;
  }

  private void initializeButtons() {
    if (currentStep.getId() == 0) {
      backButton.setVisibility(View.INVISIBLE);
    } else {
      backButton.setVisibility(View.VISIBLE);
    }

    if (currentStep.getId() == recipeSteps.size() - 1) {
      nextButton.setVisibility(View.INVISIBLE);
    } else {
      nextButton.setVisibility(View.VISIBLE);
    }
  }

  private void initializePlayer(Uri mediaUri) {
    if (exoPlayer == null) {

      TrackSelector trackSelector = new DefaultTrackSelector();
      LoadControl loadControl = new DefaultLoadControl();
      exoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
      playerView.setPlayer(exoPlayer);

      String userAgent = Util.getUserAgent(getContext(), "Baking Time");
      MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
          getContext(), userAgent), new DefaultExtractorsFactory(), null, null);

      exoPlayer.prepare(mediaSource);
      exoPlayer.setPlayWhenReady(true);
    }
  }

  private void releasePlayer() {
    exoPlayer.stop();
    exoPlayer.release();
    exoPlayer = null;
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    releasePlayer();
  }
}
