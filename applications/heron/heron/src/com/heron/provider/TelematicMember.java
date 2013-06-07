package com.heron.provider;

import android.net.Uri;
import android.provider.BaseColumns;

public class TelematicMember {
    public static final String AUTHORITY = "cl.telematic.heron.provider.telematic";
    public static final int ID_COLUMN = 0;
    
    public static final class Members implements BaseColumns {
        public static final String TYPE = "vn.android.cursor.dir/vnd.cl.telematic.heron.provider.telematic.members";
        private Members(){};
        public static final String NAME = "members";
        public static final Uri MEMBERS_URI = Uri.parse("content://" + AUTHORITY + "/" + NAME);
        
        public static final String MEMBER_NAME = "name";
        public static final String EMAIL = "email";
        public static final String PHONE_NUMBER = "phoneNumber";
        public static final String TIMESTAMP = "timestamp";
        public static final String MEMBER_ID = "memberId";
    }
}
