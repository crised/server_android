package com.heron.provider;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.heron.Heron;
import com.heron.service.TelematicService;

public abstract class RESTfulContentProvider extends ContentProvider {

    public abstract Uri insert(Uri uri, ContentValues cv, SQLiteDatabase db);

    public abstract SQLiteDatabase getDatabase();

    /**
     * Creates a new worker thread to carry out a RESTful network invocation.
     * 
     * @param queryTag
     *            unique tag that identifies this request.
     * 
     * @param queryUri
     *            the complete URI that should be access by this request.
     */
    public void asyncQueryRequest(String queryTag, String queryUri) {
        Log.i(Heron.LOG_TAG, "Starting telematic service async");
        Intent intent = new Intent(getContext(), TelematicService.class);
        intent.putExtra(TelematicService.QUERY_TEXT, queryTag);
        intent.putExtra(TelematicService.URL, queryUri);
        getContext().startService(intent);

    }

    public static String encode(String gDataQuery) {
        try {
            return URLEncoder.encode(gDataQuery, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.d(Heron.LOG_TAG, "could not decode UTF-8," + " this should not happen");
        }
        return null;
    }
}
