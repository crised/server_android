package com.heron.provider;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.util.Log;

import com.heron.Heron;

public class UriRequestTask implements Runnable {
    private ResponseHandler mHandler;
    private URL url;

    protected Context mAppContext;

    private RESTfulContentProvider mSiteProvider;
    private String mRequestTag;

    public UriRequestTask(URL url, ResponseHandler handler, Context appContext) {
        this(null, null, url, handler, appContext);
    }

    public UriRequestTask(String requestTag, RESTfulContentProvider siteProvider, URL url, ResponseHandler handler,
            Context appContext) {
        mRequestTag = requestTag;
        mSiteProvider = siteProvider;
        this.url = url;
        mHandler = handler;
        mAppContext = appContext;
    }

    /**
     * Carries out the request on the complete URI as indicated by the protocol, host, and port contained in the
     * configuration, and the URI supplied to the constructor.
     */
    public void run() {
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            mHandler.handleResponse(in);
        } catch (IOException e) {
            Log.w(Heron.LOG_TAG, "exception processing asynch request", e);
        } finally {
            if(urlConnection != null) {
                urlConnection.disconnect();                
            }
            if (mSiteProvider != null) {
                mSiteProvider.requestComplete(mRequestTag);
            }
        }
    }
}
