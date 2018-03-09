package com.topzap.android.bakingtime;


import android.content.Context;
import android.os.Bundle;
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
import com.topzap.android.bakingtime.POJO.Recipe;
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
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_recipe_step, container, false);
    ButterKnife.bind(this, rootView);

    recipeSteps = getArguments().getParcelableArrayList("STEPS");
    position = getArguments().getInt("CHOSEN_STEP_ID");
    currentStep = recipeSteps.get(position);
    maxSteps = recipeSteps.size();

    backButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        if (position > 0) {
          position--;
          currentStep = recipeSteps.get(position);
          description.setText(currentStep.getDescription());
        }
        assignButtonVisibility();
      }
    });

    nextButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {

        if (position + 1 < maxSteps) {
          position++;
          currentStep = recipeSteps.get(position);
          description.setText(currentStep.getDescription());
        }
        assignButtonVisibility();
      }
    });

    assignButtonVisibility();

    description.setText(currentStep.getDescription());

    return rootView;
  }

  private void assignButtonVisibility() {
    if (currentStep.getId() == 0) {
      backButton.setVisibility(View.GONE);
    } else {
      backButton.setVisibility(View.VISIBLE);
    }

    if (currentStep.getId() == recipeSteps.size() - 1) {
      nextButton.setVisibility(View.GONE);
    } else {
      nextButton.setVisibility(View.VISIBLE);
    }
  }

}
