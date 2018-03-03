package com.bitchat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import org.json.JSONArray;

import fragments.ForumListFragment;
import fragments.FriendsFragment;
import fragments.InsideForumFragment;
import fragments.aboutFragment;
import fragments.gMapFragment;
import fragments.settingsFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //MAIN ACTIVITY
    //This Activity has two main states depending on the type of fragment that is running. (inside forum or outside forum)
    //All the pages while logged in will be contained inside this Activity. See LoginActivity for while logged out.


    //STATE VARIABLES
    //This value is used to determine weather or not to close the keyboard and many other parts of the functionality.
    public static boolean inForum;
    //This value stores the name of a forum most recently selected used to open the correct forum by the InsideForumFragment
    public static String chosenForum;

    //FRAGMENT MANAGER
    // used to manage the fragments inside the Container/FrameLayout.
    FragmentManager fragmentManager;

    //USERNAME
    // Used to make posts and to access personal forum (username can be changes in settings)
    public static String username;

    //JSONARRAYs
    //these are used to store the various arrays which are pulled from the database
    //to either fill the chat list or the forums list
    public static JSONArray chatArray;
    public static JSONArray forumArray;
    public String lastForumType;
    public DatabaseOperations db;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseOperations();

        //get the username from the previous activity (LoginActivity)
        Intent myIntent = getIntent();

        username = myIntent.getStringExtra("username");
        Log.d("response", username);
        //initialise the arrays
        chatArray = new JSONArray();
        forumArray = new JSONArray();
        //set the ContentView
        setContentView(R.layout.activity_main);

        //NAVIGATION MENUS SETUP
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        drawer.setScrimColor(Color.TRANSPARENT);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //insert the first fragment (gMapFragment) into the frame_container
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_container, new gMapFragment()).commit();





        //every 500 milliseconds run the db function pullChatList if the user is inside a forum.
        final Handler ha = new Handler();

        ha.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (inForum)
                    db.pullChatList(chosenForum, getApplicationContext());

                setChatList(chatArray);

                ha.postDelayed(this, 500);

            }
        }, 500);


    }

    //update the chatArrayList
    public void setChatList(JSONArray jsonArray) {
        this.chatArray = jsonArray;
    }

    //When the back button is pressed the following logic determines what happens.
    @Override
    public void onBackPressed() {

        //hide the keyboard
        View keybView = this.getCurrentFocus();
        if (keybView != null) {
            InputMethodManager imm = (InputMethodManager) this.getSystemService(this.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(keybView.getWindowToken(), 0);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (inForum) {
            inForum = false;
            switch (lastForumType) {
                case "Other Users Forum":
                    fragmentManager.beginTransaction().replace(R.id.frame_container, new FriendsFragment()).commit();
                    break;
                case "From ForumList Forum":
                    fragmentManager.beginTransaction().replace(R.id.frame_container, new ForumListFragment()).commit();
                    break;
                default:
                    fragmentManager.beginTransaction().replace(R.id.frame_container, new gMapFragment(), "MAP_FRAGMENT").commit();
            }
        } else {
            gMapFragment mapFragment = (gMapFragment) fragmentManager.findFragmentByTag("MAP_FRAGMENT");
            if (mapFragment != null && mapFragment.isVisible())
                moveTaskToBack(true);
            else
                fragmentManager.beginTransaction().replace(R.id.frame_container, new gMapFragment(), "MAP_FRAGMENT").commit();

        }
    }

    //create right hand side option menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    //the following logic is used to determine what happens when an item is selected from the options menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.action_settings)
            fragmentManager.beginTransaction().replace(R.id.frame_container, new settingsFragment()).commit();


        if (id == R.id.action_sign_out) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    //the following logic is used to determine what happens when an item is selected from the Navigation Drawer
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        //hide the keyboard
        View keybView = this.getCurrentFocus();

        if (keybView != null) {
            InputMethodManager imm = (InputMethodManager) this.getSystemService(this.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(keybView.getWindowToken(), 0);
        }

        //when navigating to a different forum reset the state variables.
        chatArray = null;
        forumArray = null;
        inForum = false;

        int id = item.getItemId();

        //Campus Map
        if (id == R.id.nav_map)
            fragmentManager.beginTransaction().replace(R.id.frame_container, new gMapFragment(), "MAP_FRAGMENT").commit();

            //Area Forums
        else if (id == R.id.nav_forums)
            fragmentManager.beginTransaction().replace(R.id.frame_container, new ForumListFragment()).commit();

            //Your Forum
        else if (id == R.id.nav_your_forum)
            enterForum(username, "Personal Forum");

            //User's Forum
        else if (id == R.id.nav_friends)
            fragmentManager.beginTransaction().replace(R.id.frame_container, new FriendsFragment()).commit();

            // -- OTHER --
            //Settings
        else if (id == R.id.nav_settings)
            fragmentManager.beginTransaction().replace(R.id.frame_container, new settingsFragment()).commit();

            //About
        else if (id == R.id.nav_about)
            fragmentManager.beginTransaction().replace(R.id.frame_container, new aboutFragment()).commit();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    //this function is used to enter a forum. The name of the forum and the forumType are passed in as arguements
    public void enterForum(String forum, String forumType) {
        //store the values passed into enterForum
        chosenForum = forum;
        lastForumType = forumType;
        inForum = true;

        InsideForumFragment insideForumFragment = new InsideForumFragment();


        fragmentManager.beginTransaction().replace(R.id.frame_container, insideForumFragment).commit();

    }


}