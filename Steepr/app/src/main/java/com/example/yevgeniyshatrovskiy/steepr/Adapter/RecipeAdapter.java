package com.example.yevgeniyshatrovskiy.steepr.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.yevgeniyshatrovskiy.steepr.Activities.MainActivity;
import com.example.yevgeniyshatrovskiy.steepr.Objects.Recipe;
import com.example.yevgeniyshatrovskiy.steepr.Objects.TeaCategory;
import com.example.yevgeniyshatrovskiy.steepr.Objects.TeaDetails;
import com.example.yevgeniyshatrovskiy.steepr.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by yevgeniy on 3/29/18.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>{

    private ArrayList<TeaDetails> strRecipelist;
    private ArrayList<TeaCategory> teaCategories;
    private Recipe recipE;
    protected Context context;
    private LinearLayoutManager lln;
    public int mExpandedPosition = -1;

    public static class RecipeViewHolder extends RecyclerView.ViewHolder{


        private TextView rName;
        private ImageView imageText;
        private Button rButton;
        private TextView imageColor;
        private TextView actualText;
        public RecyclerView innerRecyclerView;

        public RecipeViewHolder(View itemView, final ArrayList<TeaDetails> recipes){
            super(itemView);
            actualText = itemView.findViewById(R.id.texthollow);
            imageColor = itemView.findViewById(R.id.textbackground);
            imageText = itemView.findViewById(R.id.teaBackground);
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


    public RecipeAdapter(Context context, ArrayList<TeaDetails> recipeList, ArrayList<TeaCategory> teaCategories){
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
        Drawable draw;
        InnerRecipeAdapter innerRecipeAdapter;
        int draws;
        lln = new GridLayoutManager(context,1);
        holder.innerRecyclerView.setLayoutManager(lln);
        innerRecipeAdapter = new InnerRecipeAdapter(context, teaCategories.get(position).getRecipes());
        holder.innerRecyclerView.setAdapter(innerRecipeAdapter);
        try{
            draws = context.getResources().getIdentifier(strRecipelist.get(position).getImageName()
                    , "drawable"
                    , context.getPackageName());

            Log.v("colors ", strRecipelist.get(position).getBackgroundColor());
            holder.imageColor.setBackgroundColor(Color.parseColor(strRecipelist.get(position).getBackgroundColor()));
            holder.actualText.setText(strRecipelist.get(position).getCategoryName());
            holder.actualText.setTextColor(Color.parseColor((strRecipelist.get(position).getTextColor())));
            draw = context.getDrawable(draws);
            holder.imageText.setImageDrawable(draw);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        final boolean isExpanded = position==mExpandedPosition;
        holder.innerRecyclerView.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        holder.itemView.setActivated(isExpanded);

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
