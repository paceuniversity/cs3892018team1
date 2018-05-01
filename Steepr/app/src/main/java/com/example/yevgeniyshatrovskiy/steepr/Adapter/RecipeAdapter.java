package com.example.yevgeniyshatrovskiy.steepr.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by yevgeniy on 3/29/18.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>{

    private ArrayList<TeaDetails> strRecipelist;
    private ArrayList<TeaCategory> teaCategories;
    private ArrayList<String> favorites;
    private Recipe recipE;
    protected Context context;
    private LinearLayoutManager lln;
    public int mExpandedPosition = -1;
    public int previousExpandedPosition = -1;
    public boolean english;
    public String userID;
    public InnerRecipeAdapter innerRecipeAdapter;
    DatabaseReference database;
    DatabaseReference myRef;

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

    public void notifychange(){
        innerRecipeAdapter.notifyDataSetChanged();
        notifyDataSetChanged();
        for(TeaCategory ca : teaCategories){
            for(Recipe rep : ca.getRecipes()){
                Log.v("RMI: notify name", rep.getName());
                Log.v("RMI: notify category", rep.getCategory());
            }
        }
    }


    public RecipeAdapter(Context context, ArrayList<TeaDetails> recipeList,
                         ArrayList<TeaCategory> teaCategories, boolean english,
                         String userID, ArrayList<String> favorites){
        this.context = context;
        this.strRecipelist = recipeList;
        this.teaCategories = teaCategories;
        this.english = english;
        this.userID = userID;
        this.favorites = favorites;
    }



    public ArrayList<TeaDetails> getStrRecipelist() {
        return strRecipelist;
    }

    public void setStrRecipelist(ArrayList<TeaDetails> strRecipelist) {
        this.strRecipelist = strRecipelist;
    }

    public ArrayList<TeaCategory> getTeaCategories() {
        return teaCategories;
    }

    public void setTeaCategories(ArrayList<TeaCategory> teaCategories) {
        this.teaCategories = teaCategories;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.tea_category_layout, parent, false);
        final RecipeViewHolder viewHolder = new RecipeViewHolder(layout, strRecipelist);

        database = FirebaseDatabase.getInstance().getReference();
        myRef = database.child("users");

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecipeViewHolder holder, final int position) {
        Drawable draw;

        int draws;
        lln = new GridLayoutManager(context,1);
        holder.innerRecyclerView.setLayoutManager(lln);

        innerRecipeAdapter = new InnerRecipeAdapter(context, teaCategories.get(position).getRecipes(), english, favorites);
        Log.v("Holder: ", teaCategories.get(position).getRecipes().get(0).getName());
        holder.innerRecyclerView.setAdapter(innerRecipeAdapter);
        try{
            draws = context.getResources().getIdentifier(strRecipelist.get(position).getImageName()
                    , "drawable"
                    , context.getPackageName());

            Log.v("colors ", strRecipelist.get(position).getBackgroundColor());
            holder.imageColor.setBackgroundColor(Color.parseColor(strRecipelist.get(position).getBackgroundColor()));

            if(english)
                holder.actualText.setText(strRecipelist.get(position).getCategoryName());
            else
                holder.actualText.setText(strRecipelist.get(position).getChineseCategory());

            holder.actualText.setTextColor(Color.parseColor((strRecipelist.get(position).getTextColor())));
            draw = context.getDrawable(draws);
            holder.imageText.setImageDrawable(draw);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        final boolean isExpanded = position==mExpandedPosition;
        holder.innerRecyclerView.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        holder.itemView.setActivated(isExpanded);

        if (isExpanded)
            previousExpandedPosition = position;

        Button button = holder.itemView.findViewById(R.id.openButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExpandedPosition = isExpanded ? -1:position;
                notifyItemChanged(previousExpandedPosition);
                notifyItemChanged(position);

            }
        });
//
//        final Swipe swipe = new Swipe(new SwipeActions() {
//            @Override
//            public void onRightClicked(int pos) {
//
//                DatabaseReference database = FirebaseDatabase.getInstance().getReference();
//                DatabaseReference myRef = database.child("users");
//
//                Recipe rep = teaCategories.get(position).getRecipes().get(pos);
//                Log.v("REP", rep.getName());
//
////                myRef.child(userID).child("Favorites").child(rep.getName()).setValue(rep);
//
////                Recipe rep = new Recipe("Awesome", "Weird", 60, null,
////                        "greentea","Favorite", "#ffffff","greentea",
////                        "#BD8300","茶","茶");
////                myRef.child(userID).child("Favorites").child(rep.getName()).removeValue();
//
////                Log.v("REC:",  teaCategories.get(position).getRecipes().get(position);
//            }
//        });
//        holder.innerRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
//            @Override
//            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
//                swipe.onDraw(c, teaCategories.get(position).getRecipes());
//            }
//        });
//
//        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipe);
//        itemTouchHelper.attachToRecyclerView(holder.innerRecyclerView);

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
