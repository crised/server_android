package com.heron.provider;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

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
    private static SQLiteDatabase mDb;

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
    
    public static SQLiteDatabase getDB() {
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
            asyncQueryRequest("members", QUERY_URI);
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
        if (sUriMatcher.match(uri) != MEMBERS) {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }
        // TODO update values instead of dump/recreate
        long rowId = db.insert(MEMBERS_TABLE_NAME, TelematicMember.Members.NAME, values);
        if (rowId >= 0) {
            Uri insertUri = ContentUris.withAppendedId(TelematicMember.Members.MEMBERS_URI, rowId);
            mContentResolver.notifyChange(insertUri, null);
            return insertUri;
        } else {
            throw new IllegalStateException("could not insert " + "content values: " + values);
        }
    }

    @Override
    public int delete(Uri arg0, String arg1, String[] arg2) {
        throw new IllegalStateException();
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new IllegalStateException();
    }

}
