package com.topzap.android.bakingtime.data;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.topzap.android.bakingtime.R;
import java.util.ArrayList;
import java.util.List;

public class RecipeStepAdapter extends
    RecyclerView.Adapter<RecipeStepAdapter.RecipeStepViewHolder> {

  private List<String> mRecipeSteps = new ArrayList<>();

  private RecipeStepOnClickListener mListener;

  /**
   * Interface to receive onClick messages
   */
  public interface RecipeStepOnClickListener {
    void onClick(int position);
  }

  public RecipeStepAdapter(RecipeStepOnClickListener listener) {
    mListener = listener;
  }

  @Override
  public RecipeStepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View itemView = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.recipe_step_row, parent, false);

    return new RecipeStepViewHolder(itemView);
  }

  @Override
  public void onBindViewHolder(RecipeStepViewHolder holder, int position) {
    holder.mTextViewStep.setText(mRecipeSteps.get(position));
  }

  public void addAll(List<String> recipeSteps) {
    mRecipeSteps.clear();
    mRecipeSteps = recipeSteps;
    notifyDataSetChanged();
  }

  @Override
  public int getItemCount() {
    return mRecipeSteps.size();
  }

  /**
   * Custom RecyclerView ViewHolder with click event that calls back via interface to fragment
   * which then calls back to the host Activity to make decision on which fragments to swap
   * depending on single or two pane layout.
   */
  public class RecipeStepViewHolder extends RecyclerView.ViewHolder implements
      View.OnClickListener {

    @BindView(R.id.tv_recipe_step_row) TextView mTextViewStep;

    RecipeStepViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
      itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
      mListener.onClick(getAdapterPosition());
    }
  }
}
