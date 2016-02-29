package ru.startandroid.p0001androidstudy;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;

/**
 * @authored by Andrew on 2/29/2016.
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

        setContentView(R.layout.listitems);
        itemsList = (ListView) findViewById(R.id.itemsList);
        adapter = getAdapter(getAllData());
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
//                Toast.makeText(getApplicationContext(), "Search query onQueryTextSubmit: " + query, Toast.LENGTH_SHORT).show();
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
//                menu.setGroupVisible(R.id.group_clear_search, true);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Toast.makeText(getApplicationContext(), "Search query onQueryTextChange: " + newText, Toast.LENGTH_SHORT).show();
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
