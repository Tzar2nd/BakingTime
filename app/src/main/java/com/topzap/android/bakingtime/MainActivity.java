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

  private RecipeAdapter mRecipeAdapter;

  @BindView(R.id.rv_main_activity)
  RecyclerView mRecipeRecyclerView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);

    checkInternetConnection();

    ArrayList<Recipe> mRecipes = new ArrayList<Recipe>();

    mRecipeAdapter = new RecipeAdapter(this, mRecipes);
    RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
    mRecipeRecyclerView.setLayoutManager(mLayoutManager);
    mRecipeRecyclerView.setAdapter(mRecipeAdapter);

    // Create the RecipeLoader
    getLoaderManager().restartLoader(RECIPE_LOADER_ID, null, this);
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
   * onCreateLoader, onLoadFinishe and onLoaderReset methods that interact with the LoaderManager
   * and return a new array of recipes to the recyclerview adapter.
   *
   */

  @Override
  public Loader<ArrayList<Recipe>> onCreateLoader(int id, Bundle args) {
    Log.d(TAG, "onCreateLoader: Recipes: started");

    return new RecipeLoader(MainActivity.this);
  }

  @Override
  public void onLoadFinished(Loader<ArrayList<Recipe>> loader, ArrayList<Recipe> recipes) {
    Log.d(TAG, "onLoadFinished: Recipes: finished");

    mRecipeAdapter.clear();
    mRecipeAdapter.addAll(recipes);
    mRecipeAdapter.notifyDataSetChanged();
  }

  @Override
  public void onLoaderReset(Loader<ArrayList<Recipe>> loader) {
    Log.d(TAG, "onLoaderReset: Recipes: Reset called!");
    mRecipeRecyclerView.setAdapter(null);
  }
}
