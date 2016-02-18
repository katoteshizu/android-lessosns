package ru.startandroid.p0001androidstudy;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ru.startandroid.p0001androidstudy.model.Person;

/**
 * @author by Andrey on 2/11/2016.
 */
public class PopupMenuFragment extends DialogFragment implements View.OnClickListener {

    private Button btnEditPerson;
    private Button btnDeletePerson;
    private Button btnCancel;
    @Nullable
    private PersonListener personListener;
    @Nullable
    private Person person;

    public void show(Person person, PersonListener personListener, FragmentManager manager, String tag) {
        this.personListener = personListener;
        this.person = person;
        super.show(manager, tag);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.popup_dialog, null);
        getDialog().setTitle(R.string.menu_title);
        getDialog().setCancelable(false);

        btnEditPerson = (Button) v.findViewById(R.id.btnEdit);
        btnEditPerson.setOnClickListener(this);

        btnDeletePerson = (Button) v.findViewById(R.id.btnDelete);
        btnDeletePerson.setOnClickListener(this);

        btnCancel = (Button) v.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnEdit:
                PersonDetailsFragment stateDetailsDialogue = new PersonDetailsFragment();
                stateDetailsDialogue.show(person, personListener, getPersonsListActivity().getSupportFragmentManager(), "stateDetailsDialogue");
                stateDetailsDialogue.setCancelable(false);
                dismiss();
                break;
            case R.id.btnDelete:
                personListener.onPersonDelete(person);
                dismiss();
                break;
            case R.id.btnCancel:
                dismiss();
                break;


        }
    }

    public PersonsListActivity getPersonsListActivity() {
        return (PersonsListActivity) getActivity();
    }
}
