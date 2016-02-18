package ru.startandroid.p0001androidstudy;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import ru.startandroid.p0001androidstudy.db.AddressDao;
import ru.startandroid.p0001androidstudy.db.DataBaseHelper;
import ru.startandroid.p0001androidstudy.db.PersonDao;
import ru.startandroid.p0001androidstudy.db.SaveDefaultsTask;

/**
 * @author by Andrey on 2/5/2016.
 */
public class TestApplication extends Application {

    PersonDao personDao;
    AddressDao addressDao;
    SaveDefaultsTask saveDefaultsTask;

    @Override
    public void onCreate() {
        super.onCreate();

        DataBaseHelper dataBaseHelper = new DataBaseHelper(this);
        SQLiteDatabase sqLiteDatabase = dataBaseHelper.getWritableDatabase();
        personDao = new PersonDao(sqLiteDatabase);
        addressDao = new AddressDao(sqLiteDatabase);
        if (!personDao.hasPersons()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    saveDefaultsTask = new SaveDefaultsTask(personDao, TestApplication.this);
                    saveDefaultsTask.saveDefaults();
                }
            }).start();
        }
    }

    public PersonDao getPersonDao() {
        return personDao;
    }

    public AddressDao getAddressDao() {
        return addressDao;
    }
}
