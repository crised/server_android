package com.heron.dagger;

import javax.inject.Singleton;

import android.app.Activity;
import android.content.Context;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;

import com.heron.MembersListFragment;
import com.heron.provider.TelematicMember;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = {
                MembersListFragment.class
        },
        complete = false,
        library = true)
public class MembersListModule {
    private final Activity activity;
    
    public MembersListModule(Activity activity) {
        this.activity = activity;
    }
    
    @Provides
    @Singleton
    @ForActivity
    Context provideActivityConetxt() {
        return activity;
    }
    
    @Provides
    @Singleton
    CursorAdapter provideListAdapter() {
        String[] fromColumns = { TelematicMember.Members.EMAIL };
        int[] toViews = { android.R.id.text1 };
        return new SimpleCursorAdapter(activity, android.R.layout.simple_list_item_1, null, fromColumns, toViews, 0);
    }
}
