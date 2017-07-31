package com.example.dhruvmj.samplerssfeedreader;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dhruvmj.samplerssfeedreader.adapter.RssFeedListAdapter;
import com.example.dhruvmj.samplerssfeedreader.model.RssFeedObject;
import com.example.dhruvmj.samplerssfeedreader.util.Util;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Dhruv Jariwala
 */

public class RssFeedListFragment extends BackgroundTaskFragment<String, RssFeedObject[]> implements OnRefreshListener {
    private static final String TAG = RssFeedListFragment.class.getSimpleName();
    private static final String RSS_FEED_URL = "http://www.cbc.ca/cmlink/rss-topstories";
    private static final String RSS_FEED_DATA_KEY = "com.example.dhruvmj.samplerssfeedreader.RSS_FEED_DATA";

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<RssFeedObject> rssFeedObjectList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rss_feed_list, container, false);
        setRetainInstance(true);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_rss_feed);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        rssFeedObjectList = new ArrayList<>();
        RssFeedListAdapter rssFeedListAdapter = new RssFeedListAdapter(rssFeedObjectList, getActivity());
        recyclerView.setAdapter(rssFeedListAdapter);
        if (savedInstanceState != null) {
            rssFeedObjectList = Arrays.asList((Util.getParcelableArrayData(savedInstanceState, RSS_FEED_DATA_KEY, RssFeedObject.CREATOR)));
            recyclerView.setAdapter(new RssFeedListAdapter(rssFeedObjectList, getActivity()));
        }
        if (rssFeedObjectList.size() == 0) {
            onRefresh();
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArray(RSS_FEED_DATA_KEY, rssFeedObjectList.toArray(new RssFeedObject[rssFeedObjectList.size()]));
    }

    @Override
    public void onDestroyView() {
        recyclerView = null;
        swipeRefreshLayout = null;
        super.onDestroyView();
    }

    @Override
    protected void onBackgroundTaskStarted() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    protected RssFeedObject[] doBackgroundTask() {
        if (TextUtils.isEmpty(RSS_FEED_URL)) {
            return null;
        }

        try {
            if (!RSS_FEED_URL.startsWith("http://") && !RSS_FEED_URL.startsWith("https://")) {
                return null;
            }

            URL url = new URL(RSS_FEED_URL);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(15 * 1000); //15 seconds
            httpURLConnection.connect();
            InputStream inputStream = httpURLConnection.getInputStream();
            return parseFeed(inputStream);
        } catch (IOException e) {
            Log.e(TAG, "IOException : ", e);
        } catch (XmlPullParserException e) {
            Log.e(TAG, "XmlPullParserException : ", e);
        }
        return null;
    }

    private RssFeedObject[] parseFeed(InputStream inputStream) throws XmlPullParserException, IOException {
        RssFeedObject rssFeedObject = new RssFeedObject();
        List<RssFeedObject> rssFeedObjectList = new ArrayList<>();

        try {
            XmlPullParser xmlPullParser = Xml.newPullParser();
            xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            xmlPullParser.setInput(inputStream, null);
            xmlPullParser.nextTag();

            while (xmlPullParser.next() != XmlPullParser.END_DOCUMENT) {
                int eventType = xmlPullParser.getEventType();
                String tagName = xmlPullParser.getName();
                if (tagName == null) {
                    continue;
                }

                if (eventType == XmlPullParser.END_TAG) {
                    if (tagName.equalsIgnoreCase("item")) {
                        rssFeedObjectList.add(rssFeedObject);
                        rssFeedObject = new RssFeedObject();
                    }
                    continue;
                }

                if (eventType == XmlPullParser.START_TAG) {
                    if (tagName.equalsIgnoreCase("item")) {
                        rssFeedObject = new RssFeedObject();
                        continue;
                    }
                }

                String tagValue = "";
                if (xmlPullParser.next() == XmlPullParser.TEXT) {
                    tagValue = xmlPullParser.getText();
                    xmlPullParser.nextTag();
                }

                if (tagName.equalsIgnoreCase("title")) {
                    rssFeedObject.setTitle(tagValue);
                } else if (tagName.equalsIgnoreCase("link")) {
                    rssFeedObject.setLink(tagValue);
                } else if (tagName.equalsIgnoreCase("pubDate")) {
                    rssFeedObject.setPublicationDate(tagValue);
                } else if (tagName.equalsIgnoreCase("author")) {
                    rssFeedObject.setAuthor(tagValue);
                } else if (tagName.equalsIgnoreCase("description")) {
                    rssFeedObject.setDescription(tagValue);
                }
            }
            return rssFeedObjectList.toArray(new RssFeedObject[rssFeedObjectList.size()]);
        } finally {
            inputStream.close();
        }
    }

    @Override
    protected void processBackgroundTask(RssFeedObject[] data) {
        swipeRefreshLayout.setRefreshing(false);
        if (data != null) {
            this.rssFeedObjectList = Arrays.asList(data);
            recyclerView.setAdapter(new RssFeedListAdapter(rssFeedObjectList, getActivity()));
        } else {
            showSnackBarMessage();
        }
    }

    private void showSnackBarMessage() {
        Snackbar snackbar = Snackbar.make(recyclerView, R.string.failed_fetching_rss, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onRefresh();
                    }
                });
        snackbar.show();
    }

    @Override
    public void onRefresh() {
        startBackgroundTask();
    }
}
