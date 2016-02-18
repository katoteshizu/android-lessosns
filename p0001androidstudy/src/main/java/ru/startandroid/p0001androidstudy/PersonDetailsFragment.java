package ru.startandroid.p0001androidstudy;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import ru.startandroid.p0001androidstudy.bitmap.BitmapUtility;
import ru.startandroid.p0001androidstudy.model.Person;
import utilities.Utilities;

/**
 * Created by Work on 2/4/2016.
 */

public class PersonDetailsFragment extends DialogFragment implements View.OnClickListener {

    private final static String TAG = PersonDetailsFragment.class.getSimpleName();
    private static final int CAMERA_REQUEST = 1888;
    private static final int maxImageSize = 100;
    public final static String PREVIEW_PATH = "preview";
    @Nullable
    private PersonListener personListener;
    @Nullable
    private Person person;


    Button buttonSave;
    Button buttonCancel;
    EditText personEditText;
    EditText personEditEmail;
    ImageView personEditFace;
    Context context;

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

            personEditFace = (ImageView) v.findViewById(R.id.ivFace);
            personEditFace.setOnClickListener(this);

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
        getDialog().setCancelable(false);
        long end = System.currentTimeMillis();
        Utilities.logD(TAG, "Get_ details view: " + (end - start));
        return v;
    }

    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnYes:

                CharSequence email = personEditEmail.getText().toString();
                String personName = personEditText.getText().toString();

                if (personListener != null && person != null && isEmailValid(email) && (personName.length() != 0)) {
                    personListener.onPersonUpdated(new Person(person.id, person.fileName, personEditText.getText().toString(), email.toString()));
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
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            personEditFace.setImageBitmap(photo);

            File f = new File(getContext().getCacheDir(), "tempFile.png");
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(f);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            if (f.exists() && photo != null) {
                photo.compress(Bitmap.CompressFormat.PNG, 100, fos);
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            addPhoto(f);
            if (f.exists()) {
                f.delete();
            }

        }
    }

    private void addPhoto(File photo) {
        context = getContext();
        File folder = context.getFilesDir();
        File previews = new File(folder, PREVIEW_PATH);

        if (!previews.exists()) {
            if (!previews.mkdirs()) {
                Utilities.logE(PersonDetailsFragment.class.getSimpleName(), "ALARM!!!");
            }
        }

        UUID uuid = UUID.randomUUID();
        String filePath = null;
        if (person != null) {
            filePath = previews.getAbsolutePath() + File.separator + person.name + "-" + uuid.toString() + ".png";
            File faceFile = new File(filePath);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    BitmapUtility.getImageFile(context, 0, photo, faceFile);
                }
            }).start();

            removeOldPhoto(person.id, person.fileName);
            person.fileName = filePath;
        }
    }

    private void removeOldPhoto(long id, String fileName) {
        List<Person> listPersons = getTestApplication().getPersonDao().getAllPersons();
        int matchCounter = 0;
        for (Person personItem : listPersons) {
            if (fileName.equals(personItem.fileName) && personItem.id != id) {
                matchCounter++;
            }
        }
        if ((matchCounter == 0) && (fileName != null)) {
            File fileToDelete = new File(fileName);
            if (!fileToDelete.delete()) {
                Utilities.logE(PersonDetailsFragment.class.getSimpleName(), "File couldn't be deleted.");
            }
        }
    }


    private boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
//        Utilities.logD(TAG, "onDismiss");
    }

    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
//        Utilities.logD(TAG, "onCancel");
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