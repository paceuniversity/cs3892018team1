package com.example.yevgeniyshatrovskiy.steepr.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yevgeniyshatrovskiy.steepr.Activities.MainActivity;
import com.example.yevgeniyshatrovskiy.steepr.Activities.Timer;
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

    Intent newIntent;

    public RecipeAdapter(Context context, List<Recipe> recipeList){
        this.recipe = recipeList;
        this.context = context;
    }


    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_layout, parent, false);
        final RecipeViewHolder viewHolder = new RecipeViewHolder(layout, recipe);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("CLICK TEST", recipe.get(viewHolder.getAdapterPosition()).getName());
                float timeToSteep = recipe.get(viewHolder.getAdapterPosition()).getSecondsToSteep();
                ((MainActivity)context).beginTimerActivity(timeToSteep);

            }
        });

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

    public void beginTimer(Intent intent){
        MainActivity activity = new MainActivity();
        activity.startActivity(intent);
    }


}
