package com.topzap.android.bakingtime.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.topzap.android.bakingtime.R;
import com.topzap.android.bakingtime.data.RecipeStepAdapter;
import com.topzap.android.bakingtime.model.Recipe;
import com.topzap.android.bakingtime.utils.Config;
import java.util.List;

public class RecipeSummaryFragment extends Fragment implements RecipeStepAdapter.RecipeStepOnClickListener {

  private static final String TAG = RecipeSummaryFragment.class.getName();

  private OnRecipeStepSelectedListener mCallback;

  @BindView(R.id.tv_ingredients_list) TextView ingredientsTextView;
  @BindView(R.id.rv_recipe_steps) RecyclerView mStepsRecyclerView;

  Recipe currentRecipe;
  List<String> recipeSteps;
  RecipeStepAdapter mRecipeStepAdapter;

  @Override
  public void onClick(int position) {
    mCallback.onRecipeStepSelected(position);
  }

  public interface OnRecipeStepSelectedListener {
    void onRecipeStepSelected(int position);
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);

    // This ensures that the host activity has implemented the callback interface, else it throws an exception
    try {
      mCallback = (OnRecipeStepSelectedListener) context;
    } catch (ClassCastException e) {
      throw new ClassCastException(String.format("Must implement onclicklistener", context.toString()));
    }
  }

  public RecipeSummaryFragment() {
    // Required empty public constructor
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_recipe_summary, container, false);
    ButterKnife.bind(this, rootView);

    currentRecipe = getArguments().getParcelable(Config.KEY_RECIPE);

    ingredientsTextView.setText(currentRecipe.getIngredientsString());

    mStepsRecyclerView.setHasFixedSize(true);
    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
    mStepsRecyclerView.setLayoutManager(mLayoutManager);

    mRecipeStepAdapter = new RecipeStepAdapter(this);
    recipeSteps = currentRecipe.getRecipeStepsList();
    mStepsRecyclerView.setAdapter(mRecipeStepAdapter);
    mRecipeStepAdapter.addAll(recipeSteps);

    return rootView;
  }


}
