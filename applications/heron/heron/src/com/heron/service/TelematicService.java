package com.heron.service;

import java.util.HashMap;
import java.util.Map;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.heron.Heron;
import com.heron.provider.UriRequestTask;

public class TelematicService extends Service {
    private Map<String, UriRequestTask> mRequestsInProgress = new HashMap<String, UriRequestTask>();
    public static final String QUERY_TEXT = "queryText";
    public static final String URL = "url";

    public void requestComplete(String mQueryText) {
        synchronized (mRequestsInProgress) {
            mRequestsInProgress.remove(mQueryText);
        }
    }

    @Override
    public void onCreate() {
        Log.i(Heron.LOG_TAG, "Creatign Telematic service.");
    }

    private UriRequestTask getRequestTask(String queryText) {
        return mRequestsInProgress.get(queryText);
    }

    UriRequestTask newQueryTask(String requestTag, String url) {
        UriRequestTask requestTask;
        requestTask = new UriRequestTask(requestTag, url, getApplicationContext(), mRequestsInProgress,
                getContentResolver());
        mRequestsInProgress.put(requestTag, requestTask);
        return requestTask;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        synchronized (mRequestsInProgress) {
            String queryTag = intent.getStringExtra(QUERY_TEXT);
            String url = intent.getStringExtra(URL);
            Log.i(Heron.LOG_TAG, "Runnign query tag: " + queryTag + ", for uri: " + url);
            UriRequestTask requestTask = getRequestTask(queryTag);
            if (requestTask == null) {
                requestTask = newQueryTask(queryTag, url);
                Thread t = new Thread(requestTask);
                // allows other requests to run in parallel.
                t.start();
            }
        }
        stopSelf();
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(Heron.LOG_TAG, "Binding not implemented");
        return null;
    }

}
