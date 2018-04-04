package com.topzap.android.bakingtime.activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import com.topzap.android.bakingtime.R;
import com.topzap.android.bakingtime.fragments.RecipeStepFragment;
import com.topzap.android.bakingtime.fragments.RecipeSummaryFragment;
import com.topzap.android.bakingtime.fragments.RecipeSummaryFragment.OnRecipeStepSelectedListener;
import com.topzap.android.bakingtime.model.Recipe;

public class RecipeActivity extends FragmentActivity
    implements OnRecipeStepSelectedListener {

  private static final String TAG = RecipeActivity.class.getName();

  Recipe currentRecipe;

  @Override
  public void onRecipeStepSelected(int position) {

    // Set up and attach the fragment
    FragmentManager manager = getSupportFragmentManager();
    FragmentTransaction transaction = manager.beginTransaction();
    RecipeStepFragment recipeStepFragment = new RecipeStepFragment();

    // Send the position ID to the new fragment
    Bundle bundle = new Bundle();

    bundle.putParcelableArrayList("STEPS", currentRecipe.getRecipeSteps());
    bundle.putInt("CHOSEN_STEP_ID", position);

    recipeStepFragment.setArguments(bundle);

    // Send the recipe object to the recipe step fragment
    transaction.replace(R.id.container_recipe_list, recipeStepFragment);
    transaction.addToBackStack(null);
    transaction.commit();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_recipe);

    Intent intent = getIntent();
    currentRecipe = intent.getParcelableExtra("RECIPE");
    Log.d(TAG, "onCreate: " + currentRecipe.getName());

    Bundle bundle = new Bundle();
    bundle.putParcelable("RECIPE", currentRecipe);

    // Set up and attach the fragment
    FragmentManager manager = getSupportFragmentManager();
    FragmentTransaction transaction = manager.beginTransaction();

    if (savedInstanceState != null) {
      Fragment fragment = getSupportFragmentManager()
          .getFragment(savedInstanceState, "RecipeStepFragment");

      if (fragment instanceof RecipeStepFragment) {
        //transaction.replace(R.id.container_recipe_list, fragment);
      }
    } else {
      // First time setup
      RecipeSummaryFragment recipeSummaryFragment = new RecipeSummaryFragment();

      // Send the recipe object to the recipe summary fragment
      recipeSummaryFragment.setArguments(bundle);

      transaction.replace(R.id.container_recipe_list, recipeSummaryFragment);
      transaction.commit();
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
