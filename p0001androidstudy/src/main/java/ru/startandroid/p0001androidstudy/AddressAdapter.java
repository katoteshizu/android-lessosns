package ru.startandroid.p0001androidstudy;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ru.startandroid.p0001androidstudy.model.Address;

/**
 * @author by Andrey on 2/15/2016.
 */
public class AddressAdapter extends BaseAdapter {

    private LayoutInflater lInflater;
    private SparseArray<Address> addresses = new SparseArray<>();

    public AddressAdapter(Context ctx, List<Address> addresses) {
        lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (Address address : addresses) {
            this.addresses.put((int) address.id, address);
        }
    }

    @Override
    public int getCount() {
        return addresses.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        int key = addresses.keyAt(position);
        return key != -1 ? addresses.get(key) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = lInflater.inflate(R.layout.itemrow, parent, false);
        }

        Address p = getAddress(position);
        if (p != null) {
            TextView addressStreet = (TextView) view.findViewById(R.id.tvAddressStreet);
            TextView addressBuilding = (TextView) view.findViewById(R.id.tvAddressBuilding);
            TextView addressBlock = (TextView) view.findViewById(R.id.tvAddressBlock);
            addressStreet.setText(p.id + ": " + p.street);
            addressBuilding.setText(p.building);
            addressBlock.setText(p.block);
        }


        return view;
    }

    public void update(@Nullable Address address) {
        if (address != null && addresses.size() > address.id) {
            addresses.put((int) address.id, address);
        }
    }

    public void refreshAddresses(List<Address> addresses){
        this.addresses.clear();
        for (Address address : addresses) {
            this.addresses.put((int) address.id, address);
        }
        notifyDataSetChanged();

    }

    @Nullable
    private Address getAddress(int position) {
        return ((Address) getItem(position));
    }
}
