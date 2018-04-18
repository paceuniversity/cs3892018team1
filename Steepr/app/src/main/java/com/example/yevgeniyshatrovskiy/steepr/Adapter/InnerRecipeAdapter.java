package com.example.yevgeniyshatrovskiy.steepr.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.yevgeniyshatrovskiy.steepr.Activities.MainActivity;
import com.example.yevgeniyshatrovskiy.steepr.Objects.Recipe;
import com.example.yevgeniyshatrovskiy.steepr.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class InnerRecipeAdapter extends RecyclerView.Adapter<InnerRecipeAdapter.InnerRecipeViewHolder>{

    private List<Recipe> recipe;
    protected Context context;
    public boolean english;


    public static class InnerRecipeViewHolder extends RecyclerView.ViewHolder{


        private TextView rName;
        private TextView rTime;
        public RecyclerView innerRecyclerView;


        public InnerRecipeViewHolder(View itemView){
            super(itemView);
            rName = itemView.findViewById(R.id.recipeName);
            rTime = itemView.findViewById(R.id.recipeTime);
            innerRecyclerView = itemView.findViewById(R.id.innerRecipeRecycler);
            Log.v("RV", "rNAME");
        }

        public TextView getrTime() {
            return rTime;
        }

        public void setrTime(TextView rTime) {
            this.rTime = rTime;
        }

        public TextView getrName() {
            return rName;
        }

        public void setrName(String name) {
            rName.setText(name);
            Log.v("RV", name);
        }
    }

    public InnerRecipeAdapter(Context context, ArrayList<Recipe> recipeList, boolean english){
        this.recipe = recipeList;
        this.context = context;
        this.english = english;
    }


    @NonNull
    @Override
    public InnerRecipeAdapter.InnerRecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_layout, parent, false);
        final InnerRecipeAdapter.InnerRecipeViewHolder viewHolder = new InnerRecipeAdapter.InnerRecipeViewHolder(layout);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("CLICK TEST", recipe.get(viewHolder.getAdapterPosition()).getName());
                float timeToSteep = recipe.get(viewHolder.getAdapterPosition()).getSecondsToSteep();
                ((MainActivity)context).beginTimerActivity(recipe.get(viewHolder.getAdapterPosition()), v);
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull InnerRecipeViewHolder holder, int position) {
        Log.v("TESTER", recipe.get(position).getName());
        if(english)
            holder.setrName(recipe.get(position).getName());
        else
            holder.setrName(recipe.get(position).getChineseName());


        setText(holder,position);
        holder.getrTime().setBackgroundColor(Color.parseColor(recipe.get(position).getTextColor()));
        holder.getrTime().setTextColor(Color.parseColor(recipe.get(position).getBackGroundColor()));

        holder.getrName().setBackgroundColor(Color.parseColor(recipe.get(position).getTextColor()));
        holder.getrName().setTextColor(Color.parseColor(recipe.get(position).getBackGroundColor()));
    }

    @Override
    public int getItemCount() {
        return this.recipe.size();

    }

    public void setText(InnerRecipeViewHolder holder, int position){

//        long msteepTimeInMiliseconds = (long)recipe.get(position).getSecondsToSteep();
        long msteepTimeInMiliseconds = (long)recipe.get(position).getSecondsToSteep() * 1000;
        int minutes = (int) (msteepTimeInMiliseconds / 1000) / 60;
        int seconds = (int) (msteepTimeInMiliseconds / 1000) % 60;
        Log.v("UPDATE", ":" + msteepTimeInMiliseconds);
        String timeLeftFormatted = String.format(Locale.getDefault(),"%02d:%02d", minutes, seconds);
//        holder.setrTime(timeLeftFormatted);
        holder.getrTime().setText(timeLeftFormatted);
//        mCountDownText.setText(timeLeftFormatted);

    }

}
