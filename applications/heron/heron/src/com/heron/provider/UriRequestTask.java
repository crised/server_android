package com.heron.provider;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.util.Log;

import com.heron.Heron;

public class UriRequestTask implements Runnable {
    public enum Method {
        GET, PUT, DELETE, POST
    }

    private ResponseHandler mHandler;
    private URL url;
    private Method mMethod;
    private String mData;

    protected Context mAppContext;

    private RESTfulContentProvider mSiteProvider;
    private String mRequestTag;

    public UriRequestTask(String requestTag, RESTfulContentProvider siteProvider, URL url, ResponseHandler handler,
            Context appContext) {
        this(requestTag, siteProvider, url, handler, appContext, Method.GET, null);
    }

    public UriRequestTask(String requestTag, RESTfulContentProvider siteProvider, URL url, ResponseHandler handler,
            Context appContext, Method method, String data) {
        mRequestTag = requestTag;
        mSiteProvider = siteProvider;
        this.url = url;
        mHandler = handler;
        mAppContext = appContext;
        mMethod = method;
        mData = data;
    }

    private void writeStream(HttpURLConnection urlConnection) throws IOException {
        OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
        out.write(mData.getBytes());
    }
    
    public void run() {
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            switch (mMethod) {
            case GET://do nothing
                break;
            case PUT:
                Log.v(Heron.LOG_TAG, "Sending PUT request: " + mData);
                urlConnection.setDoInput(true);
                urlConnection.setFixedLengthStreamingMode(mData.length());
                urlConnection.setRequestMethod(Method.PUT.name());
                writeStream(urlConnection);
                break;
            case POST:
                Log.v(Heron.LOG_TAG, "Sending POST request: " + mData);
                urlConnection.setDoInput(true);
                urlConnection.setFixedLengthStreamingMode(mData.length());
                urlConnection.setRequestMethod(Method.POST.name());
                writeStream(urlConnection);
                break;
            case DELETE:
                urlConnection.setRequestMethod(Method.DELETE.name());
                break;
            default:
                throw new IllegalStateException("Unknown method: " + mMethod);
            }
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            mHandler.handleResponse(in);
        } catch (IOException e) {
            Log.w(Heron.LOG_TAG, "exception processing asynch request", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (mSiteProvider != null) {
                mSiteProvider.requestComplete(mRequestTag);
            }
        }
    }
}
