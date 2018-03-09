package com.topzap.android.bakingtime;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import com.topzap.android.bakingtime.POJO.Recipe;
import java.util.ArrayList;

public class RecipeActivity extends FragmentActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    final String TAG = RecipeActivity.class.getName();

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_recipe);

    Intent intent = getIntent();
    Recipe currentRecipe = intent.getParcelableExtra("RECIPE");
    Log.d(TAG, "onCreate: " + currentRecipe.getName());

    Bundle bundle = new Bundle();
    bundle.putParcelable("RECIPE", currentRecipe);

    // Set up and attach the fragment
    FragmentManager manager = getSupportFragmentManager();
    FragmentTransaction transaction = manager.beginTransaction();
    RecipeSummaryFragment recipeSummaryFragment = new RecipeSummaryFragment();

    // Send the recipe object to the recipe summary fragment
    recipeSummaryFragment.setArguments(bundle);

    transaction.add(R.id.recipe_list_container, recipeSummaryFragment);
    transaction.commit();

  }
}
