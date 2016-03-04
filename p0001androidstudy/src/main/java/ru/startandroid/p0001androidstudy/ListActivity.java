package ru.startandroid.p0001androidstudy;

import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.LinkedList;
import java.util.List;

import ru.startandroid.p0001androidstudy.bitmap.BitmapUtility;

/**
 * @author by Andrew on 2/29/2016.
 */
public abstract class ListActivity<P> extends AppActivity {

    protected BaseAdapter adapter;

    protected ListView itemsList;
    protected MenuItem clearSearch;
    protected MenuItem addAction;
    protected MenuItem searchAction;

    @CallSuper
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        alignForOrientation(getResources().getConfiguration().orientation);
    }

    protected void initListView() {
        itemsList = (ListView) findViewById(R.id.itemsList);
        adapter = getAdapter(getAllData());
        itemsList.setAdapter(adapter);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        alignForOrientation(newConfig.orientation);
    }

    private void alignForOrientation(int screenOrientation) {

        if (screenOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            View rootView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.listitems_land, null);
            FrameLayout leftContainer = (FrameLayout) rootView.findViewById(R.id.left_panel_container);
            FrameLayout rightContainer = (FrameLayout) rootView.findViewById(R.id.right_panel_container);

            Point displaySize = BitmapUtility.getScreenSize(this);
            leftContainer.setLayoutParams(new LinearLayout.LayoutParams(
                    displaySize.x / 3,
                    ViewGroup.LayoutParams.MATCH_PARENT));

            rightContainer.setLayoutParams(new LinearLayout.LayoutParams(
                    2 * displaySize.x / 3,
                    ViewGroup.LayoutParams.MATCH_PARENT
            ));

            View listLayout = LayoutInflater.from(getApplicationContext()).inflate(R.layout.listitems, null);
            View detailsLayout = LayoutInflater.from(getApplicationContext()).inflate(R.layout.details_dialogue, null);
            leftContainer.addView(listLayout);
            rightContainer.addView(detailsLayout);
            setContentView(rootView);
        } else if (screenOrientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.listitems);
        }

        initListView();
    }

    @CallSuper
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.actionbarmenu, menu);

        searchAction = menu.findItem(R.id.action_search);
        clearSearch = menu.findItem(R.id.action_clear_search);
        clearSearch.setVisible(false);
        addAction = menu.findItem(R.id.it_add);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchAction);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!searchView.isIconified()) {
                    searchView.setIconified(true);
                }
                searchAction.collapseActionView();
                List<P> foundItems = filterByCondition(query);
                adapter = getAdapter(foundItems);
                itemsList.setAdapter(adapter);
                clearSearch.setVisible(true);
                addAction.setVisible(false);
                searchAction.setVisible(false);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.it_add:
                handleOnAddNewEntry(null);
                return true;
            case R.id.action_clear_search:
                List<P> allData = getAllData();
                adapter = getAdapter(allData);
                itemsList.setAdapter(adapter);
                clearSearch.setVisible(false);
                addAction.setVisible(true);
                searchAction.setVisible(true);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected abstract void handleOnAddNewEntry(P entity);

    protected List<P> getAllData() {
        return new LinkedList<>();
    }

    protected List<P> filterByCondition(String query) {
        return new LinkedList<>();
    }

    protected abstract BaseAdapter getAdapter(List<P> data);
}
