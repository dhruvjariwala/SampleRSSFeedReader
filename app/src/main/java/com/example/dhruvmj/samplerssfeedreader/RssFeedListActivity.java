package com.example.dhruvmj.samplerssfeedreader;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * @author Dhruv Jariwala
 */

public class RssFeedListActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rss_feed_list);
        setTitle(R.string.app_title);

        FragmentManager fragmentManager = getSupportFragmentManager();
        RssFeedListFragment mainFragment = (RssFeedListFragment) fragmentManager.findFragmentById(R.id.fl_main_content);
        if (mainFragment == null) {
            mainFragment = new RssFeedListFragment();
            fragmentManager.beginTransaction().add(R.id.fl_main_content, mainFragment).commit();
        }
    }

    @Override
    protected void onPause() {
        if(isFinishing()) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().remove(fragmentManager.findFragmentById(R.id.fl_main_content)).commit();
        }
        super.onPause();
    }
}
