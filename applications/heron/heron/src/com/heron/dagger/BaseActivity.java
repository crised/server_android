package com.heron.dagger;

import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;

import com.heron.HeronApplication;

import dagger.ObjectGraph;

public abstract class BaseActivity extends Activity {
    private ObjectGraph activityGraph;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HeronApplication application = (HeronApplication) getApplication();
        activityGraph = application.getApplicationGraph().plus(getModules().toArray());
        activityGraph.inject(this);
      }
    
    protected List<Object> getModules() {
        return Arrays.<Object> asList();
    }
}
