package com.topzap.android.bakingtime.POJO;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

public class Recipe implements Parcelable {

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
    in.readList(mIngredients, null);
    in.readList(mRecipeSteps, null);
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

  public String getRecipe() {
    return id + ", " + name;
  }


}
