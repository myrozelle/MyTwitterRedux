package com.codepath.apps.MyTwitterRedux.adapters;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.MyTwitterRedux.R;
import com.codepath.apps.MyTwitterRedux.activities.ProfileActivity;
import com.codepath.apps.MyTwitterRedux.activities.TimelineActivity;
import com.codepath.apps.MyTwitterRedux.models.Tweet;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Manunya on 5/23/2015.
 */
//taking the tweet objects and turning them into Views displayed in the list
public class TweetsArrayAdapter extends ArrayAdapter<Tweet>{

    private static class ViewHolder {
        ImageView ivProfileImage;
        TextView tvUserName;
        TextView tvBody;
        TextView tvTimeStamp;
        Button btRetweet;
        Button btFavorite;
    }

    public TweetsArrayAdapter(Context context, List<Tweet> tweets) {
        super(context, android.R.layout.simple_list_item_1, tweets);
    }

    // Override and setup custom template

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //get the tweet
        final Tweet tweet = getItem(position);
        ViewHolder viewHolder; // view lookup cache stored in tag
        //find or inflate the template
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent, false);
            //find the subviews to fill with data in template
            viewHolder.ivProfileImage = (ImageView) convertView.findViewById(R.id.ivProfileImage);
            viewHolder.tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
            viewHolder.tvBody = (TextView) convertView.findViewById(R.id.tvBody);
            viewHolder.tvTimeStamp = (TextView) convertView.findViewById(R.id.tvTimeStamp);
            viewHolder.btRetweet = (Button) convertView.findViewById(R.id.btRetweet);
            viewHolder.btFavorite = (Button) convertView.findViewById(R.id.btFavorite);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //populate data into the subviews
        Spanned formattedUserName = Html.fromHtml("<b>" + tweet.getUser().getName() + "</b>");
        Spanned formattedScreenName = Html.fromHtml("<font color=" + getContext().getResources().getColor(R.color.light_gray) + ">"
                + "@" + tweet.getUser().getScreenName() + "</font>");
        viewHolder.tvUserName.setText(TextUtils.concat(formattedUserName, " ", formattedScreenName));
        viewHolder.tvBody.setText(tweet.getBody());
        viewHolder.tvTimeStamp.setText(tweet.getRelativeCreatedAtShort());
        viewHolder.btRetweet.setText(tweet.getRetweetCount()+"");
        viewHolder.btFavorite.setText(tweet.getFavoriteCount()+"");
        viewHolder.ivProfileImage.setImageResource(android.R.color.transparent); // clear out the old image for recycled view
        Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl()).into(viewHolder.ivProfileImage);
        viewHolder.ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // launch profile from clicking the image icon
                Intent i = new Intent(getContext(), ProfileActivity.class);
                i.putExtra("screen_name", tweet.getUser().getScreenName());
                ((TimelineActivity) getContext()).startActivity(i);
            }
        });
        //return the view to be inserted into the list
        return convertView;
    }




}
