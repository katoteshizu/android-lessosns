package ru.startandroid.p0001androidstudy;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ru.startandroid.p0001androidstudy.model.Address;

/**
 * @author by Andrey on 2/15/2016.
 */
public class AddressDetailsFragment extends DialogFragment implements View.OnClickListener {

    @Nullable
    private AddressListener addressListener;
    @Nullable
    private Address address;


    Button buttonSave;
    Button buttonCancel;
    EditText addressEditStreet;
    EditText addressEditBuilding;
    EditText addressEditBlock;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.adress_details_dialog, null);

        getDialog().setTitle("Address details");

        if (address != null) {

            buttonSave = (Button) v.findViewById(R.id.btnYes);
            buttonSave.setOnClickListener(this);
            buttonCancel = (Button) v.findViewById(R.id.btnNo);
            buttonCancel.setOnClickListener(this);

            addressEditStreet = (EditText) v.findViewById(R.id.edAddressStreet);
            addressEditStreet.setText(address.street, TextView.BufferType.EDITABLE);

            addressEditBuilding = (EditText) v.findViewById(R.id.edAddressBuilding);
            addressEditBlock = (EditText) v.findViewById(R.id.edAddressBlock);

            if (address.building != -1 && address.block != -1) {
                addressEditBuilding.setText(String.valueOf(address.building), TextView.BufferType.EDITABLE);
                addressEditBlock.setText(String.valueOf(address.block), TextView.BufferType.EDITABLE);
            }
            addressEditStreet.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    addressEditStreet.setBackgroundResource(0);
                }
            });

            addressEditBuilding.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    addressEditBuilding.setBackgroundResource(0);
                }
            });

            addressEditBlock.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    addressEditBlock.setBackgroundResource(0);
                }
            });
        }
        getDialog().setCancelable(false);

        return v;
    }

    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnYes:

                String street = addressEditStreet.getText().toString();
                int building, block;
                if (addressEditBuilding.getText().length() != 0) {
                    building = Integer.parseInt(addressEditBuilding.getText().toString());
                } else building = 0;
                if (addressEditBlock.getText().length() != 0) {
                    block = Integer.parseInt(addressEditBlock.getText().toString());
                } else block = 0;

                if (addressListener != null && address != null && (street.length() != 0) && building !=0 && block != 0) {
                    addressListener.onAddressUpdated(new Address(address.id, street, building, block));
                    dismiss();
                } else {
                    if (addressEditStreet.getText().length() == 0) {
                        addressEditStreet.setBackgroundResource(R.drawable.shape);
                    } else
                        addressEditStreet.setBackgroundResource(0);
                    if (addressEditBuilding.getText().length() == 0) {
                        addressEditBuilding.setBackgroundResource(R.drawable.shape);
                    } else
                        addressEditBuilding.setBackgroundResource(0);
                    if (addressEditBlock.getText().length() == 0) {
                        addressEditBlock.setBackgroundResource(R.drawable.shape);
                    } else
                        addressEditBlock.setBackgroundResource(0);
                    Toast.makeText(getContext(), "Data is invalid", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.btnNo:
                dismiss();
                break;
            default:
                dismiss();
                break;

        }
    }


    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
//        Utilities.logD(TAG, "onDismiss");
    }

    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
//        Utilities.logD(TAG, "onCancel");
    }

    public void show(Address address, AddressListener addressListener, FragmentManager manager, String tag) {
        this.addressListener = addressListener;
        this.address = address;
        super.show(manager, tag);
    }

    public TestApplication getTestApplication() {
        return getAppActivity().getTestApplication();
    }

    public AppActivity getAppActivity() {
        return (AppActivity) getActivity();
    }

}
