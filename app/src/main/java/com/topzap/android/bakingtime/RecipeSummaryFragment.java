package com.topzap.android.bakingtime;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.topzap.android.bakingtime.POJO.Recipe;

public class RecipeSummaryFragment extends Fragment {

  Recipe currentRecipe;

  public RecipeSummaryFragment() {
    // Required empty public constructor
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_recipe_summary, container, false);

    currentRecipe = getArguments().getParcelable("RECIPE");

    TextView ingredientsTextView = (TextView) rootView.findViewById(R.id.tv_ingredients_list);
    ingredientsTextView.setText(currentRecipe.getIngredientsString());

    return rootView;
  }

}
