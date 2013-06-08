package com.heron.dagger;

import javax.inject.Singleton;

import android.content.Context;

import com.heron.HeronApplication;

import dagger.Module;
import dagger.Provides;

@Module(library = true)
public class AndroidModule {
    private final HeronApplication application;
    
    public AndroidModule(HeronApplication application) {
        this.application = application;
    }
    
    @Provides 
    @Singleton 
    @ForApplication 
    Context provideApplicationContext() {
        return application;
    }
}
