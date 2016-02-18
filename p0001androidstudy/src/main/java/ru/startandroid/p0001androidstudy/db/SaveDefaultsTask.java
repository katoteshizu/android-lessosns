package ru.startandroid.p0001androidstudy.db;

import android.content.Context;
import android.content.res.Resources;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import ru.startandroid.p0001androidstudy.R;
import ru.startandroid.p0001androidstudy.bitmap.BitmapUtility;
import ru.startandroid.p0001androidstudy.model.Person;
import utilities.Utilities;

/**
 * @author by Andrey on 2/5/2016.
 */
public class SaveDefaultsTask {

    public final static String PREVIEW_PATH = "preview";

    private static final List<PersonDefaultData> personList;

    static {
        personList = new LinkedList<>();
        personList.add(new PersonDefaultData(R.drawable.face, "Uno", "qwerty@g.com"));
        personList.add(new PersonDefaultData(R.drawable.face_0000, "Duos", "qwerty@g.com"));
        personList.add(new PersonDefaultData(R.drawable.face_0001, "Tres", "qwerty@g.com"));
        personList.add(new PersonDefaultData(R.drawable.face_0002, "Quatro", "qwerty@g.com"));
        personList.add(new PersonDefaultData(R.drawable.face_0003, "Quinque", "qwerty@g.com"));
        personList.add(new PersonDefaultData(R.drawable.face_0004, "Sex", "qwerty@g.com"));
        personList.add(new PersonDefaultData(R.drawable.face, "Septem", "qwerty@g.com"));
        personList.add(new PersonDefaultData(R.drawable.face_0001, "Octo", "qwerty@g.com"));
        personList.add(new PersonDefaultData(R.drawable.face_0002, "Novem", "qwerty@g.com"));
        personList.add(new PersonDefaultData(R.drawable.face_0003, "Decem", "qwerty@g.com"));
        personList.add(new PersonDefaultData(R.drawable.face_0004, "Undecim", "qwerty@g.com"));
    }

    private final PersonDao personDao;
    private final Context context;

    public SaveDefaultsTask(PersonDao personDao, Context context) {
        this.personDao = personDao;
        this.context = context;
    }

    public void saveDefaults() {
        File folder = context.getFilesDir();
        File previews = new File(folder, PREVIEW_PATH);
        Resources resources = context.getResources();

        if (!previews.mkdirs()) {
            Utilities.logE(SaveDefaultsTask.class.getSimpleName(), "ALARM!!!");
        }

        for (PersonDefaultData person : personList) {

            int faceID = person.face;
            String faceFileName = resources.getResourceEntryName(faceID);
            String filePath = previews.getAbsolutePath() + File.separator + faceFileName + ".png";
            File faceFile = new File(filePath);

            BitmapUtility.convertImage(context, faceID, null, faceFile);

            Person newPerson = new Person(PersonDao.NULL_LONG, filePath, person.name, person.email);
            personDao.save(newPerson);
        }
    }

    public static class PersonDefaultData {
        public int face;
        public String name;
        public String email;

        public PersonDefaultData(int face, String name, String email) {
            this.face = face;
            this.name = name;
            this.email = email;
        }
    }
}
