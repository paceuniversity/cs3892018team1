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
import android.widget.TextView;
import com.example.yevgeniyshatrovskiy.steepr.Activities.MainActivity;
import com.example.yevgeniyshatrovskiy.steepr.Objects.Recipe;
import com.example.yevgeniyshatrovskiy.steepr.Objects.TeaCategory;
import com.example.yevgeniyshatrovskiy.steepr.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by yevgeniy on 3/29/18.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>{

    private List<Recipe> recipeCatlist;
    private List<Recipe> recipelist;
    private ArrayList<String> strRecipelist;
    private ArrayList<TeaCategory> teaCategories;
    private Recipe recipE;
    protected Context context;
    private LinearLayoutManager lln;
    public int mExpandedPosition = -1;
    public int localIndex = 0;
    public ArrayList<Recipe> refinedList = new ArrayList<>();

    public static class RecipeViewHolder extends RecyclerView.ViewHolder{


        private TextView rName;
        private Button rButton;
        public RecyclerView innerRecyclerView;

        public RecipeViewHolder(View itemView, final ArrayList<String> recipes){
            super(itemView);
//            rName = itemView.findViewById(R.id.recipeName);
            rButton = itemView.findViewById(R.id.openButton);
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

        public Button getrButton() {
            return rButton;
        }

        public void setrButton(Button rButton) {
            this.rButton = rButton;
        }
    }


    public RecipeAdapter(Context context, ArrayList<String> recipeList, ArrayList<TeaCategory> teaCategories){
        this.context = context;
        this.strRecipelist = recipeList;
        this.teaCategories = teaCategories;
    }


    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.tea_category_layout, parent, false);
        final RecipeViewHolder viewHolder = new RecipeViewHolder(layout, strRecipelist);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecipeViewHolder holder, final int position) {
        lln = new LinearLayoutManager(context);
        InnerRecipeAdapter innerRecipeAdapter;
        holder.innerRecyclerView.setLayoutManager(lln);
            Log.v("MOD2", "true");
            Log.v(position + "", "CONTEXT");
            innerRecipeAdapter = new InnerRecipeAdapter(context, teaCategories.get(position).getRecipes());
            holder.innerRecyclerView.setAdapter(innerRecipeAdapter);
        holder.getrButton().setText(strRecipelist.get(position));
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        holder.getrButton().setBackgroundColor(color);

        final boolean isExpanded = position==mExpandedPosition;
        holder.innerRecyclerView.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        holder.itemView.setActivated(isExpanded);
        localIndex++;
        Log.v(localIndex+"", "Local Index Count");


        Button button = holder.itemView.findViewById(R.id.openButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExpandedPosition = isExpanded ? -1:position;
                notifyItemChanged(position);
            }
        });
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
        return this.strRecipelist.size();
    }

    public void beginTimer(Intent intent){
        MainActivity activity = new MainActivity();
        activity.startActivity(intent);
    }


}
