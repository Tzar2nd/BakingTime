package com.topzap.android.bakingtime.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import com.topzap.android.bakingtime.R;
import com.topzap.android.bakingtime.widget.IngredientWidgetService;

public class IngredientWidgetProvider extends AppWidgetProvider {

  // onUpdate is called in response to ACTION_APPWIDGET_UPDATE and ACTION_APPWIDGET_RESTORED broadcasts
  // and provides RemoteViews for a set of AppWidgets.
  @Override
  public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
    // There may be multiple widgets active, so update all of them in a loop
    for (int appWidgetId : appWidgetIds) {
      // Create an intent that calls the service that gets the ingredients ArrayList from SharedPreferences
      Intent serviceIntent = new Intent(context, IngredientWidgetService.class);

      // Add the widget IDs as an extra so the service knows which ones to update
      serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

      // RemoteViews are a view hierarchy that can be displayed in another process. The hierarchy is
      // inflated from a layout resource file (in this case the ingredients_widget listview file) and
      // the class provides basic operations to modifying the content of the inflated hierarchy.
      RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget);

      // Attach the serviceIntent as the remote adapter to the listview
      remoteView.setRemoteAdapter(R.id.widget_listview, serviceIntent);

      // Update the current widget (using appWidgetId) to the current remoteView
      appWidgetManager.updateAppWidget(appWidgetId, remoteView);
    }
  }

  @Override
  public void onDeleted(Context context, int[] appWidgetIds) {
    // When the user deletes the widget, delete the preference associated with it.
  }

  @Override
  public void onEnabled(Context context) {
    // Enter relevant functionality for when the first widget is created
  }

  @Override
  public void onDisabled(Context context) {
    // Enter relevant functionality for when the last widget is disabled
  }
}

