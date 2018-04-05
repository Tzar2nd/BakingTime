package com.topzap.android.bakingtime.activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import butterknife.BindBool;
import butterknife.ButterKnife;
import com.topzap.android.bakingtime.R;
import com.topzap.android.bakingtime.fragments.RecipeStepFragment;
import com.topzap.android.bakingtime.fragments.RecipeSummaryFragment;
import com.topzap.android.bakingtime.fragments.RecipeSummaryFragment.OnRecipeStepSelectedListener;
import com.topzap.android.bakingtime.model.Recipe;
import com.topzap.android.bakingtime.utils.Config;

public class RecipeActivity extends AppCompatActivity
    implements OnRecipeStepSelectedListener {

  private static final String TAG = RecipeActivity.class.getName();

  Recipe mCurrentRecipe;
  private Bundle mRecipeBundle;
  FragmentManager mFragmentManager;

  @BindBool(R.bool.two_pane_layout) boolean mIsTwoPane;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_recipe);
    ButterKnife.bind(this);

    // Get the current Recipe from the bundle, and add it to a bundle again in case of
    // orientation or activity state change.
    Intent intent = getIntent();
    mRecipeBundle = intent.getExtras();
    mCurrentRecipe = intent.getParcelableExtra(Config.KEY_RECIPE);

    setTitle(mCurrentRecipe.getName() + " Recipe");

    // Get an instance of FragmentManager and begin a transaction
    mFragmentManager = getSupportFragmentManager();

    // First time set up - attach one recipe list fragment to a single pane layout, and both
    // recipe list and recipe detail steps fragment if a two pane layout
    if (savedInstanceState == null) {

      if (mIsTwoPane) {
        Log.d(TAG, "onCreate: TWO PANE");
        // Create two fragments; one for recipe on the left and one for the steps on the right
        RecipeSummaryFragment recipeSummaryFragment = new RecipeSummaryFragment();
        mRecipeBundle.putInt(Config.KEY_SELECTED_STEP, 0);    // Highlight first step
        recipeSummaryFragment.setArguments(mRecipeBundle);

        mFragmentManager
            .beginTransaction()
            .replace(R.id.container_recipe_list, recipeSummaryFragment)
            .commit();

        // Create second fragment for recipe step detail
        RecipeStepFragment recipeStepFragment = new RecipeStepFragment();
        mRecipeBundle.putInt(Config.KEY_SELECTED_STEP, 0);
        mRecipeBundle.putParcelableArrayList(Config.KEY_SELECTED_RECIPE_STEPS,
            mCurrentRecipe.getRecipeSteps());
        recipeStepFragment.setArguments(mRecipeBundle);

        mFragmentManager
            .beginTransaction()
            .replace(R.id.container_recipe_step_detail, recipeStepFragment)
            .addToBackStack(null)
            .commit();
      } else {
        Log.d(TAG, "onCreate: SINGLE PANE");
        // Create a fragment only for the recipe and steps if single pane
        RecipeSummaryFragment recipeSummaryFragment = new RecipeSummaryFragment();
        mRecipeBundle.putInt(Config.KEY_SELECTED_STEP, 0);
        mRecipeBundle.putParcelableArrayList(Config.KEY_SELECTED_RECIPE_STEPS,
            mCurrentRecipe.getRecipeSteps());
        recipeSummaryFragment.setArguments(mRecipeBundle);

        mFragmentManager
            .beginTransaction()
            .replace(R.id.container_recipe_list, recipeSummaryFragment)
            .commit();
      }
    }
  }

  @Override
  public void onBackPressed() {
    Fragment fragment = mFragmentManager.findFragmentByTag("RECIPE_STEPS");

    // If single pane and recipe steps is showing, on a back press swap to the recipe list fragment
    // instead of going back to the main menu
    if (!mIsTwoPane) {
      if (fragment != null && fragment.isVisible()) {
        RecipeSummaryFragment recipeSummaryFragment = new RecipeSummaryFragment();
        mRecipeBundle.putInt(Config.KEY_SELECTED_STEP, 0);    // Highlight first step
        recipeSummaryFragment.setArguments(mRecipeBundle);

        mFragmentManager
            .beginTransaction()
            .replace(R.id.container_recipe_list, recipeSummaryFragment)
            .commit();
      } else {
        super.onBackPressed();
      }
    }
  }

  @Override
  public void onRecipeStepSelected(int position) {

    Bundle bundle = new Bundle();
    bundle
        .putParcelableArrayList(Config.KEY_SELECTED_RECIPE_STEPS, mCurrentRecipe.getRecipeSteps());
    bundle.putInt(Config.KEY_SELECTED_STEP, position);

    RecipeStepFragment recipeStepFragment = new RecipeStepFragment();
    recipeStepFragment.setArguments(bundle);

    // Set up and attach the fragment
    if (mIsTwoPane) {
      mFragmentManager
          .beginTransaction()
          .replace(R.id.container_recipe_step_detail, recipeStepFragment)
          .addToBackStack(null)
          .commit();
    } else {
      mFragmentManager
          .beginTransaction()
          .replace(R.id.container_recipe_list, recipeStepFragment, "RECIPE_STEPS")
          .commit();
    }
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);

    Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container_recipe_list);

    if (fragment instanceof RecipeStepFragment) {
      getSupportFragmentManager().putFragment(outState, "RecipeStepFragment", fragment);
    }

  }
}
