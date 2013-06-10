package com.heron.provider;

import java.net.MalformedURLException;
import java.net.URL;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;
import android.util.Log;

import com.heron.Heron;
import com.heron.model.Member;
import com.heron.provider.UriRequestTask.Method;

public class TelematicContentProvider extends RESTfulContentProvider {

    private static final String QUERY_URI = "http://23.21.60.18:8080/jboss-as-kitchensink-angularjs/rest/members";
    private static final String DATABASE_NAME = "member.db";
    private static final int DATABASE_VERSION = 1;
    private static final String MEMBERS_TABLE_NAME = "member";
    private static final int MEMBERS = 1;
    private static final int MEMBER_ID = 2;
    private static UriMatcher sUriMatcher;
    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(TelematicMember.AUTHORITY, TelematicMember.Members.NAME, MEMBERS);
        sUriMatcher.addURI(TelematicMember.AUTHORITY, TelematicMember.Members.NAME + "/#", MEMBER_ID);
    }

    private DatabaseHelper mOpenHelper;
    private ContentResolver mContentResolver;
    private SQLiteDatabase mDb;

    private static class DatabaseHelper extends SQLiteOpenHelper {
        private DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
            super(context, name, factory, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqlLiteDatabase) {
            createTable(sqlLiteDatabase);
        }

        private void createTable(SQLiteDatabase sqLiteDatabase) {
            String createvideoTable =
                    "CREATE TABLE " + MEMBERS_TABLE_NAME + " (" + TelematicMember.Members._ID
                            + " INTEGER PRIMARY KEY, " + TelematicMember.Members.MEMBER_NAME + " TEXT, "
                            + TelematicMember.Members.EMAIL + " TEXT, " + TelematicMember.Members.MEMBER_ID
                            + " INTEGER, " + TelematicMember.Members.TIMESTAMP + " TEXT, "
                            + TelematicMember.Members.PHONE_NUMBER + " TEXT" + ");";
            sqLiteDatabase.execSQL(createvideoTable);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int arg1, int arg2) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MEMBERS_TABLE_NAME + ";");
            createTable(sqLiteDatabase);
        }

    }

    @Override
    public boolean onCreate() {
        Context ctx = getContext();
        mContentResolver = ctx.getContentResolver();
        mOpenHelper = new DatabaseHelper(ctx, DATABASE_NAME, null);
        mDb = mOpenHelper.getWritableDatabase();
        return true;
    }

    public SQLiteDatabase getDatabase() {
        return mDb;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String where, String[] whereArgs, String sortOrder) {
        Cursor queryCursor;
        int match = sUriMatcher.match(uri);
        switch (match) {
        case MEMBERS:
            queryCursor = mDb.query(MEMBERS_TABLE_NAME, projection, where, whereArgs, null, null, sortOrder);
            queryCursor.setNotificationUri(mContentResolver, uri);
            try {
                asyncQueryRequest("members", new URL(QUERY_URI), Method.GET, null);
            } catch (MalformedURLException e) {
                Log.w(Heron.LOG_TAG, "url malformed: " + QUERY_URI);
            }
            break;
        case MEMBER_ID:
            long memberID = ContentUris.parseId(uri);
            queryCursor =
                    mDb.query(MEMBERS_TABLE_NAME, projection, TelematicMember.Members._ID + " = " + memberID,
                            whereArgs, null, null, null);
            break;
        default:
            throw new IllegalArgumentException("unsupported uri: " + QUERY_URI);
        }
        return queryCursor;
    }

    @Override
    public String getType(Uri uri) {
        return TelematicMember.Members.TYPE;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = getDatabase();
        return insert(uri, values, db);
    }

    @Override
    public Uri insert(Uri uri, ContentValues values, SQLiteDatabase db) {
        if (!values.containsKey(TelematicMember.Members.TIMESTAMP)) {
            Long now = System.currentTimeMillis();
            values.put(TelematicMember.Members.TIMESTAMP, now);
        }
        switch (sUriMatcher.match(uri)) {
        case MEMBERS:
            // TODO update values instead of dump/recreate
            long rowId = db.insert(MEMBERS_TABLE_NAME, TelematicMember.Members.NAME, values);
            if (rowId >= 0) {
                Uri insertUri = ContentUris.withAppendedId(TelematicMember.Members.MEMBERS_URI, rowId);
                mContentResolver.notifyChange(insertUri, null);
                return insertUri;
            } else {
                throw new IllegalStateException("could not insert " + "content values: " + values);
            }
        case MEMBER_ID:
            long id = ContentUris.parseId(uri);
            long _rowId = db.insert(MEMBERS_TABLE_NAME, TelematicMember.Members.NAME, values);
            try {
                asyncQueryRequest("insert" + id, new URL(QUERY_URI + "/" + id), Method.PUT, Member
                        .memberFromContentValues(values).asJson());
            } catch (MalformedURLException e) {
                Log.w(Heron.LOG_TAG, "malformed url: " + e);
            }
            Uri insertUri = ContentUris.withAppendedId(TelematicMember.Members.MEMBERS_URI, _rowId);
            mContentResolver.notifyChange(insertUri, null);
            return insertUri;
        default:
            throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public int delete(Uri uri, String where, String[] whereArgs) {
        int match = sUriMatcher.match(uri);
        switch (match) {
        case MEMBER_ID:
            long id = ContentUris.parseId(uri);
            try {
                asyncQueryRequest("delete" + id, new URL(QUERY_URI + "/" + id), Method.DELETE, null);
            } catch (MalformedURLException e) {
                Log.w(Heron.LOG_TAG, "malformedUrl: " + e);
            }
            break;
        default:
            throw new IllegalArgumentException("unknows uri: " + uri);

        }
        return 1;// we don't know yet
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        getContext().getContentResolver().notifyChange(uri, null);
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int count;
        switch (sUriMatcher.match(uri)) {
        case MEMBERS:
            count = db.update(MEMBERS_TABLE_NAME, values, selection, selectionArgs);
            break;
        case MEMBER_ID:
            String memberId = uri.getPathSegments().get(1);
            count =
                    db.update(MEMBERS_TABLE_NAME, values,
                            BaseColumns._ID + " = " + memberId
                                    + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
            try {
                Log.d(Heron.LOG_TAG, "sending async request to update: " + Member.memberFromContentValues(values));
                asyncQueryRequest("update" + memberId, new URL(QUERY_URI + "/" + memberId), Method.POST, Member
                        .memberFromContentValues(values).asJson());
            } catch (MalformedURLException e) {
                Log.w(Heron.LOG_TAG, "malformed url: " + e);
            }
            break;
        default:
            throw new IllegalStateException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    protected ResponseHandler newResponseHandler(String requestTag) {
        return new TelematicHandler(this, requestTag);
    }

}
