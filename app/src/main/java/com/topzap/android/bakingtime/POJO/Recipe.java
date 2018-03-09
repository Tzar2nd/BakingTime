package com.topzap.android.bakingtime.POJO;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.List;

public class Recipe implements Parcelable {

  private static final String TAG = Recipe.class.getName();

  private String id;
  private String name;
  private ArrayList<Ingredient> mIngredients = new ArrayList<>();
  private ArrayList<RecipeStep> mRecipeSteps = new ArrayList<>();

  public Recipe(String id, String name, ArrayList<Ingredient> ingredients,
      ArrayList<RecipeStep> recipeSteps) {
    this.id = id;
    this.name = name;
    this.mIngredients = ingredients;
    this.mRecipeSteps = recipeSteps;
  }

  protected Recipe(Parcel in) {
    id = in.readString();
    name = in.readString();

    mIngredients = in.readArrayList(Ingredient.class.getClassLoader());
    mRecipeSteps = in.readArrayList(RecipeStep.class.getClassLoader());
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(id);
    dest.writeString(name);
    dest.writeList(mIngredients);
    dest.writeList(mRecipeSteps);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
    @Override
    public Recipe createFromParcel(Parcel in) {
      return new Recipe(in);
    }

    @Override
    public Recipe[] newArray(int size) {
      return new Recipe[size];
    }
  };

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public ArrayList<Ingredient> getIngredients() {
    return mIngredients;
  }

  public ArrayList<RecipeStep> getRecipeSteps() {
    return mRecipeSteps;
  }

  public RecipeStep getSelectedRecipeStep(int position) {
    for(RecipeStep recipeStep : mRecipeSteps) {
      if (recipeStep.getId() == position) {
        return recipeStep;
      }
    }
    return null;
  }

  public String getRecipe() {
    return id + ", " + name;
  }

  public String getIngredientsString() {
    StringBuilder ingredientsString = new StringBuilder();

    for (Ingredient ingredient : mIngredients) {
      ingredientsString
          .append("\n")
          .append(ingredient.getIngredient().substring(0, 1).toUpperCase())
          .append(ingredient.getIngredient().substring(1))
          .append(" - ")
          .append(ingredient.getQuantity())
          .append(" ")
          .append(ingredient.getMeasure());
    }

    return ingredientsString.toString();
  }

  public List<String> getRecipeStepsList() {
    List<String> recipeSteps = new ArrayList<String>();

    for(RecipeStep recipeStep : mRecipeSteps) {
      recipeSteps.add(recipeStep.getId() + ": " + recipeStep.getShortDescription());
    }

    return recipeSteps;
  }

}
