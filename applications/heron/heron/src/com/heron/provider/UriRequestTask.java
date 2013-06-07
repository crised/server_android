package com.heron.provider;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;

import com.heron.Heron;

import android.content.Context;
import android.net.Uri;
import android.util.Log;


public class UriRequestTask implements Runnable {
    private HttpUriRequest mRequest;
    private ResponseHandler mHandler;

    protected Context mAppContext;

    private RESTfulContentProvider mSiteProvider;
    private String mRequestTag;
    
    private int mRawResponse = -1;
//    private int mRawResponse = R.raw.map_src;

    public UriRequestTask(HttpUriRequest request,
                          ResponseHandler handler, Context appContext)
    {
        this(null, null, request, handler, appContext);
    }
                                          
    public UriRequestTask(String requestTag,
                          RESTfulContentProvider siteProvider,
                          HttpUriRequest request,
                          ResponseHandler handler, Context appContext)
    {
        mRequestTag = requestTag;
        mSiteProvider = siteProvider;
        mRequest = request;
        mHandler = handler;
        mAppContext = appContext;
    }

    public void setRawResponse(int rawResponse) {
        mRawResponse = rawResponse;
    }

    /**
     * Carries out the request on the complete URI as indicated by the protocol,
     * host, and port contained in the configuration, and the URI supplied to
     * the constructor.
     */
    public void run() {
        HttpResponse response;

        try {
            response = execute(mRequest);
            mHandler.handleResponse(response, getUri());
        } catch (IOException e) {
            Log.w(Heron.LOG_TAG, "exception processing asynch request", e);
        } finally {
            if (mSiteProvider != null) {
                mSiteProvider.requestComplete(mRequestTag);
            }
        }
    }

    private HttpResponse execute(HttpUriRequest mRequest) throws IOException {
        if (mRawResponse >= 0) {
            return new RawResponse(mAppContext, mRawResponse);
        } else {
            HttpClient client = new DefaultHttpClient();
            return client.execute(mRequest);
        }
    }

    public Uri getUri() {
        return Uri.parse(mRequest.getURI().toString());
    }
}
