package com.topzap.android.bakingtime.fragments;

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
import butterknife.BindView;
import butterknife.ButterKnife;
import com.topzap.android.bakingtime.R;
import com.topzap.android.bakingtime.model.Recipe;
import com.topzap.android.bakingtime.utils.Config;
import java.util.List;

public class RecipeSummaryFragment extends Fragment {

  private static final String TAG = RecipeSummaryFragment.class.getName();
  OnRecipeStepSelectedListener mCallback;

  @BindView(R.id.tv_ingredients_list) TextView ingredientsTextView;
  @BindView(R.id.lv_recipe_steps) ListView stepsListView;

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
    ButterKnife.bind(this, rootView);

    currentRecipe = getArguments().getParcelable(Config.KEY_RECIPE);
    ingredientsTextView.setText(currentRecipe.getIngredientsString());
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
        view.getFocusables(position);
        view.setSelected(true);
        mCallback.onRecipeStepSelected(position);
      }
    });

    return rootView;
  }


}
