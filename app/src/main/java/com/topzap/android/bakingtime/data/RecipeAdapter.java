package com.topzap.android.bakingtime.data;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.topzap.android.bakingtime.POJO.Recipe;
import com.topzap.android.bakingtime.R;
import com.topzap.android.bakingtime.RecipeActivity;
import java.util.ArrayList;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

  private Context mContext;
  private ArrayList<Recipe> mRecipes = new ArrayList<>();
  private static final String mIntentFlag = "RECIPE";

  public RecipeAdapter(Context context, ArrayList<Recipe> recipes) {
    mContext = context;
    mRecipes = recipes;
  }

  @Override
  public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View itemView = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.recipe_card, parent, false);
    return new RecipeViewHolder(itemView);
  }

  @Override
  public void onBindViewHolder(RecipeViewHolder holder, int position) {
    Recipe currentRecipe = mRecipes.get(position);

    holder.mRecipeName.setText(currentRecipe.getName());
  }

  public void addAll(ArrayList<Recipe> recipes) {
    mRecipes = recipes;
  }

  public void clear() {
    int size = mRecipes.size();
    mRecipes.clear();
    notifyItemRangeRemoved(0, size);
  }

  @Override
  public int getItemCount() {
    return mRecipes.size();
  }

  public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    @BindView(R.id.iv_recipeImage) ImageView mRecipeImage;
    @BindView(R.id.tv_recipeName) TextView mRecipeName;

    public RecipeViewHolder(View itemView) {
      super(itemView);
      itemView.setOnClickListener(this);
      ButterKnife.bind(this, itemView);
    }

    @Override
    public void onClick(View v) {
      Recipe currentRecipe = mRecipes.get(getAdapterPosition());

      Intent intent = new Intent(mContext, RecipeActivity.class);

      intent.putExtra(mIntentFlag, currentRecipe);

      mContext.startActivity(intent);
    }
  }

}
