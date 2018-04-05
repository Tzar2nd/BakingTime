package com.topzap.android.bakingtime.widget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.topzap.android.bakingtime.R;
import com.topzap.android.bakingtime.model.Ingredient;
import com.topzap.android.bakingtime.utils.Config;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class IngredientWidgetService extends RemoteViewsService {

  @Override
  public RemoteViewsFactory onGetViewFactory(Intent intent) {
    return new ListRemoteViewsFactory(this.getApplicationContext());
  }
}

class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

  private static final String TAG = "ListRemoteViewsFactory";

  private ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
  private String mRecipeName;
  private Context mContext;

  public ListRemoteViewsFactory(Context applicationContext) {
    mContext = applicationContext;
  }

  @Override
  public void onCreate() {
    initIngredients();
  }

  @Override
  public void onDataSetChanged() {
    // Called on start and when notifyAppWidgetDataViewDataChanged is called
    initIngredients();
  }

  // when onCreate and onDataSetChanged are called, de-serialize the arraylist of ingredients
  // from the shared preferences using Gson and populate the arraylist.
  private void initIngredients() {
    Log.d(TAG, "initIngredients: De-serialising Ingredients ArrayList");
    ingredients.clear();

    SharedPreferences prefs = PreferenceManager
        .getDefaultSharedPreferences(mContext.getApplicationContext());

    // Get the recipe title first
    mRecipeName = prefs.getString(Config.KEY_CURRENT_RECIPE_NAME, "");
    Log.d(TAG, "initIngredients: " + mRecipeName);

    Gson gson = new Gson();
    String json = prefs.getString(Config.KEY_INGREDIENTS, "");
    Type type = new TypeToken<ArrayList<Ingredient>>() {
    }.getType();
    ingredients = gson.fromJson(json, type);

  }

  @Override
  public void onDestroy() {
  }

  @Override
  public int getCount() {
    return ingredients.size();
  }

  @Override
  public RemoteViews getViewAt(int position) {
    Ingredient currentIngredient = ingredients.get(position);

    RemoteViews row = new RemoteViews(mContext.getPackageName(), android.R.layout.simple_list_item_1);
    row.setTextViewText(android.R.id.text1, currentIngredient.getIngredient());

    return row;
  }

  @Override
  public RemoteViews getLoadingView() {
    return null;
  }

  @Override
  public int getViewTypeCount() {
    return 1;
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public boolean hasStableIds() {
    return false;
  }
}