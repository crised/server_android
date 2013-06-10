package com.heron.activity;

import java.util.Arrays;
import java.util.List;

import android.content.ContentUris;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.heron.Heron;
import com.heron.R;
import com.heron.dagger.BaseActivity;
import com.heron.dagger.EditMemberModule;
import com.heron.model.Member;
import com.heron.provider.TelematicMember;

public class EditMemberActivity extends BaseActivity {

    private long id;
    private EditText mNameEditText;
    private EditText mEmailEditText;
    private EditText mPhoneNumberText;
    private Button mInsertButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_member);
        mNameEditText = (EditText) findViewById(R.id.name_edit);
        mEmailEditText = (EditText) findViewById(R.id.email_edit);
        mPhoneNumberText = (EditText) findViewById(R.id.phoneNumber_edit);
        mInsertButton = (Button) findViewById(R.id.save_member_button);
        mInsertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insert();
            }
        });
        if(getIntent() != null) {
            Bundle extras = getIntent().getExtras();
            id = extras != null ? extras.getLong("RowId") : -1L;
        }
    }

    private void insert() {
        Member member =
                new Member(mNameEditText.getText().toString(), mEmailEditText.getText().toString(), mPhoneNumberText
                        .getText().toString());
        Log.d(Heron.LOG_TAG, "inserting: " + member);
        if(id == -1) { // new user
            getContentResolver().insert(TelematicMember.Members.MEMBERS_URI, member.asContentValues());
        } else {
            Uri memberUri = ContentUris.withAppendedId(TelematicMember.Members.MEMBERS_URI, 0L);
            getContentResolver().update(memberUri, member.asContentValues(), null,
                    null);
        }
        mNameEditText.setText("");
        mEmailEditText.setText("");
        mPhoneNumberText.setText("");
    }

    @Override
    protected List<Object> getModules() {
        return Arrays.<Object> asList(new EditMemberModule(this));
    }
}
