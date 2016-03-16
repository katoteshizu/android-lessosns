package ru.startandroid.p0001androidstudy.db;

import android.content.Context;
import android.content.res.Resources;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import ru.startandroid.p0001androidstudy.R;
import ru.startandroid.p0001androidstudy.bitmap.BitmapUtility;
import ru.startandroid.p0001androidstudy.model.Address;
import ru.startandroid.p0001androidstudy.model.Person;
import utilities.Utilities;

/**
 * @author by Andrey on 2/5/2016.
 */
public class SaveDefaultsTask {

    public final static String PREVIEW_PATH = "preview";

    private static final List<PersonDefaultData> personList;
    private static final List<AddressDefaultData> addressList;

    static {
        personList = new LinkedList<>();
        personList.add(new PersonDefaultData(R.drawable.face, "Uno", "qwerty@g.com", 1));
        personList.add(new PersonDefaultData(R.drawable.face_0000, "Duos", "qwerty@g.com", 2));
        personList.add(new PersonDefaultData(R.drawable.face_0001, "Tres", "qwerty@g.com", 3));
        personList.add(new PersonDefaultData(R.drawable.face_0002, "Quatro", "qwerty@g.com", 4));
        personList.add(new PersonDefaultData(R.drawable.face_0003, "Quinque", "qwerty@g.com", 5));
        personList.add(new PersonDefaultData(R.drawable.face_0004, "Sex", "qwerty@g.com", 6));
        personList.add(new PersonDefaultData(R.drawable.face, "Septem", "qwerty@g.com", 1));
        personList.add(new PersonDefaultData(R.drawable.face_0001, "Octo", "qwerty@g.com", 1));
        personList.add(new PersonDefaultData(R.drawable.face_0002, "Novem", "qwerty@g.com", 1));
        personList.add(new PersonDefaultData(R.drawable.face_0003, "Decem", "qwerty@g.com", 2));
        personList.add(new PersonDefaultData(R.drawable.face_0004, "Undecim", "qwerty@g.com", 2));

        addressList = new LinkedList<>();
        addressList.add(new AddressDefaultData("Liberty", 14, 1));
        addressList.add(new AddressDefaultData("Independency", 23, 2));
        addressList.add(new AddressDefaultData("Lenina", 55, 3));
        addressList.add(new AddressDefaultData("Sunset", 13, 4));
        addressList.add(new AddressDefaultData("Mulholland", 8, 5));
        addressList.add(new AddressDefaultData("Soho", 22, 6));

    }

    private final PersonDao personDao;
    private final AddressDao addressDao;
    private final Context context;

    public SaveDefaultsTask(PersonDao personDao, AddressDao addressDao, Context context) {
        this.personDao = personDao;
        this.addressDao = addressDao;
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

            Person newPerson = new Person(PersonDao.NULL_LONG, filePath, person.name, person.email, person.addressID);
            personDao.save(newPerson);
        }

        for (AddressDefaultData address : addressList){
            Address newAddress = new Address(AddressDao.NULL_LONG, address.street, address.building, address.block);
            addressDao.save(newAddress);
        }
    }

    public static class PersonDefaultData {
        public int face;
        public String name;
        public String email;
        public int addressID;

        public PersonDefaultData(int face, String name, String email, int addressID) {
            this.face = face;
            this.name = name;
            this.email = email;
            this.addressID = addressID;
        }
    }

    public static class AddressDefaultData {
        public String street;
        public int building;
        public int block;

        public AddressDefaultData(String street, int building, int block) {
            this.street = street;
            this.building = building;
            this.block = block;
        }
    }
}
