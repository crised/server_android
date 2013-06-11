package com.heron.provider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Collection;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.heron.Heron;

public class TelematicHandler implements ResponseHandler {
    private static final String FLUSH_TIME = "5 minutes";
    private ContentResolver cr;

    @Override
    public void handleResponse(InputStream is) throws IOException {
        try {
            int newCount = parseTelematicEntity(is);
            if (newCount > 0) {
                deleteOld();
            }
        } catch (IOException e) {
            //
        }
    }

    public TelematicHandler(ContentResolver cr) {
        this.cr = cr;
    }

    private void deleteOld() {
        Cursor old = null;
        try {
            SQLiteDatabase db = TelematicContentProvider.getDB();
            old =
                    db.query("member", null, "member." + TelematicMember.Members.TIMESTAMP
                            + " < strftime('%s', 'now', '-" + FLUSH_TIME + "')", null, null, null, null);
            int c = old.getCount();
            if (old.getCount() > 0) {
                StringBuffer sb = new StringBuffer();
                boolean next;
                if (old.moveToNext()) {
                    do {
                        String ID = old.getString(TelematicMember.ID_COLUMN);
                        sb.append(TelematicMember.Members._ID);
                        sb.append("=");
                        sb.append(ID);
                        next = old.moveToNext();
                        if (next) {
                            sb.append(" OR ");
                        }
                    } while (next);
                }
                String where = sb.toString();
                db.delete("member", where, null);
                Log.d(Heron.LOG_TAG, "flushed old query results: " + c);
            }
        } finally {
            if (old != null) {
                old.close();
            }
        }
    }

    private int parseTelematicEntity(InputStream is) throws IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(is));
        StringBuilder total = new StringBuilder();
        String line;
        while ((line = r.readLine()) != null) {
            total.append(line);
        }
        String membersS = total.toString();
        Gson gson = new Gson();
        Type collectionType = new TypeToken<Collection<Member>>() {
        }.getType();
        Collection<Member> members = gson.fromJson(membersS, collectionType);
        ContentValues memberEntry;
        for (Member member : members) {
            memberEntry = new ContentValues();
            memberEntry.put(TelematicMember.Members.MEMBER_ID, member.id);
            memberEntry.put(TelematicMember.Members.MEMBER_NAME, member.name);
            memberEntry.put(TelematicMember.Members.EMAIL, member.email);
            memberEntry.put(TelematicMember.Members.PHONE_NUMBER, member.phoneNumber);
            if (notInserted(member.email)) {
                cr.insert(TelematicMember.Members.MEMBERS_URI, memberEntry);
            }
        }
        return members.size();
    }

    private boolean notInserted(String email) {
        //Ideally, we would only fetch member that have changed since a timestamp
        //for now, we don't update the user if we already got it.
        SQLiteDatabase db = TelematicContentProvider.getDB();
        Cursor cursor =
                db.query("member", null, "email" + " = '" + email + "'",
                        null, null, null, null);
        return cursor.getCount() == 0;
    }

    private static class Member {
        private int id;
        private String name;
        private String email;
        private String phoneNumber;
    }
}
