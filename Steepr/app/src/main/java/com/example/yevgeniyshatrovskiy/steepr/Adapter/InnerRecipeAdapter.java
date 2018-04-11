package com.example.yevgeniyshatrovskiy.steepr.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yevgeniyshatrovskiy.steepr.Activities.MainActivity;
import com.example.yevgeniyshatrovskiy.steepr.Objects.Recipe;
import com.example.yevgeniyshatrovskiy.steepr.R;

import java.util.ArrayList;
import java.util.List;

public class InnerRecipeAdapter extends RecyclerView.Adapter<InnerRecipeAdapter.InnerRecipeViewHolder>{

    private List<Recipe> recipe;
    protected Context context;
    private LinearLayoutManager lln;
    private Recipe rec;
    private String nombre;



    Intent newIntent;

    public static class InnerRecipeViewHolder extends RecyclerView.ViewHolder{


        private TextView rName;
        public RecyclerView innerRecyclerView;

        public InnerRecipeViewHolder(View itemView, final List<Recipe> recipes){
            super(itemView);
            rName = itemView.findViewById(R.id.recipeName);
            innerRecyclerView = itemView.findViewById(R.id.innerRecipeRecycler);
            Log.v("RV", "rNAME");
        }

        public TextView getrName() {
            return rName;
        }

        public void setrName(String name) {
            rName.setText(name);
            Log.v("RV", name);
        }
    }

    public InnerRecipeAdapter(Context context, ArrayList<Recipe> recipeList){
        this.recipe = recipeList;
        this.context = context;
    }


    @NonNull
    @Override
    public InnerRecipeAdapter.InnerRecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_layout, parent, false);
        final InnerRecipeAdapter.InnerRecipeViewHolder viewHolder = new InnerRecipeAdapter.InnerRecipeViewHolder(layout, recipe);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("CLICK TEST", recipe.get(viewHolder.getAdapterPosition()).getName());
                float timeToSteep = recipe.get(viewHolder.getAdapterPosition()).getSecondsToSteep();
                ((MainActivity)context).beginTimerActivity(timeToSteep, v);

            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull InnerRecipeViewHolder holder, int position) {
        Log.v(holder.getrName()+" TESTER", recipe.get(position).getName());
        holder.getrName().setText(recipe.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return this.recipe.size();

    }

}
