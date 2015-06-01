package com.codepath.apps.MyTwitterRedux.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.codepath.apps.MyTwitterRedux.TwitterApplication;
import com.codepath.apps.MyTwitterRedux.TwitterClient;
import com.codepath.apps.MyTwitterRedux.activities.DetailActivity;
import com.codepath.apps.MyTwitterRedux.listeners.EndlessScrollListener;
import com.codepath.apps.MyTwitterRedux.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by mrozelle on 5/30/2015.
 */
public class UserTimelineFragment extends TweetsListFragment {
    private TwitterClient client;

    public static UserTimelineFragment newInstance(String screenName) {
        UserTimelineFragment userFragment = new UserTimelineFragment();
        Bundle args = new Bundle();
        args.putString("screen_name", screenName);
        userFragment.setArguments(args);
        return userFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        setupListeners();
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient(); //singleton client
        populateTimeLine(0);

    }

    private void setupListeners() {
        lvTweets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(TimelineActivity.this, "list item clicked", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getActivity(), DetailActivity.class);
                i.putExtra("tweet", tweets.get(position));
                startActivity(i);
            }
        });

        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int totalItemsCount) {
                long max_id = aTweets.getItem(totalItemsCount - 1).getUid();
                populateTimeLine(max_id - 1);
            }
        });

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                populateTimeLine(0);
            }
        });


    }

    private void populateTimeLine(final long max_id) {
        String screenName = getArguments().getString("screen_name");
        client.getUserTimeLine(new JsonHttpResponseHandler() {
            // Success
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("DEBUG", response.toString());
                if (max_id <= 0) {
                    //deleteCache();
                    aTweets.clear();// clear adapter if it's first page load
                }
                // deserialize json, create models and add them to the adapter, and load model data into listview
                aTweets.addAll(Tweet.fromJSONArray(response));
                swipeContainer.setRefreshing(false);
            }

            // Failure
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
            }
        }, max_id, screenName);
    }
}
