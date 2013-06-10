package com.heron.provider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.heron.Heron;
import com.heron.model.Member;

public class TelematicHandler implements ResponseHandler {
    private static final String FLUSH_TIME = "5 minutes";

    private RESTfulContentProvider telematicContentProvider;

    public TelematicHandler(RESTfulContentProvider restfulProvider, String queryText) {
        this.telematicContentProvider = restfulProvider;
    }

    @Override
    public void handleResponse(InputStream is) throws IOException {
        try {
           int newCount = parseTelematicEntity(is);
           if(newCount > 0) {
               deleteOld();
           }
        } catch (IOException e) {
            //
        }
    }
    
    private void deleteOld() {
        Cursor old = null;
        try {
            SQLiteDatabase db = telematicContentProvider.getDatabase();
            old = db.query("member", null, "member." + TelematicMember.Members.TIMESTAMP + 
                    " < strftime('%s', 'now', '-" + FLUSH_TIME + "')", null, null, null, null);
            int c = old.getCount();
            if(old.getCount() > 0) {
                StringBuffer sb = new StringBuffer();
                boolean next;
                if(old.moveToNext()) {
                    do {
                       String ID = old.getString(TelematicMember.ID_COLUMN);
                       sb.append(TelematicMember.Members._ID);
                       sb.append("=");
                       sb.append(ID);
                       next = old.moveToNext();
                       if(next) {
                           sb.append(" OR ");
                       }
                    } while(next);
                }
                String where = sb.toString();
                db.delete("member", where, null);
                Log.d(Heron.LOG_TAG, "flushed old query results: " + c);
            }
        } finally {
            if(old != null) {
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
        Collection<Member> members = Member.membersFromJson(membersS);
        for(Member member: members) {
            SQLiteDatabase db = telematicContentProvider.getDatabase();
            telematicContentProvider.insert(TelematicMember.Members.MEMBERS_URI, member.asContentValues(), db);
        }
        return members.size();
    }
}
