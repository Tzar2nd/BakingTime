package com.topzap.android.bakingtime.utils;

import android.util.Log;
import com.topzap.android.bakingtime.POJO.Ingredient;
import com.topzap.android.bakingtime.POJO.Recipe;
import com.topzap.android.bakingtime.POJO.RecipeStep;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class NetworkUtils {

  private static final String TAG = NetworkUtils.class.getName();
  private static final String url = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
  private static final int READ_TIMEOUT = 10000;
  private static final int CONNECTION_TIMEOUT = 150000;
  private static final String RECIPE_CHAR_SET = "UTF-8";

  // Empty Constructor so that this class cannot be called directly, only its public methods
  private NetworkUtils() {
  }

  public static ArrayList<Recipe> getRecipeData() {

    String jsonResponse = getJsonResponse(url);
    ArrayList<Recipe> recipes = extractRecipeDataFromJSON(jsonResponse);
    return recipes;
  }

  private static String getJsonResponse(String stringUrl) {
    // Create URL object
    URL url = null;
    try {
      url = new URL(stringUrl);
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }

    String jsonResponse = null;

    try {
      jsonResponse = makeHttpRequest(url);
    } catch (IOException e) {
      Log.e(TAG, "Error making the HTTP request", e);
    }

    return jsonResponse;
  }

  private static String makeHttpRequest(URL url) throws IOException {

    String jsonResponseString = "";
    HttpURLConnection urlConnection = null;
    InputStream inputStream = null;

    // Create an HTTP connection, setting the timeouts and try to obtain a 200 OK connection
    try {
      urlConnection = (HttpURLConnection) url.openConnection();
      urlConnection.setReadTimeout(READ_TIMEOUT);
      urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
      urlConnection.setRequestMethod("GET");
      urlConnection.connect();

      if (urlConnection.getResponseCode() == 200) {
        Log.d(TAG, "Connection successful (200)");
        inputStream = urlConnection.getInputStream();
        jsonResponseString = readFromStreamJSON(
            inputStream);   // Convert the inputStreamJSON to String
      } else {
        Log.e(TAG, "Unable to connect, error response code: " + urlConnection.getResponseCode());
      }
    } catch (IOException e) {
      Log.e(TAG, "Problem receiving query due to connection failure", e);
    } finally {
      if (urlConnection != null) {
        urlConnection.disconnect();
      }
      if (inputStream != null) {
        inputStream.close();
      }
    }

    return jsonResponseString;
  }

  private static String readFromStreamJSON(InputStream inputStream) throws IOException {
    StringBuilder stringBuilder = new StringBuilder();
    if (inputStream != null) {
      InputStreamReader inputStreamReader =
          new InputStreamReader(inputStream, Charset.forName(RECIPE_CHAR_SET));

      // BufferedReader takes an inputstream in chunks
      BufferedReader reader = new BufferedReader(inputStreamReader);
      String line = reader.readLine();
      while (line != null) {
        stringBuilder.append(line);
        line = reader.readLine();
      }
    }
    return stringBuilder.toString();
  }

  /**
   *
   * @param recipeJSON
   * @return recipes ArrayList that contains arraylists of ingredients and recipe steps.
   */
  private static ArrayList<Recipe> extractRecipeDataFromJSON(String recipeJSON) {
    ArrayList<Recipe> recipes = new ArrayList<>();

    try {
      JSONArray jsonRecipes = new JSONArray(recipeJSON);

      for (int i = 0; i < jsonRecipes.length(); i++) {
        JSONObject jsonRecipe = jsonRecipes.getJSONObject(i);

        String id = jsonRecipe.getString("id");
        String name = jsonRecipe.getString("name");

        // Extract the ingredients array
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        JSONArray jsonIngredients = jsonRecipe.getJSONArray("ingredients");

        for (int j = 0; j < jsonIngredients.length(); j++) {
          JSONObject jsonIngredient = jsonIngredients.getJSONObject(j);

          double quantity = Double.parseDouble(jsonIngredient.getString("quantity"));
          String measure = jsonIngredient.getString("measure");
          String ingredient = jsonIngredient.getString("ingredient");
          ingredients.add(new Ingredient(quantity, measure, ingredient));

          Log.d(TAG, "extractRecipeDataFromJSON: " + ingredient + ", " + quantity + ", " + measure);
        }

        // Extract the recipe steps array
        ArrayList<RecipeStep> recipeSteps = new ArrayList<>();
        JSONArray jsonRecipeSteps = jsonRecipe.getJSONArray("steps");

        for (int k = 0; k < jsonRecipeSteps.length(); k++) {
          JSONObject jsonRecipeStep = jsonRecipeSteps.getJSONObject(k);

          int stepId = Integer.parseInt(jsonRecipeStep.getString("id"));
          String shortDescription = jsonRecipeStep.getString("shortDescription");
          String description = jsonRecipeStep.getString("description");
          String videoURL = jsonRecipeStep.getString("videoURL");
          String thumbnailURL = jsonRecipeStep.getString("thumbnailURL");

          recipeSteps
              .add(new RecipeStep(stepId, shortDescription, description, videoURL, thumbnailURL));
          Log.d(TAG,
              "extractRecipeDataFromJSON: " + stepId + ", " + shortDescription + ", " + description
                  + videoURL + ", " + thumbnailURL);
        }

        recipes.add(new Recipe(id, name, ingredients, recipeSteps));
      }

      // Log what is in the recipes
      for (Recipe recipe : recipes) {
        Log.d(TAG, "extractRecipeDataFromJSON: " + recipe.getRecipe());
      }

    } catch (JSONException e) {
      Log.e(TAG, "extractRecipeDataFromJSON: Problem parsing the Recipe JSON response ");
    }

    return recipes;
  }

}
