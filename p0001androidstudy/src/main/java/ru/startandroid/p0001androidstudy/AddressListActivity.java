package ru.startandroid.p0001androidstudy;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Toast;

import java.util.List;

import ru.startandroid.p0001androidstudy.model.Address;
import utilities.Utilities;

/**
 * @author by Andrey on 2/15/2016.
 */
public class AddressListActivity extends ListActivity<Address> {

    private final static String TAG = AddressListActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        itemsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Address selectedAddress = (Address) parent.getAdapter().getItem(position);
                handleOnAddNewEntry(selectedAddress);
            }
        });
        itemsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
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

    @NonNull
    @Override
    protected List<Address> filterByCondition(String query) {
        return getTestApplication().getAddressDao().searchAddresses(query);
    }

    @NonNull
    @Override
    protected List<Address> getAllData() {
        long start = System.currentTimeMillis();
        List<Address> allAddresses = getTestApplication().getAddressDao().getAllAddresses();
        long end = System.currentTimeMillis();
        Utilities.logD(TAG, "Read all addresses from db: " + (end - start));
        return allAddresses;
    }

    @Override
    protected BaseAdapter getAdapter(List<Address> data) {
        return new AddressAdapter(getApplicationContext(), data);
    }

    @Override
    protected void handleOnAddNewEntry(@Nullable Address address) {
        AddressDetailsFragment stateDetailsDialogue = new AddressDetailsFragment();
        if (address == null) {
            stateDetailsDialogue.show(new Address(), addressListener, getSupportFragmentManager(), "stateDetailsDialogue");
        } else {
            stateDetailsDialogue.show(address, addressListener, getSupportFragmentManager(), "stateDetailsDialogue");
        }
    }

    private AddressAdapter getAddressAdapter() {
        return (AddressAdapter) adapter;
    }

    private final AddressListener addressListener = new AddressListener() {
        @Override
        public void onAddressUpdated(Address address) {
            int position = (int) getAddressAdapter().getPosition(address);
            getTestApplication().getAddressDao().save(address);
            getAddressAdapter().update(address);
            getAddressAdapter().refreshAddresses(getTestApplication().getAddressDao().getAllAddresses());
            if (position == -1) {
                itemsList.smoothScrollToPosition(adapter.getCount() - 1);
            }
        }

        @Override
        public void onAddressDelete(Address address) {
            getTestApplication().getAddressDao().remove(address);
            getAddressAdapter().refreshAddresses(getTestApplication().getAddressDao().getAllAddresses());
        }
    };
}
