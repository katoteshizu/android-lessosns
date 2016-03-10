package ru.startandroid.p0001androidstudy;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;

import ru.startandroid.p0001androidstudy.bitmap.BitmapUtility;
import ru.startandroid.p0001androidstudy.model.Person;
import utilities.Utilities;

/**
 * @author Andrew on 2/4/2016.
 */

public class PersonDetailsFragment extends DialogFragment implements View.OnClickListener {

    private final static String TAG = PersonDetailsFragment.class.getSimpleName();
    private static final int CAMERA_REQUEST = 1888;
    private static final int mDialogHeight = 460;
    private static final int mDialogWidth = 400;

    private float scale;
    @Nullable
    private PersonListener personListener;
    @Nullable
    private Person person;

    Button buttonSave;
    Button buttonCancel;
    ImageButton buttonSwitchDetails;
    EditText personEditText;
    EditText personEditEmail;
    ImageView personEditFace;
    TextView tvDetailsHeader;
    ListView lvPersonAddresses;
    ProgressDialog saveDialog;
    Context context;
    Resources resources;
    boolean personDetailsMode = true;
    @Nullable
    private Bitmap photo;
    private BaseAdapter adapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.details_dialogue, null);

        long start = System.currentTimeMillis();
        if (person != null) {
            if (person.name.isEmpty()) {
                getDialog().setTitle("New person");
            } else getDialog().setTitle(person.name);

            buttonSave = (Button) v.findViewById(R.id.btnYes);
            buttonSave.setOnClickListener(this);
            buttonCancel = (Button) v.findViewById(R.id.btnNo);
            buttonCancel.setOnClickListener(this);
            buttonSwitchDetails = (ImageButton) v.findViewById(R.id.btnAddresses);
            buttonSwitchDetails.setOnClickListener(this);
            personEditFace = (ImageView) v.findViewById(R.id.ivFace);
            personEditFace.setOnClickListener(this);
            tvDetailsHeader = (TextView) v.findViewById(R.id.tvPersonHeader);
            lvPersonAddresses = (ListView) v.findViewById(R.id.lvPersonAddresses);

            saveDialog = new ProgressDialog(getContext());
            context = getContext();
            resources = context.getResources();
            saveDialog.setMessage(resources.getString(R.string.saving_dialog_text));

            if (person.fileName != null && !person.fileName.isEmpty()) {
                File faceFile = new File(person.fileName);
                if (faceFile.exists()) {
                    Glide.with(getContext()).load(faceFile).into(personEditFace);
                } else {
                    Glide.with(getContext()).load(R.drawable.no_img).into(personEditFace);
                }
            }

            personEditText = (EditText) v.findViewById(R.id.editTextPerson);
            personEditText.setText(person.name, TextView.BufferType.EDITABLE);

            personEditEmail = (EditText) v.findViewById(R.id.editTextEmail);
            personEditEmail.setText(person.email, TextView.BufferType.EDITABLE);

            personEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    personEditText.setBackgroundResource(0);
                }
            });

            personEditEmail.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    personEditEmail.setBackgroundResource(0);
                }
            });
        }
        long end = System.currentTimeMillis();
        Utilities.logD(TAG, "Get_ details view: " + (end - start));
        scale = getContext().getResources().getDisplayMetrics().density;
        return v;
    }

    public void onStart() {
        super.onStart();
        int dpsWidth = BitmapUtility.pixelsToDPS(mDialogWidth, scale);
        int dpsHeight = BitmapUtility.pixelsToDPS(mDialogHeight, scale);
        if (getDialog() != null) getDialog().getWindow().setLayout(dpsWidth, dpsHeight);
    }

    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnYes:

                final CharSequence email = personEditEmail.getText().toString();
                String personName = personEditText.getText().toString();

                if (personListener != null && person != null && isEmailValid(email) && (personName.length() != 0)) {
                    saveDialog.show();
                    final long dialogStart = System.currentTimeMillis();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            PreparePersonTask preparePersonTask = new PreparePersonTask(getContext(),
                                    getTestApplication(), person.id, personEditText.getText().toString(),
                                    email.toString(), person.fileName, photo);
                            final Person personToSave = preparePersonTask.getPerson();

                            Handler handler = new Handler(Looper.getMainLooper());
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    personListener.onPersonUpdated(personToSave);
                                    long dialogStop = System.currentTimeMillis();
                                    Utilities.logD(TAG, "Save time ms: " + (dialogStop - dialogStart));
                                    saveDialog.dismiss();
                                }
                            });
                        }
                    }).start();
                    dismiss();
                } else {
                    if (personEditText.getText().length() == 0) {
                        personEditText.setBackgroundResource(R.drawable.shape);
                    } else
                        personEditText.setBackgroundResource(0);
                    if (personEditEmail.getText().length() == 0 || !isEmailValid(email)) {
                        personEditEmail.setBackgroundResource(R.drawable.shape);
                    } else
                        personEditEmail.setBackgroundResource(0);
                    Toast.makeText(getContext(), "Data is invalid", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnNo:
                dismiss();
                break;
            case R.id.ivFace:
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
                break;
            case R.id.btnAddresses:
                Utilities.logD(TAG, "Switch button pressed");
                switchPersonDetails();
                break;
        }
    }

    private void switchPersonDetails() {
        if (personDetailsMode) {
            tvDetailsHeader.setText(getString(R.string.person_addresses), TextView.BufferType.NORMAL);
            personEditFace.setVisibility(View.GONE);
            personEditText.setVisibility(View.GONE);
            personEditEmail.setVisibility(View.GONE);
            lvPersonAddresses.setVisibility(View.GONE);
            personDetailsMode = false;
        } else {
            tvDetailsHeader.setText(getString(R.string.person_dialog_text), TextView.BufferType.NORMAL);
            personEditFace.setVisibility(View.VISIBLE);
            personEditText.setVisibility(View.VISIBLE);
            personEditEmail.setVisibility(View.VISIBLE);
            lvPersonAddresses.setVisibility(View.VISIBLE);
//            adapter = new AddressAdapter(getContext(), getPersonAddresses(person.id));
            personDetailsMode = true;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            photo = (Bitmap) data.getExtras().get("data");
            personEditFace.setImageBitmap(photo);
        }
    }

    private boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
    }

    public void show(Person person, PersonListener personListener, FragmentManager manager, String tag) {
        this.personListener = personListener;
        this.person = person;
        super.show(manager, tag);
    }

    public TestApplication getTestApplication() {
        return getAppActivity().getTestApplication();
    }

    public AppActivity getAppActivity() {
        return (AppActivity) getActivity();
    }
}