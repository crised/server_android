package com.heron;

import java.util.Arrays;
import java.util.List;

import com.heron.dagger.AndroidModule;

import android.app.Application;
import dagger.ObjectGraph;

public class HeronApplication extends Application {
    private ObjectGraph applicationGraph;
    
    @Override
    public void onCreate() {
        super.onCreate();
        applicationGraph = ObjectGraph.create(getModules().toArray());
    }
    
    protected List<Object> getModules() {
        return Arrays.<Object>asList(new AndroidModule(this));
    }
    
    ObjectGraph getApplicationGraph() {
        return applicationGraph;
    }
}
