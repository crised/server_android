package com.heron;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

import com.heron.dagger.MembersListModule;
import com.heron.provider.TelematicMember;

import dagger.ObjectGraph;

public class MembersListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private ObjectGraph activityGraph;

    @Inject
    CursorAdapter mAdapter;

    @Inject
    Context app;

    static final String[] PROJECTION = new String[] { TelematicMember.Members._ID, TelematicMember.Members.EMAIL };

    static final String SELECTION = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HeronApplication application = (HeronApplication) app;
        activityGraph = application.getApplicationGraph().plus(getModules().toArray());
        activityGraph.inject(this);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        setListAdapter(mAdapter);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), TelematicMember.Members.MEMBERS_URI, PROJECTION, SELECTION, null, null);
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
        return Arrays.<Object> asList(new MembersListModule(getActivity()));
    }
}
