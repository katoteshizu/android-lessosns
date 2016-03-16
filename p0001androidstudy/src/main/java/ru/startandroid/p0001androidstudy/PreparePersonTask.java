package ru.startandroid.p0001androidstudy;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.UUID;

import ru.startandroid.p0001androidstudy.bitmap.BitmapUtility;
import ru.startandroid.p0001androidstudy.model.Person;
import utilities.Utilities;

/**
 * @author by Andrey on 2/18/2016.
 */
public class PreparePersonTask {
    private static final String TAG = PreparePersonTask.class.getSimpleName();
    private static final String PREVIEW_PATH = "preview";

    private long personID;
    private String personImagePath;
    private String personName;
    private String personEmail;
    private Bitmap personPhoto;
    private int addressID;
    private TestApplication testApplication;

    private Context context;

    public PreparePersonTask(Context context, TestApplication testApplication, long id, String name,
                             String email, String imagePath, Bitmap photo, int addressID) {
        this.context = context;
        this.testApplication = testApplication;
        this.personID = id;
        this.personImagePath = imagePath;
        this.personName = name;
        this.personEmail = email;
        this.personPhoto = photo;
        this.addressID = addressID;
    }

    public Person getPerson() {
        if (personPhoto != null) {
            personImagePath = getImagePath(personPhoto);
        }
        return new Person(personID, personImagePath, personName, personEmail, addressID);
    }

    private String getImagePath(Bitmap photo) {
        String filePath;
        File folder = context.getFilesDir();
        File previews = new File(folder, PREVIEW_PATH);

        if (!previews.exists()) {
            if (!previews.mkdirs()) {
                Utilities.logE(TAG, "Can't create preview dir.");
            }
        }

        UUID uuid = UUID.randomUUID();
        filePath = previews.getAbsolutePath() + File.separator + personName + "-" + uuid.toString() + ".png";
        String tempFilePath = previews.getAbsolutePath() + File.separator + uuid.toString() + ".png";
        File faceFile = new File(filePath);
        File tempFaceFile = new File(tempFilePath);

        OutputStream os;
        try {
            os = new FileOutputStream(tempFaceFile);
            photo.compress(Bitmap.CompressFormat.PNG, 100, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Utilities.logE(TAG, "Error writing bitmap", e);
        }


        BitmapUtility.convertImage(context, 0, tempFaceFile, faceFile);

        removeOldPhoto(personID, personImagePath);

        if (!tempFaceFile.delete()) {
            Utilities.logE(TAG, "Temp image file couldn't be deleted.");
        }

        if (faceFile.exists()) {
            personImagePath = filePath;
        } else {
            personImagePath = null;
            Utilities.logE(TAG, "Setting image path failed.");
        }

        return filePath;
    }


    private void removeOldPhoto(long id, String fileName) {
        List<Person> listPersons = testApplication.getPersonDao().getAllPersons();
        int matchCounter = 0;
        for (Person personItem : listPersons) {
            if (fileName.equals(personItem.fileName) && personItem.id != id) {
                matchCounter++;
            }
        }
        if ((matchCounter == 0) && (fileName != null)) {
            File fileToDelete = new File(fileName);
            if (!fileToDelete.delete()) {
                Utilities.logE(TAG, "File couldn't be deleted.");
            }
        }
    }

}
