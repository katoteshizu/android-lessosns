package ru.startandroid.p0001androidstudy;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import ru.startandroid.p0001androidstudy.model.Address;

/**
 * @author by Andrey on 2/15/2016.
 */
public class AddressListActivity extends AppActivity {
    private AddressAdapter addressAdapter;
    protected ListView lvAddressList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listitems);

        lvAddressList = (ListView) findViewById(R.id.itemsList);

        addressAdapter = new AddressAdapter(this, getTestApplication().getAddressDao().getAllAddresses());
        lvAddressList.setAdapter(addressAdapter);

        lvAddressList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Address selectedAddress = (Address) parent.getAdapter().getItem(position);

                AddressDetailsFragment stateDetailsDialogue = new AddressDetailsFragment();

                stateDetailsDialogue.show(selectedAddress, addressListener, getSupportFragmentManager(), "stateDetailsDialogue");
            }
        });
        lvAddressList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "Long press at: " + position + " id: " + id, Toast.LENGTH_SHORT).show();
                Address selectedAddress = (Address) parent.getAdapter().getItem(position);

                AddressMenuFragment addressMenuFragment = new AddressMenuFragment();
                addressMenuFragment.show(selectedAddress, addressListener, getSupportFragmentManager(), "addressMenuFragment");
                return true;
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbarmenu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.it_add:
                AddressDetailsFragment stateDetailsDialogue = new AddressDetailsFragment();
                stateDetailsDialogue.show(new Address(), addressListener, getSupportFragmentManager(), "stateDetailsDialogue");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private final AddressListener addressListener = new AddressListener() {
        @Override
        public void onAddressUpdated(Address address) {
            getTestApplication().getAddressDao().save(address);
            addressAdapter.update(address);
            addressAdapter.refreshAddresses(getTestApplication().getAddressDao().getAllAddresses());
            lvAddressList.smoothScrollToPosition(addressAdapter.getCount() - 1);
        }
        @Override
        public void onAddressDelete(Address address){
            getTestApplication().getAddressDao().remove(address);
            addressAdapter.refreshAddresses(getTestApplication().getAddressDao().getAllAddresses());
        }
    };
}
