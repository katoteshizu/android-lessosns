package ru.startandroid.p0001androidstudy;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.UUID;

import ru.startandroid.p0001androidstudy.bitmap.BitmapUtility;
import ru.startandroid.p0001androidstudy.model.Person;
import utilities.Utilities;

/**
 * @author by Andrey on 2/18/2016.
 */
public class SavePersonTask {
    private static final String TAG = SavePersonTask.class.getSimpleName();
    private static final String PREVIEW_PATH = "preview";

    private long personID;
    private String personImagePath;
    private String personName;
    private String personEmail;
    private Bitmap personPhoto;

    private Context context;

    public SavePersonTask(Context context, long id, String name, String email, String imagePath, Bitmap photo) {
        this.context = context;
        this.personID = id;
        this.personImagePath = imagePath;
        this.personName = name;
        this.personEmail = email;
        this.personPhoto = photo;
    }

    public Person getPerson() {
        if (personImagePath == null || personImagePath.isEmpty()) {
            personImagePath = getImagePath(personPhoto);
        }
        return new Person(personID, personImagePath, personName, personEmail);
    }

    private String getImagePath(Bitmap photo) {
        String filePath = null;
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

//            removeOldPhoto(person.id, person.fileName);


            personImagePath = filePath;
//        }
        return filePath;
    }


//    private void removeOldPhoto(long id, String fileName) {
//        List<Person> listPersons = getTestApplication().getPersonDao().getAllPersons();
//        int matchCounter = 0;
//        for (Person personItem : listPersons) {
//            if (fileName.equals(personItem.fileName) && personItem.id != id) {
//                matchCounter++;
//            }
//        }
//        if ((matchCounter == 0) && (fileName != null)) {
//            File fileToDelete = new File(fileName);
//            if (!fileToDelete.delete()) {
//                Utilities.logE(TAG, "File couldn't be deleted.");
//            }
//        }
//    }

//    public TestApplication getTestApplication() {
//        return getAppActivity().getTestApplication();
//    }
//
//    public AppActivity getAppActivity() {
//        return (AppActivity) getActivity();
//    }
}
