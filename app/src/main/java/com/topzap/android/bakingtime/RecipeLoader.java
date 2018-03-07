package com.topzap.android.bakingtime;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;
import com.topzap.android.bakingtime.POJO.Recipe;
import com.topzap.android.bakingtime.utils.NetworkUtils;
import java.util.ArrayList;


public class RecipeLoader extends AsyncTaskLoader<ArrayList<Recipe>> {

  private static final String TAG = RecipeLoader.class.getName();

  public RecipeLoader(Context context) {
    super(context);
    Log.d(TAG, "RecipeLoader: constructor called");
  }

  @Override
  protected void onStartLoading() {
    Log.d(TAG, "onStartLoading: called");
    forceLoad();
  }

  /**
   * On the background thread, get the supplied JSON from
   * https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json
   * and convert it to three sets of objects:
   *
   * @return a parent ArrayList of Recipe objects called recipes that contains
   *                    a child ArrayList of Ingredient objects called ingredients
   *                    a child ArrayList of RecipeStep objects called recipeSteps
   */

  @Override
  public ArrayList<Recipe> loadInBackground() {
    Log.d(TAG, "loadInBackground: loadinbackground called");

    ArrayList<Recipe> recipes = NetworkUtils.getRecipeData();

    return recipes;
  }
}
