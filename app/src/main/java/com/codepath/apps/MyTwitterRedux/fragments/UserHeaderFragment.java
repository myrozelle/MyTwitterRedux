package com.codepath.apps.MyTwitterRedux.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.MyTwitterRedux.R;
import com.codepath.apps.MyTwitterRedux.ThemeStringFormatter;
import com.codepath.apps.MyTwitterRedux.TwitterApplication;
import com.codepath.apps.MyTwitterRedux.TwitterClient;
import com.codepath.apps.MyTwitterRedux.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by mrozelle on 5/31/2015.
 */
public class UserHeaderFragment extends Fragment{

    private ImageView ivBackgroundImage;
    private ImageView ivProfileImage;
    private TextView tvUserName;
    private TextView tvScreenName;
    private TextView tvTagLine;
    private Button btFollowers;
    private Button btFollowing;

    public static UserHeaderFragment newInstance(String screenName) {
        UserHeaderFragment userFragment = new UserHeaderFragment();
        Bundle args = new Bundle();
        args.putString("screen_name", screenName);
        userFragment.setArguments(args);
        return userFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(com.codepath.apps.MyTwitterRedux.R.layout.fragment_user_header, container,false);
        String screenName = getArguments().getString("screen_name");
        TwitterClient client = TwitterApplication.getRestClient(); // can call this anywhere and will get the same object back
        // Get the account info
        client.getUserInfo(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                User user = null;
                try {
                    user = User.fromJSON(response.getJSONObject(0));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                setupViews(view, user);
            }
        }, screenName);

        return view;
    }

    private void setupViews(View view, User user) {
        ivBackgroundImage = (ImageView) view.findViewById(R.id.ivBackgroundImage);
        ivProfileImage = (ImageView) view.findViewById(R.id.ivProfileImage);
        tvUserName = (TextView) view.findViewById(R.id.tvUserName);
        tvScreenName = (TextView) view.findViewById(R.id.tvScreenName);
        tvTagLine = (TextView) view.findViewById(R.id.tvTagLine);
        btFollowers = (Button) view.findViewById(R.id.btFollowers);
        btFollowing = (Button) view.findViewById(R.id.btFollowing);

        tvUserName.setText(user.getName());
        tvScreenName.setText("@" + user.getScreenName());
        tvTagLine.setText(user.getTagLine());
        ivProfileImage.setImageResource(android.R.color.transparent);
        Picasso.with(getActivity()).load(user.getProfileImageUrl()).into(ivProfileImage);
        ivBackgroundImage.setImageResource(android.R.color.transparent);
        Picasso.with(getActivity()).load(user.getBackgroundImageUrl()).fit().into(ivBackgroundImage);
        btFollowing.setText(ThemeStringFormatter.roundedNumberFormat(user.getFollowingsCount()) + " FOLLOWING");
        btFollowers.setText(ThemeStringFormatter.roundedNumberFormat(user.getFollowersCount()) + " FOLLOWERS");

    }
}
