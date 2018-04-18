package com.example.yevgeniyshatrovskiy.steepr.Activities;

import android.app.DialogFragment;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.example.yevgeniyshatrovskiy.steepr.Adapter.RecipeAdapter;
import com.example.yevgeniyshatrovskiy.steepr.Fragment.CustomTeaFragment;
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

public class MainActivity extends AppCompatActivity implements CustomTeaFragment.OnFragmentInteractionListener {

    private FirebaseAuth mAuth;
    FirebaseRecyclerAdapter adapter;
    FirebaseRecyclerOptions<Recipe> options;
    RecyclerView recipeRecycler;
    private List<Recipe> allRecipe;
    private RecipeAdapter recipeAdapter;
    public boolean english = true;
    MenuItem logo;

    //Test Database (Works)
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    DatabaseReference myRef = database.child("all");
    ChildEventListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


//        NavigationView navigationView = findViewById(R.id.nav_view);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();

//        navigationView.setNavigationItemSelectedListener(this);

        recipeRecycler = findViewById(R.id.recipeRecycler);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        allRecipe = new ArrayList<Recipe>();

        recipeRecycler.setHasFixedSize(true);
        recipeRecycler.setLayoutManager(new GridLayoutManager(this, 1));


        createListener();

    }

    public void createListener(){

        listener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                getAllTask(dataSnapshot);
                Log.v("GET", "GET ALL");
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                allRecipe.removeAll(allRecipe);
                getAllTask(dataSnapshot);
                Log.v("CHANGED", "CHANGED");
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.v("DELETED", "DELETED");
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

    private void getAllTask(DataSnapshot dataSnapshot){

        boolean missing;
        ArrayList<TeaDetails> details = new ArrayList<>();
        ArrayList<TeaCategory> allRec = new ArrayList<>();
        for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
            Recipe rec = singleSnapshot.getValue(Recipe.class);
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

            for(TeaCategory re : allRec){
                Log.v("CONTEXT", re.toString());
            }

            for(TeaCategory cat : allRec){
                if(cat.getCategoryName().equals(rec.getCategory()) && !(cat.getRecipes().contains(rec))){
                    cat.addRecipes(rec);
                    Log.v("ADDED", rec.getName());
                }
                Log.v("ADDING", rec.getName() + " " +cat.getCategoryName());
            }
        }


        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation_fall_down);
        LinearLayoutManager lln = new GridLayoutManager(this,1);

//        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recipeRecycler.getContext(), lln.getOrientation());
        recipeRecycler.addItemDecoration(new VertSpace(0));
        recipeRecycler.setLayoutAnimation(controller);
        recipeAdapter = new RecipeAdapter(MainActivity.this, details, allRec, english);
        recipeRecycler.setAdapter(recipeAdapter);
        recipeRecycler.scheduleLayoutAnimation();

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

    public void showDialog() {
        DialogFragment newFragment = CustomTeaFragment.newInstance();
        newFragment.show(getFragmentManager(), "dialog");
        myRef.removeEventListener(listener);
        Log.v("LISTENER", "Removed");
    }

    public void restartListener(){
        myRef.addChildEventListener(listener);
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

//    @SuppressWarnings("StatementWithEmptyBody")
//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//
//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }
//
//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }

    public void beginTimerActivity(Recipe re, View view){
        Intent newIntent = new Intent(MainActivity.this, Timer.class);
        newIntent.putExtra("reci", new Gson().toJson(re));
        newIntent.putExtra("english", english);
        newIntent.putExtra("time", re.getSecondsToSteep());
//        ImageView image = findViewById(R.id.imageBackground);
//        image.setBackgroundResource(R.drawable.whitetea);
//        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation
//                (this, view.findViewById(R.id.teaBackground), "myImage");
//        startActivity(newIntent, options.toBundle());
        startActivity(newIntent);
    }
}
