package com.example.dhruvmj.samplerssfeedreader.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dhruvmj.samplerssfeedreader.R;
import com.example.dhruvmj.samplerssfeedreader.WebBrowserActivity;
import com.example.dhruvmj.samplerssfeedreader.model.RssFeedObject;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * @author Dhruv Jariwala
 */

public class RssFeedListAdapter extends RecyclerView.Adapter<RssFeedListAdapter.ViewHolder> {
    private List<RssFeedObject> rssFeedObjectList;
    private Activity activity;
    private int imageThumbSizeDimen;

    public RssFeedListAdapter(List<RssFeedObject> rssFeedObjectList, Activity context) {
        this.rssFeedObjectList = rssFeedObjectList;
        this.activity = context;
        imageThumbSizeDimen = activity.getResources().getDimensionPixelSize(R.dimen.image_thumb_size);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView publicationDateTextView;
        TextView authorTextView;
        ImageView feedImageView;

        ViewHolder(View view) {
            super(view);
            titleTextView = (TextView) view.findViewById(R.id.title);
            publicationDateTextView = (TextView) view.findViewById(R.id.publication_date);
            authorTextView = (TextView) view.findViewById(R.id.author);
            feedImageView = (ImageView) view.findViewById(R.id.rss_feed_image);
        }
    }

    @Override
    public RssFeedListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_rss_feed_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (position == RecyclerView.NO_POSITION) {
            return;
        }
        final RssFeedObject rssFeedObject = rssFeedObjectList.get(position);
        holder.titleTextView.setText(rssFeedObject.getTitle());
        holder.publicationDateTextView.setText(rssFeedObject.getPublicationDate());
        holder.authorTextView.setText(rssFeedObject.getAuthor());
        Picasso.with(activity)
                .load(rssFeedObject.getImageUrl())
                .resize(imageThumbSizeDimen, imageThumbSizeDimen)
                .centerCrop()
                .into(holder.feedImageView);
        holder.itemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                WebBrowserActivity.startWebBrowserActivity(activity, rssFeedObject.getLink(), rssFeedObject.getTitle());
            }
        });
    }

    @Override
    public int getItemCount() {
        return rssFeedObjectList.size();
    }
}
