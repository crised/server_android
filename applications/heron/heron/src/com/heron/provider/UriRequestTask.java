package com.heron.provider;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import android.content.ContentResolver;
import android.content.Context;
import android.util.Log;

import com.heron.Heron;

public class UriRequestTask implements Runnable {
    
    private String mRequestTag;
    private String urlS;
    private Map<String, UriRequestTask> mRequestsInProgress;
    private ContentResolver cr;

    public UriRequestTask(String requestTag, String url, Context context,Map<String, UriRequestTask> mRequestsInProgress,
            ContentResolver cr) {
        mRequestTag = requestTag;
        this.urlS = url;
        this.mRequestsInProgress = mRequestsInProgress;
        this.cr = cr;
    }

    /**
     * Carries out the request on the complete URI as indicated by the protocol, host, and port contained in the
     * configuration, and the URI supplied to the constructor.
     */
    public void run() {
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(urlS);
            ResponseHandler mHandler = new TelematicHandler(cr);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            mHandler.handleResponse(in);
        } catch (MalformedURLException e) {
            Log.e(Heron.LOG_TAG, "Malformed url: " + urlS, e);
        } catch (IOException e) {
            Log.w(Heron.LOG_TAG, "exception processing asynch request", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            synchronized (mRequestsInProgress) {
                mRequestsInProgress.remove(mRequestTag);
            }
        }
    }
}
