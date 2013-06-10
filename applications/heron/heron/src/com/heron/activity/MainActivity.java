package com.heron.activity;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import android.app.ActionBar.LayoutParams;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.heron.HeronApplication;
import com.heron.R;
import com.heron.dagger.MainActivityModule;
import com.heron.provider.TelematicMember;

import dagger.ObjectGraph;

public class MainActivity extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private ObjectGraph activityGraph;

    @Inject
    CursorAdapter mAdapter;

    static final String[] PROJECTION = new String[] { TelematicMember.Members._ID, TelematicMember.Members.EMAIL };

    static final String SELECTION = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_list);
        HeronApplication application = (HeronApplication) getApplication();
        activityGraph = application.getApplicationGraph().plus(getModules().toArray());
        activityGraph.inject(this);
        
        ProgressBar progressBar = new ProgressBar(this);
        progressBar.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
                Gravity.CENTER));
        progressBar.setIndeterminate(true);
        getListView().setEmptyView(progressBar);

        ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
        root.addView(progressBar);
        setListAdapter(mAdapter);

        getLoaderManager().initLoader(0, null, this);
    }
    
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent i = new Intent(this, EditMemberActivity.class);
        i.putExtra("RowId", id);
        startActivity(i);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, TelematicMember.Members.MEMBERS_URI, PROJECTION, SELECTION, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> arg0) {
        mAdapter.swapCursor(null);
    }

    protected List<Object> getModules() {
        return Arrays.<Object> asList(new MainActivityModule(this));
    }
}
