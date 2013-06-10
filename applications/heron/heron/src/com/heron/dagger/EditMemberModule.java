package com.heron.dagger;


import javax.inject.Singleton;

import android.app.Activity;
import android.content.Context;

import com.heron.activity.EditMemberActivity;
import com.heron.activity.MainActivity;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = {
                MainActivity.class,
                EditMemberActivity.class
        },
        complete = false,
        library = true)
public class EditMemberModule {
    private final Activity activity;
    
    public EditMemberModule(EditMemberActivity activity) {
        this.activity = activity;
    }
    
    @Provides
    @Singleton
    @ForActivity
    Context provideActivityConetxt() {
        return activity;
    }
}
