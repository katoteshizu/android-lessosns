package ru.startandroid.p0001androidstudy;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ru.startandroid.p0001androidstudy.model.Address;

/**
 * @author by Andrey on 2/15/2016.
 */
public class AddressMenuFragment extends DialogFragment implements View.OnClickListener {

    private Button btnEditAddress;
    private Button btnDeleteAddress;
    private Button btnCancel;
    @Nullable
    private AddressListener addressListener;
    @Nullable
    private Address address;

    public void show(Address address, AddressListener addressListener, FragmentManager manager, String tag) {
        this.addressListener = addressListener;
        this.address = address;
        super.show(manager, tag);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.popup_dialog, null);
        getDialog().setTitle(R.string.menu_title);
        getDialog().setCancelable(false);

        btnEditAddress = (Button) v.findViewById(R.id.btnEdit);
        btnEditAddress.setOnClickListener(this);

        btnDeleteAddress = (Button) v.findViewById(R.id.btnDelete);
        btnDeleteAddress.setOnClickListener(this);

        btnCancel = (Button) v.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnEdit:
                AddressDetailsFragment stateDetailsDialogue = new AddressDetailsFragment();
                stateDetailsDialogue.show(address, addressListener, getAddressesListActivity().getSupportFragmentManager(), "stateDetailsDialogue");
                stateDetailsDialogue.setCancelable(false);
                dismiss();
                break;
            case R.id.btnDelete:
                addressListener.onAddressDelete(address);
                dismiss();
                break;
            case R.id.btnCancel:
                dismiss();
                break;


        }
    }

    public AddressListActivity getAddressesListActivity() {
        return (AddressListActivity) getActivity();
    }
}
