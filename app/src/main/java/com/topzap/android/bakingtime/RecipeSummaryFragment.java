package com.topzap.android.bakingtime;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.topzap.android.bakingtime.POJO.Recipe;
import java.util.List;

public class RecipeSummaryFragment extends Fragment {

  private static final String TAG = RecipeSummaryFragment.class.getName();
  OnRecipeStepSelectedListener mCallback;

  Recipe currentRecipe;
  List<String> recipeSteps;
  ArrayAdapter<String> adapter;

  public interface OnRecipeStepSelectedListener {
    public void onRecipeStepSelected(int position);
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);

    // This makes sure that the container activity has implemented
    // the callback interface. If not, it throws an exception
    try {
      mCallback = (OnRecipeStepSelectedListener) context;
    } catch (ClassCastException e) {
      throw new ClassCastException(context.toString()
          + " must implement OnHeadlineSelectedListener");
    }
  }

  public RecipeSummaryFragment() {
    // Required empty public constructor
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_recipe_summary, container, false);

    currentRecipe = getArguments().getParcelable("RECIPE");

    TextView ingredientsTextView = (TextView) rootView.findViewById(R.id.tv_ingredients_list);
    ingredientsTextView.setText(currentRecipe.getIngredientsString());

    final ListView stepsListView = (ListView) rootView.findViewById(R.id.lv_recipe_steps);

    recipeSteps = currentRecipe.getRecipeStepsList();

    if(adapter == null) {
      adapter = new ArrayAdapter<String>(
          getActivity(),
          android.R.layout.simple_list_item_1,
          recipeSteps);
    } else {
      adapter.clear();
      adapter.addAll(recipeSteps);
    }

    stepsListView.setAdapter(adapter);

    stepsListView.setOnItemClickListener(new OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mCallback.onRecipeStepSelected(position);
      }
    });

    return rootView;
  }


}
