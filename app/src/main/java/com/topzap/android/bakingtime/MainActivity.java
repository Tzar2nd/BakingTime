package com.topzap.android.bakingtime;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.topzap.android.bakingtime.POJO.Ingredient;
import com.topzap.android.bakingtime.POJO.Recipe;
import com.topzap.android.bakingtime.POJO.RecipeStep;
import com.topzap.android.bakingtime.data.RecipeAdapter;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Recipe>> {

  private static final String TAG = MainActivity.class.getName();

  private static final int RECIPE_LOADER_ID = 1;

  @BindView(R.id.rv_main_activity)
  RecyclerView mRecipeRecyclerView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);

    checkInternetConnection();

    ArrayList<Recipe> mRecipes = new ArrayList<Recipe>();

    ArrayList<Ingredient> ingredients = createDummyIngredients();
    ArrayList<RecipeStep> recipeSteps = createDummyRecipeSteps();

    mRecipes.add(new Recipe("1", "Choclate Pudding", ingredients, recipeSteps));
    mRecipes.add(new Recipe("2", "Ice Cream Sandwich", ingredients, recipeSteps));
    RecipeAdapter adapter = new RecipeAdapter(this, mRecipes);

    RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
    mRecipeRecyclerView.setLayoutManager(mLayoutManager);
    mRecipeRecyclerView.setAdapter(adapter);

    // Create the RecipeLoader
    getLoaderManager().restartLoader(RECIPE_LOADER_ID, null, this);
  }

  private ArrayList<Ingredient> createDummyIngredients() {
    Ingredient ingredient1 = new Ingredient(1, "Dollop", "Chocolate");
    Ingredient ingredient2 = new Ingredient(2, "", "");
    ArrayList<Ingredient> ingredients = new ArrayList<>();
    ingredients.add(ingredient1);
    ingredients.add(ingredient2);
    return ingredients;
  }

  private ArrayList<RecipeStep> createDummyRecipeSteps() {
    RecipeStep recipeStep1 = new RecipeStep(0, "Put x in y",
        "Put x in y", "www.youtube.com", "www.google.com");
    RecipeStep recipeStep2 = new RecipeStep(1, "burn the food", "make sure to burn the food",
        "www.youtube.com", "www.google.com");
    ArrayList<RecipeStep> recipeSteps = new ArrayList<>();
    recipeSteps.add(recipeStep1);
    recipeSteps.add(recipeStep2);
    return recipeSteps;
  }

  private boolean checkInternetConnection() {
    // Check the status of the network connection
    ConnectivityManager connectivityManager =
        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo networkInfo;

    // Get details on the current active default data network
    if (connectivityManager != null) {
      networkInfo = connectivityManager.getActiveNetworkInfo();

      if (networkInfo != null && networkInfo.isConnected()) {
        Log.d(TAG, "checkInternetConnection: Connected");
        return true;
      }
    }
    Log.d(TAG, "checkInternetConnection: No internet connection!");
    return false;
  }

  /**
   *
   * Section for the three methods that interact with the RecipeLoader
   *
   * onCreateLoader
   * onLoadFinished
   * onLoadReset
   */

  @Override
  public Loader<ArrayList<Recipe>> onCreateLoader(int id, Bundle args) {
   // Build the Recipe url and begin a new RecipeLoader
    Log.d(TAG, "onCreateLoader: Recipes: started");

    return new RecipeLoader(MainActivity.this);
  }

  @Override
  public void onLoadFinished(Loader<ArrayList<Recipe>> loader, ArrayList<Recipe> data) {
    Log.d(TAG, "onLoadFinished: Recipes: finished");

    Toast.makeText(this, "Loader finished successfully", Toast.LENGTH_SHORT).show();
  }

  @Override
  public void onLoaderReset(Loader<ArrayList<Recipe>> loader) {
    Log.d(TAG, "onLoaderReset: Recipes: Reset called!");

  }
}
