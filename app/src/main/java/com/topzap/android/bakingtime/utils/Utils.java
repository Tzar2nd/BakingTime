package com.topzap.android.bakingtime.utils;

import android.support.annotation.NonNull;
import android.view.View;
import butterknife.ButterKnife;

public class Utils {

  private void Utils() {}

  /**
   * Butterknife setter interface to change view visibility
   */
  public static final ButterKnife.Setter<View, Integer> VISIBILITY = new
      ButterKnife.Setter<View,Integer>() {
        @Override
        public void set(@NonNull View view, Integer value, int index) {
          view.setVisibility(value);
        }
      };

  public static boolean isEmptyString(String stringToCheck) {
    return (stringToCheck == null || stringToCheck.trim().length() == 0);
  }
}
