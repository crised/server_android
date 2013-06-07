package cl.telematic.heron.provider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Collection;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import cl.telematic.heron.Heron;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class TelematicHandler implements ResponseHandler {
    private static final String FLUSH_TIME = "5 minutes";

    private RESTfulContentProvider telematicContentProvider;

    public TelematicHandler(RESTfulContentProvider restfulProvider, String queryText) {
        this.telematicContentProvider = restfulProvider;
    }

    @Override
    public void handleResponse(HttpResponse response, Uri uri) throws IOException {
        try {
           int newCount = parseTelematicEntity(response.getEntity());
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
    
    private int parseTelematicEntity(HttpEntity entity) throws IOException {
        InputStream telematicContent = entity.getContent();
        BufferedReader r = new BufferedReader(new InputStreamReader(telematicContent));
        StringBuilder total = new StringBuilder();
        String line;
        while ((line = r.readLine()) != null) {
            total.append(line);
        }
        String membersS = total.toString();
        Gson gson = new Gson();
        Type collectionType = new TypeToken<Collection<Member>>(){}.getType();
        Collection<Member> members = gson.fromJson(membersS, collectionType);
        ContentValues memberEntry;
        for(Member member: members) {
            memberEntry = new ContentValues();
            memberEntry.put(TelematicMember.Members.MEMBER_ID, member.id);
            memberEntry.put(TelematicMember.Members.MEMBER_NAME, member.name);
            memberEntry.put(TelematicMember.Members.EMAIL, member.email);
            memberEntry.put(TelematicMember.Members.PHONE_NUMBER, member.phoneNumber);
            SQLiteDatabase db = telematicContentProvider.getDatabase();
            telematicContentProvider.insert(TelematicMember.Members.MEMBERS_URI, memberEntry, db);
        }
        return members.size();
    }
    
    private static class Member {
        private int id;
        private String name;
        private String email;
        private String phoneNumber;
    }
}
