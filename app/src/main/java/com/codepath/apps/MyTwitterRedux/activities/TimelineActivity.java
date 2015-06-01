package com.codepath.apps.MyTwitterRedux.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.MyTwitterRedux.R;
import com.codepath.apps.MyTwitterRedux.TwitterApplication;
import com.codepath.apps.MyTwitterRedux.TwitterClient;
import com.codepath.apps.MyTwitterRedux.adapters.SmartFragmentStatePagerAdapter;
import com.codepath.apps.MyTwitterRedux.fragments.ComposeFragment;
import com.codepath.apps.MyTwitterRedux.fragments.HomeTimelineFragment;
import com.codepath.apps.MyTwitterRedux.fragments.MentionsTimelineFragment;
import com.codepath.apps.MyTwitterRedux.models.Tweet;
import com.codepath.apps.MyTwitterRedux.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

public class TimelineActivity extends ActionBarActivity implements ComposeFragment.ComposeFragmentListener{

    private ViewPager vpPager;
    private TweetsPagerAdapter aPager;
    private User thisUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        getAccountCredentials();
        // Get viewpager
        vpPager = (ViewPager) findViewById(R.id.viewpager);
        // Set viewpager adapter for the pager
        aPager = new TweetsPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(aPager);
        // Find the sliding tabstrip
        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        // Attach the tabstrip to the viewpager
        tabStrip.setViewPager(vpPager);
        if (savedInstanceState == null) {
            //homeTimelineFragment = (HomeTimelineFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_timeline);
        }

    }

    private void getAccountCredentials() {
        TwitterClient client = TwitterApplication.getRestClient();
        client.getAccountCredentials(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                thisUser = User.fromJSON(response);
                //Log.d("Success", response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d("Failure", "status code = " + statusCode + ", response = " + errorResponse.toString());
            }
        });
    }

    private void setupViews() {
        //add icon to action bar
        /*getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.miCompose) {
            FragmentManager fm = getSupportFragmentManager();
            ComposeFragment composeDialogFragment = ComposeFragment.newInstance(thisUser);
            composeDialogFragment.show(fm, "fragment_compost");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPostTweet(Tweet tweet) {
        HomeTimelineFragment homeTimelineFragment = (HomeTimelineFragment) aPager.getRegisteredFragment(0);
        homeTimelineFragment.getTweetsArrayAdapter().insert(tweet, 0);
    }

    public void onProfileView(MenuItem item) {
        Intent i = new Intent(this, ProfileActivity.class);
        i.putExtra("screen_name", thisUser.getScreenName());
        startActivity(i);
    }

    // return the order of fragments in the view pager
    public class TweetsPagerAdapter extends SmartFragmentStatePagerAdapter {
        private String tabTitles[] = {"Home", "Mentions"};

        //Adapter gets the manager insert or remove fragment from activity
        public TweetsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        // The order and creation of fragments within the pager
        // will cache for us automatically
        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new HomeTimelineFragment();
            } else if (position == 1) {
                return new MentionsTimelineFragment();
            } else {
                return null;
            }
        }

        //get how many tabs we are having
        @Override
        public int getCount() {
            return tabTitles.length;
        }

        // Return tab title
        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }
    }

}
