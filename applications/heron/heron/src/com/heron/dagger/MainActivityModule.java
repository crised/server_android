package com.heron.dagger;

import javax.inject.Singleton;

import android.content.Context;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;

import com.heron.MainActivity;
import com.heron.provider.TelematicMember;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = {
                MainActivity.class
        },
        complete = false,
        library = true)
public class MainActivityModule {
    private final MainActivity mainActivity;
    
    public MainActivityModule(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }
    
    @Provides
    @Singleton
    @ForActivity
    Context provideActivityConetxt() {
        return mainActivity;
    }
    
    @Provides
    @Singleton
    CursorAdapter provideListAdapter() {
        String[] fromColumns = { TelematicMember.Members.EMAIL };
        int[] toViews = { android.R.id.text1 };
        return new SimpleCursorAdapter(mainActivity, android.R.layout.simple_list_item_1, null, fromColumns, toViews, 0);
    }
}
