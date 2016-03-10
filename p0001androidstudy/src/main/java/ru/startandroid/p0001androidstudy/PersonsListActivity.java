package ru.startandroid.p0001androidstudy;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Toast;

import java.util.List;

import ru.startandroid.p0001androidstudy.model.Person;
import utilities.Utilities;

/**
 * @author by Andrew on 2/4/2016.
 */

public class PersonsListActivity extends ListActivity<Person> {

    private final static String TAG = PersonsListActivity.class.getSimpleName();

    @Override
    protected void initListView() {
        super.initListView();

        itemsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Person selectedPerson = (Person) parent.getAdapter().getItem(position);
                handleOnAddNewEntry(selectedPerson);
                itemsList.setItemChecked(position, true);
            }
        });
        itemsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Person selectedPerson = (Person) parent.getAdapter().getItem(position);

                PopupMenuFragment popupMenuFragment = new PopupMenuFragment();
                popupMenuFragment.show(selectedPerson, personListener, getSupportFragmentManager(), "popupMenuFragment");
                return true;
            }
        });
    }

    @NonNull
    @Override
    protected List<Person> filterByCondition(String query) {
        return getTestApplication().getPersonDao().searchPersons(query);
    }

    @NonNull
    @Override
    protected List<Person> getAllData() {
        long start = System.currentTimeMillis();
        List<Person> allPersons = getTestApplication().getPersonDao().getAllPersons();
        long end = System.currentTimeMillis();
        Utilities.logD(TAG, "Get_ all persons: " + (end - start));
        return allPersons;
    }

    @Override
    protected BaseAdapter getAdapter(List<Person> data) {
        return new PersonAdapter(getApplicationContext(), data);
    }

    @Override
    protected void handleOnAddNewEntry(@Nullable Person person) {
        PersonDetailsFragment stateDetailsDialogue = new PersonDetailsFragment();
        stateDetailsDialogue.setCancelable(false);
        if (person == null) {
            stateDetailsDialogue.show(new Person(), personListener, getSupportFragmentManager(), "stateDetailsDialogue");
        } else {
            stateDetailsDialogue.show(person, personListener, getSupportFragmentManager(), "stateDetailsDialogue");
        }
    }

    private PersonAdapter getPersonAdapter() {
        return (PersonAdapter) adapter;
    }

    private final PersonListener personListener = new PersonListener() {
        @Override
        public void onPersonUpdated(Person person) {
            int position = (int) getPersonAdapter().getPosition(person);
            getTestApplication().getPersonDao().save(person);
            getPersonAdapter().update(person);
            getPersonAdapter().refreshPersons(getTestApplication().getPersonDao().getAllPersons());
            if (position == -1) {
                itemsList.smoothScrollToPosition(adapter.getCount() - 1);
            }
            Toast.makeText(getApplicationContext(), "Position: " + position, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPersonDelete(Person person) {
            getTestApplication().getPersonDao().remove(person);
            getPersonAdapter().refreshPersons(getTestApplication().getPersonDao().getAllPersons());
        }
    };
}
