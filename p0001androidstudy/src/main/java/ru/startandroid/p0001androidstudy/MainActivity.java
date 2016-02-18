package ru.startandroid.p0001androidstudy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import utilities.Utilities;

public class MainActivity extends AppActivity {

    private final static String TAG = MainActivity.class.getSimpleName();
    private final static String[] itemNames = {"Addresses", "Persons"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ListView lvMainList = (ListView) findViewById(R.id.mainList);

        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<>(this, R.layout.list_menu, itemNames);

        lvMainList.setAdapter(itemsAdapter);
        lvMainList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Utilities.logD(TAG, "position: " + position + " id: " + id);
//                Toast.makeText(getApplicationContext(), "position: " + position + " id: "  + id, Toast.LENGTH_SHORT).show();
                switch (position) {
                    case 0:
                        Intent addressIntent = new Intent(getApplicationContext(), AddressListActivity.class);
                        startActivity(addressIntent);
                        break;
                    case 1:
                        Intent personIntent = new Intent(getApplicationContext(), PersonsListActivity.class);
                        startActivity(personIntent);
                        break;
                }
            }
        });
    }


}
