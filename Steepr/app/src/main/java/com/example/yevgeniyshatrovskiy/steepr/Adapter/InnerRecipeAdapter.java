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
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yevgeniyshatrovskiy.steepr.Activities.MainActivity;
import com.example.yevgeniyshatrovskiy.steepr.Objects.Recipe;
import com.example.yevgeniyshatrovskiy.steepr.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class InnerRecipeAdapter extends RecyclerView.Adapter<InnerRecipeAdapter.InnerRecipeViewHolder>{

    public static List<Recipe> recipe;
    protected Context context;
    public boolean english;
    private ArrayList<String> favs;
    private InnerRecipeAdapter.InnerRecipeViewHolder viewHolder;
    public Map<Integer,Recipe> test = new HashMap<>();
    public View layout;
    public boolean favorite;


    public static class InnerRecipeViewHolder extends RecyclerView.ViewHolder{


        private TextView rName;
        private TextView rTime;
        private ImageView rHeart;
        public RecyclerView innerRecyclerView;


        public InnerRecipeViewHolder(View itemView){
            super(itemView);
            rName = itemView.findViewById(R.id.recipeName);
            rTime = itemView.findViewById(R.id.recipeTime);
            rHeart = itemView.findViewById(R.id.heart);
            innerRecyclerView = itemView.findViewById(R.id.innerRecipeRecycler);

        }

        public ImageView getrHeart() {
            return rHeart;
        }

        public void setrHeart(ImageView rHeart) {
            this.rHeart = rHeart;
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

    public InnerRecipeAdapter(Context context, ArrayList<Recipe> recipeList, boolean english, ArrayList<String> favorites){
        this.recipe = recipeList;
        this.context = context;
        this.english = english;
        this.favs = favorites;
    }

    @NonNull
    @Override
    public InnerRecipeAdapter.InnerRecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_layout, parent, false);
        viewHolder = new InnerRecipeAdapter.InnerRecipeViewHolder(layout);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.v("CLICK TEST", recipe.get(viewHolder.getAdapterPosition()).getName());


            }
        });

        return viewHolder;
    }



    @Override
    public void onBindViewHolder(@NonNull final InnerRecipeViewHolder holder, final int position) {
        favorite = false;
        Log.v("TESTER", recipe.get(position).getName());

        if(english)
            holder.setrName(recipe.get(position).getName());
        else
            holder.setrName(recipe.get(position).getChineseName());


        final int pos = position;
        Log.v("TEST1", recipe.get(position).getName());
        Log.v("TEST2", pos + " ");
        test.put(pos, recipe.get(position));

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favorite = false;
                if(favs.contains(test.get(pos).getName()))
                    favorite = true;
                Log.v("VAF", test.get(pos).getName());
                Log.v("VAF", test.get(pos).getCategory());
                Log.v("VAF", favorite + " :");
//                float timeToSteep = test.get(pos).getSecondsToSteep();
                ((MainActivity)context).beginTimerActivity(test.get(pos), v, favorite);
            }
        });

        setText(holder,position);
        holder.getrTime().setBackgroundColor(Color.parseColor(recipe.get(position).getTextColor()));
        holder.getrTime().setTextColor(Color.parseColor(recipe.get(position).getBackGroundColor()));

        holder.getrName().setBackgroundColor(Color.parseColor(recipe.get(position).getTextColor()));
        holder.getrName().setTextColor(Color.parseColor(recipe.get(position).getBackGroundColor()));

        holder.getrHeart().setBackgroundColor(Color.parseColor(recipe.get(position).getTextColor()));

        if(favs.contains(recipe.get(position).getName())){
            favorite = true;
            Log.v("VAFX++", recipe.get(position).getName());
            holder.getrHeart().setImageResource(R.drawable.heart);
        }


    }

    @Override
    public int getItemCount() {
        return this.recipe.size();

    }

    public void remove(Recipe rec){
        recipe.remove(rec);
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
