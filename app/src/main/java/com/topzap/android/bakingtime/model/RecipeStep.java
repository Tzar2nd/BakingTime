package com.topzap.android.bakingtime.model;

import android.os.Parcel;
import android.os.Parcelable;

public class RecipeStep implements Parcelable {
  private int id;
  private String shortDescription;
  private String description;
  private String videoURL;
  private String thumbnailURL;

  public RecipeStep(int id, String shortDescription, String description, String videoURL,
      String thumbnailURL) {
    this.id = id;
    this.shortDescription = shortDescription;
    this.description = description;
    this.videoURL = videoURL;
    this.thumbnailURL = thumbnailURL;
  }

  protected RecipeStep(Parcel in) {
    id = in.readInt();
    shortDescription = in.readString();
    description = in.readString();
    videoURL = in.readString();
    thumbnailURL = in.readString();
  }

  public static final Creator<RecipeStep> CREATOR = new Creator<RecipeStep>() {
    @Override
    public RecipeStep createFromParcel(Parcel in) {
      return new RecipeStep(in);
    }

    @Override
    public RecipeStep[] newArray(int size) {
      return new RecipeStep[size];
    }
  };

  public int getId() {
    return id;
  }

  public String getShortDescription() {
    return shortDescription;
  }

  public String getDescription() {
    return description;
  }

  public String getVideoURL() {
    return videoURL;
  }

  public String getThumbnailURL() {
    return thumbnailURL;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(id);
    dest.writeString(shortDescription);
    dest.writeString(description);
    dest.writeString(videoURL);
    dest.writeString(thumbnailURL);
  }
}
