package com.topzap.android.bakingtime.POJO;

import java.util.ArrayList;

public class Recipe {
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

  public String getRecipe() {
    return id + ", " + name;

  }

}
