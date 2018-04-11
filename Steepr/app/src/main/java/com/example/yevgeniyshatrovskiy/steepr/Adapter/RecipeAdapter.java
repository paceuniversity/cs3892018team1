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

/**
 * Created by yevgeniy on 3/29/18.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>{

    private List<Recipe> recipeCatlist;
    private List<Recipe> recipelist;
    private Recipe recipE;
    protected Context context;
    private LinearLayoutManager lln;
    public int mExpandedPosition = -1;
    public int localIndex = 0;
    public ArrayList<Recipe> refinedList = new ArrayList<>();

    Intent newIntent;

    public static class RecipeViewHolder extends RecyclerView.ViewHolder{


        private TextView rName;
        public RecyclerView innerRecyclerView;

        public RecipeViewHolder(View itemView, final List<Recipe> recipes){
            super(itemView);
            rName = itemView.findViewById(R.id.recipeName);
            innerRecyclerView = itemView.findViewById(R.id.innerRecipeRecycler);
            innerRecyclerView.setHasFixedSize(true);
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

    public RecipeAdapter(Context context, List<Recipe> recipeList, List<Recipe> recipeCategoryList, Recipe rec){
        this.recipeCatlist = recipeList;
        this.recipelist = recipeCategoryList;
        this.context = context;
        this.recipE = rec;
    }

    public RecipeAdapter(Context context, List<Recipe> recipeList, List<Recipe> recipeCategoryList){
        this.recipeCatlist = recipeCategoryList;
        this.recipelist = recipeList;
        this.context = context;
    }

    public RecipeAdapter(Context context,
                         List<Recipe> recipeList,
                         ArrayList<Recipe> extra,
                         List<Recipe> recipeCategoryList){
        this.recipeCatlist = recipeCategoryList;
        this.refinedList = extra;
        this.recipelist = recipeList;
        this.context = context;
    }

    public RecipeAdapter(Context context, List<Recipe> recipeList){
        this.recipeCatlist = recipeList;
        this.context = context;
    }


    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_layout, parent, false);
        final RecipeViewHolder viewHolder = new RecipeViewHolder(layout, recipelist);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecipeViewHolder holder, final int position) {
        lln = new LinearLayoutManager(context);
        InnerRecipeAdapter innerRecipeAdapter;
        holder.innerRecyclerView.setLayoutManager(lln);
            Log.v("MOD2", "true");
            Log.v(position + "", "CONTEXT");
            if(position == 0){
                innerRecipeAdapter = new InnerRecipeAdapter(context, recipelist);
                holder.innerRecyclerView.setAdapter(innerRecipeAdapter);
            }else{
                innerRecipeAdapter = new InnerRecipeAdapter(context, refinedList);
                holder.innerRecyclerView.setAdapter(innerRecipeAdapter);
            }



//

        holder.getrName().setText(recipeCatlist.get(position).getName());

        final boolean isExpanded = position==mExpandedPosition;
        holder.innerRecyclerView.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        holder.itemView.setActivated(isExpanded);
        localIndex++;
        Log.v(localIndex+"", "Local Index Count");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mExpandedPosition = isExpanded ? -1:position;
                notifyItemChanged(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.recipeCatlist.size();
    }

    public void beginTimer(Intent intent){
        MainActivity activity = new MainActivity();
        activity.startActivity(intent);
    }


}
