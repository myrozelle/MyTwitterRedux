package com.codepath.apps.MyTwitterRedux.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.codepath.apps.MyTwitterRedux.R;
import com.codepath.apps.MyTwitterRedux.adapters.TweetsArrayAdapter;
import com.codepath.apps.MyTwitterRedux.models.Tweet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mrozelle on 5/29/2015.
 */
public class TweetsListFragment extends Fragment{

    protected SwipeRefreshLayout swipeContainer;
    protected ArrayList<Tweet> tweets;
    protected TweetsArrayAdapter aTweets;
    protected ListView lvTweets;

    //inflation logic
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweets_list, container,false);
        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);
        lvTweets = (ListView) v.findViewById(R.id.lv_tweets);
        lvTweets.setAdapter(aTweets);
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        return v;
    }

    //creation lifecycle event
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tweets = new ArrayList<>();
        aTweets = new TweetsArrayAdapter(getActivity(), tweets);
    }

    public void addAll(List<Tweet> tweets) {
        aTweets.addAll(tweets);
    }

    public SwipeRefreshLayout getSwipeContainer() {
        return swipeContainer;
    }

    public TweetsArrayAdapter getTweetsArrayAdapter() {
        return aTweets;
    }

    public ArrayList<Tweet> getTweets() {
        return tweets;
    }

    public ListView getLvTweets() {
        return lvTweets;
    }
}
