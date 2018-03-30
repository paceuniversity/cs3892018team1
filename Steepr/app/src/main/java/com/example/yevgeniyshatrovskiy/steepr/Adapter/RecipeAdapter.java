package com.example.yevgeniyshatrovskiy.steepr.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yevgeniyshatrovskiy.steepr.Objects.Recipe;
import com.example.yevgeniyshatrovskiy.steepr.R;
import com.example.yevgeniyshatrovskiy.steepr.ViewHolders.RecipeViewHolder;

import java.util.List;

/**
 * Created by yevgeniy on 3/29/18.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeViewHolder>{
    private List<Recipe> recipe;
    protected Context context;

    public RecipeAdapter(Context context, List<Recipe> recipeList){
        this.recipe = recipeList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecipeViewHolder viewHolder = null;
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_layout, parent, false);
        viewHolder = new RecipeViewHolder(layout, recipe);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        holder.getrName().setText(recipe.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return this.recipe.size();
    }
}
