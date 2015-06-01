package com.codepath.apps.MyTwitterRedux.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.MyTwitterRedux.R;
import com.codepath.apps.MyTwitterRedux.TwitterClient;
import com.codepath.apps.MyTwitterRedux.fragments.UserHeaderFragment;
import com.codepath.apps.MyTwitterRedux.fragments.UserTimelineFragment;

public class ProfileActivity extends ActionBarActivity {
    private TwitterClient client;
    //private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(com.codepath.apps.MyTwitterRedux.R.layout.activity_profile);
        // Get the screen name
        String screenName = getIntent().getStringExtra("screen_name");
        if (savedInstanceState == null) {
            // Create user timeline fragment
            UserTimelineFragment userTimelineFragment = UserTimelineFragment.newInstance(screenName);
            UserHeaderFragment userHeaderFragment = UserHeaderFragment.newInstance(screenName);
            // Display user fragment within the activity dynamically
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flContainer, userTimelineFragment);
            ft.replace(R.id.flHeader, userHeaderFragment);
            ft.commit();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(com.codepath.apps.MyTwitterRedux.R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == com.codepath.apps.MyTwitterRedux.R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
