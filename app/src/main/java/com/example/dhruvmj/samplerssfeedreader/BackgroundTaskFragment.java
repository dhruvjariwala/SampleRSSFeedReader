package com.example.dhruvmj.samplerssfeedreader;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;

/**
 * @author Dhruv Jariwala
 */

@SuppressWarnings("unchecked")
public abstract class BackgroundTaskFragment<T, U> extends Fragment {
    protected void startBackgroundTask() {
        new BackgroundTask().execute();
    }

    protected abstract void onBackgroundTaskStarted();

    protected abstract U doBackgroundTask();

    protected abstract void processBackgroundTask(U data);

    private class BackgroundTask extends AsyncTask<T, U, U> {

        @Override
        protected void onPreExecute() {
            onBackgroundTaskStarted();
        }

        @Override
        protected U doInBackground(T... params) {
            return doBackgroundTask();
        }

        @Override
        protected void onPostExecute(U data) {
            processBackgroundTask(data);
        }
    }
}
