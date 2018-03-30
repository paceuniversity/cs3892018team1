package com.example.yevgeniyshatrovskiy.steepr.Activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.yevgeniyshatrovskiy.steepr.Adapter.RecipeAdapter;
import com.example.yevgeniyshatrovskiy.steepr.Objects.Recipe;
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

import java.util.ArrayList;
import java.util.List;

//public class MainActivity extends AppCompatActivity
//        implements NavigationView.OnNavigationItemSelectedListener

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private FirebaseAuth mAuth;
    FirebaseRecyclerAdapter adapter;
    FirebaseRecyclerOptions<Recipe> options;
    RecyclerView recipeRecyler;
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
        final DatabaseReference writeRef = database.getReference("Public");
        final DatabaseReference myRef = database.getReference();


        Recipe greenTea = new Recipe("Green Tea", "Simple Tea", 20, null);
        writeRef.child(greenTea.getName()).setValue(greenTea);
        Recipe blueTea = new Recipe("Blue Tea", "Not Simple Tea", 20, null);
        writeRef.child(blueTea.getName()).setValue(blueTea);
        ArrayList<String> ingredients = new ArrayList<>();
        ingredients.add("Honey");
        ingredients.add("Sugar");
        Recipe yellowTea = new Recipe("Yellow Tea", "Complex Tea", 20, ingredients);
        writeRef.child(yellowTea.getName()).setValue(yellowTea);




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




        recipeRecyler = findViewById(R.id.recipeRecycler);


        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        allRecipe = new ArrayList<Recipe>();
        recipeRecyler = findViewById(R.id.recipeRecycler);
        recipeRecyler.setHasFixedSize(true);
        recipeRecyler.setLayoutManager(new LinearLayoutManager(this));


        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                getAllTask(dataSnapshot);
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
        });



    }


    private void getAllTask(DataSnapshot dataSnapshot){
        for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
            Recipe rec = singleSnapshot.getValue(Recipe.class);
            allRecipe.add(rec);
            recipeAdapter = new RecipeAdapter(MainActivity.this, allRecipe);
            recipeRecyler.setAdapter(recipeAdapter);
        }
    }

    private void taskDeletion(DataSnapshot dataSnapshot){
        for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
            Recipe rec = singleSnapshot.getValue(Recipe.class);
            Log.v(rec.getName(), "TEST");
            String name = rec.getName();
            for(int i = 0; i < allRecipe.size(); i++){
                if(allRecipe.get(i).getName().equals(name)){
                    allRecipe.remove(i);
                }
            }
            recipeAdapter.notifyDataSetChanged();
            recipeAdapter = new RecipeAdapter(MainActivity.this, allRecipe);
            recipeRecyler.setAdapter(recipeAdapter);
        }
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
}
