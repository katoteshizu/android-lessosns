package ru.startandroid.p0001androidstudy;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import ru.startandroid.p0001androidstudy.model.Person;
import utilities.Utilities;

/**
 * Created by Work on 2/4/2016.
 */
public class PersonsListActivity extends AppActivity {

    private final static String TAG = PersonsListActivity.class.getSimpleName();

    private PersonAdapter personAdapter;

    protected ListView itemsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listitems);

        itemsList = (ListView) findViewById(R.id.itemsList);

        long start = System.currentTimeMillis();
        List<Person> allPersons = getTestApplication().getPersonDao().getAllPersons();
        long end = System.currentTimeMillis();
        Utilities.logD(TAG, "Get_ all persons: " + (end - start));
        personAdapter = new PersonAdapter(this, allPersons);
        itemsList.setAdapter(personAdapter);

        itemsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Person selectedPerson = (Person) parent.getAdapter().getItem(position);

                PersonDetailsFragment stateDetailsDialogue = new PersonDetailsFragment();

                stateDetailsDialogue.show(selectedPerson, personListener, getSupportFragmentManager(), "stateDetailsDialogue");
            }
        });
        itemsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "Long press at: " + position + " id: " + id, Toast.LENGTH_SHORT).show();
                Person selectedPerson = (Person) parent.getAdapter().getItem(position);

                PopupMenuFragment popupMenuFragment = new PopupMenuFragment();
                popupMenuFragment.show(selectedPerson, personListener, getSupportFragmentManager(), "popupMenuFragment");
                return true;
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbarmenu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.it_add:
                PersonDetailsFragment stateDetailsDialogue = new PersonDetailsFragment();
                stateDetailsDialogue.show(new Person(), personListener, getSupportFragmentManager(), "stateDetailsDialogue");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private final PersonListener personListener = new PersonListener() {
        @Override
        public void onPersonUpdated(Person person) {
            int position = (int) personAdapter.getPosition(person);
            getTestApplication().getPersonDao().save(person);
            personAdapter.update(person);
            personAdapter.refreshPersons(getTestApplication().getPersonDao().getAllPersons());
            if (position == -1) {
                itemsList.smoothScrollToPosition(personAdapter.getCount() - 1);
            }
            Toast.makeText(getApplicationContext(), "Position: " + position, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPersonDelete(Person person) {
            getTestApplication().getPersonDao().remove(person);
            personAdapter.refreshPersons(getTestApplication().getPersonDao().getAllPersons());
        }
    };
}
