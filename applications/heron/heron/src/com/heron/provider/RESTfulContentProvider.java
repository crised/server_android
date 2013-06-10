package com.heron.provider;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.heron.Heron;
import com.heron.provider.UriRequestTask.Method;


public abstract class RESTfulContentProvider extends ContentProvider {
    private Map<String, UriRequestTask> mRequestsInProgress =
            new HashMap<String, UriRequestTask>();

    public abstract Uri insert(Uri uri, ContentValues cv, SQLiteDatabase db);    

    private UriRequestTask getRequestTask(String queryText) {
        return mRequestsInProgress.get(queryText);
    }

    public void requestComplete(String mQueryText) {
        synchronized (mRequestsInProgress) {
            mRequestsInProgress.remove(mQueryText);
        }
    }

    /**
     * Abstract method that allows a subclass to define the type of handler
     * that should be used to parse the response of a given request.
     *
     * @param requestTag unique tag identifying this request.
     * @return The response handler created by a subclass used to parse the
     * request response.
     */
    protected abstract ResponseHandler newResponseHandler(String requestTag);
    public abstract SQLiteDatabase getDatabase();
    
    UriRequestTask newQueryTask(String requestTag, URL url, Method method, String data) {
        UriRequestTask requestTask;
        ResponseHandler handler = newResponseHandler(requestTag);
        requestTask = new UriRequestTask(requestTag, this, url, handler, getContext(), method, data);
        mRequestsInProgress.put(requestTag, requestTask);
        return requestTask;
    }

    /**
     * Creates a new worker thread to carry out a RESTful network invocation.
     *
     * @param queryTag unique tag that identifies this request.
     *
     * @param queryUri the complete URI that should be access by this request.
     */
    public void asyncQueryRequest(String queryTag, URL queryUri, Method method, String data) {
        synchronized (mRequestsInProgress) {
            UriRequestTask requestTask = getRequestTask(queryTag);
            if (requestTask == null) {
                requestTask = newQueryTask(queryTag, queryUri, method, data);
                Thread t = new Thread(requestTask);
                // allows other requests to run in parallel.
                t.start();
            }
        }
    }

    public static String encode(String gDataQuery) {
        try {
            return URLEncoder.encode(gDataQuery, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.d(Heron.LOG_TAG, "could not decode UTF-8," +
                    " this should not happen");
        }
        return null;
    }
}
