package com.example.yevgeniyshatrovskiy.steepr.Activities;

import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ProgressBar;

import com.example.yevgeniyshatrovskiy.steepr.Adapter.RecipeAdapter;
import com.example.yevgeniyshatrovskiy.steepr.Fragment.CustomFragment;
import com.example.yevgeniyshatrovskiy.steepr.Objects.DualList;
import com.example.yevgeniyshatrovskiy.steepr.Objects.Recipe;
import com.example.yevgeniyshatrovskiy.steepr.Objects.TeaCategory;
import com.example.yevgeniyshatrovskiy.steepr.Objects.TeaDetails;
import com.example.yevgeniyshatrovskiy.steepr.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

//public class MainActivity extends AppCompatActivity
//        implements NavigationView.OnNavigationItemSelectedListener

public class MainActivity extends AppCompatActivity implements CustomFragment.OnFragmentInteractionListener {

    FirebaseRecyclerAdapter adapter;
    FirebaseRecyclerOptions<Recipe> options;
    RecyclerView recipeRecycler;
    private List<Recipe> allRecipe;
    private RecipeAdapter recipeAdapter;
    public boolean english = true;
    MenuItem logo;
    private android.widget.RelativeLayout.LayoutParams layoutParams;
    int x_cord;
    int y_cord;
    int count = 0;
    FirebaseAuth mAuth;
    String userID;
    DualList favorites;
    DualList common;
    ArrayList<String> teaCategoriesForFragment;


    //Test Database (Works)
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    DatabaseReference myRef = database.child("all");
    DatabaseReference favRef = database.child("users");
    ChildEventListener listener;
    ChildEventListener favListener;
    ProgressBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();

        FirebaseAuth auth = FirebaseAuth.getInstance();

        FirebaseUser firebaseUser = auth.getCurrentUser();
        userID = firebaseUser.getUid();

//        NavigationView navigationView = findViewById(R.id.nav_view);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        recipeRecycler = findViewById(R.id.recipeRecycler);

        mAuth = FirebaseAuth.getInstance();
        allRecipe = new ArrayList<Recipe>();

        bar = findViewById(R.id.progressBar);
        bar.setVisibility(View.VISIBLE);
        recipeRecycler.setVisibility(View.GONE);
        recipeRecycler.setHasFixedSize(true);
        recipeRecycler.setLayoutManager(new GridLayoutManager(this, 1));
        recipeRecycler.invalidate();

        createListener();

    }

    public void createListener(){

        favListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.v("FAV", "ADDED");
                favorites = getAllTask(dataSnapshot);
                refreshList();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.v("FAV", "CHANGED");
                favorites = null;
                favorites = getAllTask(dataSnapshot);
                refreshList();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.v("DELETEDM", "DELETED");
//                removeItems(dataSnapshot);
                favorites = null;
                refreshList();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        favRef.child(userID).addChildEventListener(favListener);

        listener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                common = getAllTask(dataSnapshot);
                refreshList();
                Log.v("FAVF", "GET ALL");
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                allRecipe.removeAll(allRecipe);
                common = getAllTask(dataSnapshot);
                refreshList();
                Log.v("FAVF", "CHANGED");
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.v("FAVF", "DELETED");
//                removeItems(dataSnapshot);

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        myRef.addChildEventListener(listener);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

//    public void removeItems(DataSnapshot dataSnapshot){
//        for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
//            Recipe rec = singleSnapshot.getValue(Recipe.class);
//            for(TeaCategory cat : favorites.getAllRec()){
//                for(Recipe rep : cat.getRecipes()){
//                    if(rep.getCategory().equals(rec.getCategory())){
//
//                    }
//                }
//            }
//
//            myRef.removeEventListener(listener);
//            favRef.child(userID).removeEventListener(favListener);
//            restartListener();
//        }
//    }

    private DualList getAllTask(DataSnapshot dataSnapshot){

        boolean missing;

        ArrayList<TeaDetails> details = new ArrayList<>();
        ArrayList<TeaCategory> allRec = new ArrayList<>();

        for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
            Recipe rec = singleSnapshot.getValue(Recipe.class);
//            Log.v("FAV", rec.getCategory());
            missing = true;

            if(details.isEmpty()){
                allRec.add(new TeaCategory(rec));
                details.add(new TeaDetails(rec.getCategory(), rec.getBackGroundImage(),rec.getBackGroundColor(),
                        rec.getTextColor(), rec.getChineseName(), rec.getChineseCategory()));
                missing = false;
            }else{
                for(TeaDetails deets:details) {
                    if(deets.getCategoryName().equals(rec.getCategory())) {
                        missing = false;
                    }
                }
            }

            if(missing){
                details.add(new TeaDetails(rec.getCategory(), rec.getBackGroundImage(),rec.getBackGroundColor(),
                        rec.getTextColor(), rec.getChineseName(), rec.getChineseCategory()));
                allRec.add(new TeaCategory(rec));
            }

//            for(TeaCategory re : allRec){
//                Log.v("CONTEXT", re.toString());
//            }

            for(TeaCategory cat : allRec){
                if(cat.getCategoryName().equals(rec.getCategory()) && !(cat.getRecipes().contains(rec))){
                    cat.addRecipes(rec);
//                    Log.v("ADDED", rec.getName());
                }
//                Log.v("ADDING", rec.getName() + " " +cat.getCategoryName());
            }
        }

        Log.v("ADAPT3", allRec.get(0).getCategoryName());

        if(!(allRec.get(0).getCategoryName().equals("Favorite"))){
            teaCategoriesForFragment = new ArrayList<>();

            for(TeaCategory tea : allRec){
                teaCategoriesForFragment.add(tea.getCategoryName());
                Log.v("ADAPTADD", tea.getCategoryName());
            }
        }



        return new DualList(details, allRec);

    }

    public void refreshList() {


        ArrayList<String> names = new ArrayList<>();
//        recipeRecycler.setVisibility(View.GONE);

        ArrayList<TeaDetails> finalDetails = new ArrayList<>();
        ArrayList<TeaCategory> finalAllRec = new ArrayList<>();
        try{

            try{
                finalDetails.addAll(favorites.getDetails());
                finalAllRec.addAll(favorites.getAllRec());

                for(Recipe rep : favorites.getAllRec().get(0).getRecipes()){
                    names.add(rep.getName());
                }

            }catch (Exception e){
                e.printStackTrace();
            }

            try{
                finalDetails.addAll(common.getDetails());
                finalAllRec.addAll(common.getAllRec());
            }catch (Exception e){

            }


            final LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation_fall_down);
            LinearLayoutManager lln = new GridLayoutManager(this,1);

//        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recipeRecycler.getContext(), lln.getOrientation());

            recipeRecycler.addItemDecoration(new VertSpace(0));
            recipeRecycler.setLayoutAnimation(controller);
            recipeAdapter = new RecipeAdapter(MainActivity.this, finalDetails, finalAllRec, english,
                    userID, names);
            recipeRecycler.setAdapter(recipeAdapter);
            recipeRecycler.scheduleLayoutAnimation();
            count++;
            recipeRecycler.setVisibility(View.VISIBLE);
            bar.setVisibility(View.GONE);



        }catch (Exception e){
            e.printStackTrace();
        }


    }

    class VertSpace extends RecyclerView.ItemDecoration{
        private final int spacer;

        public VertSpace(int spacer){
            this.spacer = spacer;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.bottom = spacer;

        }
    }

    public void changeLanguage(){
        if(english == true){
            english = false;
            createListener();
            Log.v("ENGLISH", "SET FALSE");
        }
        else{
            english = true;
            createListener();
            Log.v("ENGLISH", "SET TRUE");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        restartListener();
    }

    public void showDialog() {
        FragmentTransaction fragmentTransaction  = getSupportFragmentManager().beginTransaction();
        CustomFragment fragment = new CustomFragment();

//        MyFragmentClass mFrag = new MyFragmentClass();
//        Bundle bundle = new Bundle();
//        bundle.putString("DocNum", docNum);   //parameters are (key, value).
//        mFrag.setArguments(bundle);

        Bundle bundle = new Bundle();
        bundle.putStringArrayList("categories", teaCategoriesForFragment);
        fragment.setArguments(bundle);

        fragmentTransaction.add(R.id.fragment, fragment).addToBackStack("Test").commit();

        myRef.removeEventListener(listener);
        favRef.child(userID).removeEventListener(favListener);
        Log.v("LISTENER", "Removed");
    }

    public void restartListener(){
        myRef.addChildEventListener(listener);
        favRef.child(userID).addChildEventListener(favListener);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.mybutton) {
            if(english)
                item.setIcon(R.drawable.chineselogo);
            else
                item.setIcon(R.drawable.englishlogo);
            changeLanguage();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void beginTimerActivity(Recipe re, View view, boolean fav){
        Intent newIntent = new Intent(MainActivity.this, Timer.class);
        newIntent.putExtra("fav", fav);
        newIntent.putExtra("reci", new Gson().toJson(re));
        newIntent.putExtra("english", english);
        newIntent.putExtra("time", re.getSecondsToSteep());
        startActivity(newIntent);
    }
}
