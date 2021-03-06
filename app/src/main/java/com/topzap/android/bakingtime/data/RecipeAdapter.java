package com.topzap.android.bakingtime.data;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.topzap.android.bakingtime.utils.Config;
import com.topzap.android.bakingtime.widget.IngredientWidgetProvider;
import com.topzap.android.bakingtime.widget.IngredientWidgetService;
import com.topzap.android.bakingtime.model.Recipe;
import com.topzap.android.bakingtime.R;
import com.topzap.android.bakingtime.activities.RecipeActivity;
import java.util.ArrayList;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

  private Context mContext;
  private ArrayList<Recipe> mRecipes = new ArrayList<>();
  private static final String mIntentFlag = "RECIPE";
  private Recipe mCurrentRecipe;

  public RecipeAdapter(Context context, ArrayList<Recipe> recipes) {
    mContext = context;
    mRecipes = recipes;
  }

  @Override
  public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View itemView = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.recipe_card, parent, false);
    return new RecipeViewHolder(itemView);
  }

  @Override
  public void onBindViewHolder(RecipeViewHolder holder, int position) {
    mCurrentRecipe = mRecipes.get(position);

    String recipeName = mCurrentRecipe.getName();

    holder.mRecipeName.setText(recipeName);
    holder.mRecipeName.append(" - Servings: " + mCurrentRecipe.getServings());

    // Use an image id if it is available, or use a default image if it is not
    if(!mCurrentRecipe.getRecipeImage().equals("")) {
      Picasso.with(mContext)
          .load(mCurrentRecipe.getRecipeImage())
          .placeholder(getImagePlaceholderId(recipeName))
          .error(getImagePlaceholderId(recipeName))
          .into(holder.mRecipeImage);
    } else {
      holder.mRecipeImage.setImageResource(getImagePlaceholderId(recipeName));
    }

  }

  public int getImagePlaceholderId(String recipeName) {
    int imageResourceId;

    if (recipeName.contains(mContext.getString(R.string.recipe_pie))) {
      imageResourceId = R.drawable.nutella_pie;
    } else if (recipeName.contains(mContext.getString(R.string.recipe_brownie))) {
      imageResourceId = R.drawable.brownies;
    } else if (recipeName.contains(mContext.getString(R.string.recipe_cheesecake))) {
      imageResourceId = R.drawable.cheesecake;
    } else if (recipeName.contains(mContext.getString(R.string.recipe_cake))) {
      imageResourceId = R.drawable.yellow_cake;
    } else { // default if recipe name does not match anything else
      imageResourceId = R.mipmap.ic_launcher;
    }

    return imageResourceId;
  }

  public void addAll(ArrayList<Recipe> recipes) {
    mRecipes = recipes;
  }

  public void clear() {
    int size = mRecipes.size();
    mRecipes.clear();
    notifyItemRangeRemoved(0, size);
  }

  @Override
  public int getItemCount() {
    return mRecipes.size();
  }

  public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    @BindView(R.id.iv_recipeImage) ImageView mRecipeImage;
    @BindView(R.id.tv_recipeName) TextView mRecipeName;

    public RecipeViewHolder(View itemView) {
      super(itemView);
      itemView.setOnClickListener(this);
      ButterKnife.bind(this, itemView);
    }

    @Override
    public void onClick(View v) {
      mCurrentRecipe = mRecipes.get(getAdapterPosition());

      updateAllWidgets();

      // Start the RecipeActivity class, sending the current recipe via an intent
      Intent intent = new Intent(mContext, RecipeActivity.class);
      intent.putExtra(mIntentFlag, mCurrentRecipe);
      mContext.startActivity(intent);
    }

    /**
     * Creates a serviceIntent to notify the service, and triggers a refresh of all widgets
     * listviews and renames the recipe header on all widgets.
     */
    private void updateAllWidgets() {

      // Create an intent to broadcast to onUpdate in IngredientWidgetProvider
      Intent serviceIntent = new Intent(mContext, IngredientWidgetService.class);
      serviceIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

      // Get an instance of appWidgetManager and get all remoteViews and thisWidget
      AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(mContext);
      RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.ingredients_widget);
      ComponentName thisWidget = new ComponentName(mContext, IngredientWidgetProvider.class);

      // Set the recipe name
      remoteViews.setTextViewText(R.id.widget_header, mCurrentRecipe.getName());

      // Get app widget Ids
      int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
      serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

      // Commit the recipe name and notify the list view to update
      appWidgetManager.updateAppWidget(thisWidget, remoteViews);
      appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_listview);

      // Get shared preferences and use Gson to serialize the arraylist of ingredients
      SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext.getApplicationContext());
      Editor prefsEditor = preferences.edit();
      Gson gson = new Gson();
      String json = gson.toJson(mCurrentRecipe.getIngredients());
      prefsEditor.putString(Config.KEY_INGREDIENTS, json);
      prefsEditor.putString(Config.KEY_CURRENT_RECIPE_NAME, mCurrentRecipe.getName());
      prefsEditor.apply();

    }
  }

}
