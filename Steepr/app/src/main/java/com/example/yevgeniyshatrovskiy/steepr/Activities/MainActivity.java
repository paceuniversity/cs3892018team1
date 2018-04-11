package com.example.yevgeniyshatrovskiy.steepr.Activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.yevgeniyshatrovskiy.steepr.Adapter.RecipeAdapter;
import com.example.yevgeniyshatrovskiy.steepr.Objects.Recipe;
import com.example.yevgeniyshatrovskiy.steepr.Objects.TeaCategory;
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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

//public class MainActivity extends AppCompatActivity
//        implements NavigationView.OnNavigationItemSelectedListener

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private FirebaseAuth mAuth;
    FirebaseRecyclerAdapter adapter;
    FirebaseRecyclerOptions<Recipe> options;
    RecyclerView recipeRecycler;
//    RecyclerView innerRecycler;
    private List<Recipe> allRecipe;
    private RecipeAdapter recipeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //Test Database (Works)
        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        final DatabaseReference writeRef = database.getReference("public");
        final DatabaseReference myRef = database.getReference("all");


//        //Database Practice Objects
//        Recipe greenTea = new Recipe("Green Tea", "Simple Tea", 20, null);
//        writeRef.child(greenTea.getName()).setValue(greenTea);
//        Recipe blueTea = new Recipe("Blue Tea", "Not Simple Tea", 20, null);
//        writeRef.child(blueTea.getName()).setValue(blueTea);
//        ArrayList<String> ingredients = new ArrayList<>();
//        ingredients.add("Honey");
//        ingredients.add("Sugar");
//        Recipe yellowTea1 = new Recipe("Yellow Tea1", "Complex Tea", 20, ingredients);
//        writeRef.child(yellowTea1.getName()).setValue(yellowTea1);
//
//        Recipe yellowTea2 = new Recipe("Yellow Tea2", "Complex Tea", 20, ingredients);
//        writeRef.child(yellowTea2.getName()).setValue(yellowTea2);
//
//        Recipe yellowTea3 = new Recipe("Yellow Tea3", "Complex Tea", 20, ingredients);
//        writeRef.child(yellowTea3.getName()).setValue(yellowTea3);
//
//        Recipe yellowTea4 = new Recipe("Yellow Tea4", "Complex Tea", 20, ingredients);
//        writeRef.child(yellowTea4.getName()).setValue(yellowTea4);
//
//        Recipe yellowTea5 = new Recipe("Yellow Tea5", "Complex Tea", 20, ingredients);
//        writeRef.child(yellowTea5.getName()).setValue(yellowTea5);
//
//        Recipe yellowTea6 = new Recipe("Yellow Tea6", "Complex Tea", 20, ingredients);
//        writeRef.child(yellowTea6.getName()).setValue(yellowTea6);
//
//        Recipe yellowTea7 = new Recipe("Yellow Tea7", "Complex Tea", 20, ingredients);
//        writeRef.child(yellowTea7.getName()).setValue(yellowTea7);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();




        recipeRecycler = findViewById(R.id.recipeRecycler);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        allRecipe = new ArrayList<Recipe>();

        recipeRecycler.setHasFixedSize(true);
        recipeRecycler.setLayoutManager(new GridLayoutManager(this, 1));




        ChildEventListener listener = new ChildEventListener() {
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


        ArrayList<String> categories = new ArrayList<>();
        ArrayList<TeaCategory> allRec = new ArrayList<>();
        for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
            Recipe rec = singleSnapshot.getValue(Recipe.class);

            if(!categories.contains(rec.getCategory())){
                Log.v("NOT", rec.getCategory());
                categories.add(rec.getCategory());
                allRec.add(new TeaCategory(rec.getCategory()));
            }

            for(TeaCategory cat : allRec){
                if(cat.getCategoryName().equals(rec.getCategory()))
                    cat.addRecipes(rec);
            }
        }

        Log.v(allRec.size() +"", "allRec Size");
        Log.v(categories.get(0) +"", "cat Size");
        recipeAdapter = new RecipeAdapter(MainActivity.this, categories, allRec);
        recipeRecycler.setAdapter(recipeAdapter);
    }




    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void inflateFragment(){

    }

    public void beginTimerActivity(float timer, View view){
        Intent newIntent = new Intent(MainActivity.this, Timer.class);
        Bundle bundle = new Bundle();
        bundle.putFloat("timeToSteep", timer);
        newIntent.putExtras(bundle);

        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation
                (this, view.findViewById(R.id.imageView3), "myImage");
        startActivity(newIntent, options.toBundle());
    }
}
