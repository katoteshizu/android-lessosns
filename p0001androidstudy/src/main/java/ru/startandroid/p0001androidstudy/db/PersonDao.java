package ru.startandroid.p0001androidstudy.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.startandroid.p0001androidstudy.model.Person;

/**
 * @author by Andrey on 2/5/2016.
 */
public class PersonDao {

    private final static String TABLE = "PERSONS";
    private SQLiteDatabase sqLiteDatabase;
    private static final String ID = "_ID";
    private static final String PERSON_NAME = "PERSON_NAME";
    private static final String PERSON_EMAIL = "PERSON_EMAIL";
    private static final String IMAGE_PATH = "IMAGE_PATH";
    public static final long NULL_LONG = -1l;

    public PersonDao(SQLiteDatabase sqLiteDatabase) {
        this.sqLiteDatabase = sqLiteDatabase;
    }

    protected ContentValues personToContentValues(@NonNull Person person) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(PERSON_NAME, person.name);
        contentValues.put(PERSON_EMAIL, person.email);
        contentValues.put(IMAGE_PATH, person.fileName);

        return contentValues;
    }

    public long save(@NonNull Person person) {
        long id;
        if (person.id != NULL_LONG) {
            id = person.id;
            update(person);
        } else {
            id = insertOrReplace(person);
        }

        return id;
    }

    private void update(Person person) {
        sqLiteDatabase.beginTransaction();
        sqLiteDatabase.update(TABLE, personToContentValues(person), "_ID = " + person.id, null);
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
    }

    public boolean remove(@NonNull Person person) {
        boolean result = false;
        String value = String.valueOf(person.id);
        sqLiteDatabase.beginTransaction();
        if (sqLiteDatabase.delete(TABLE, "_ID = ?", new String[]{value}) > 0) {
            result = true;
        }
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
        return result;
    }

    @NonNull
    public List<Person> getAllPersons() {
        String query = "SELECT * FROM " + TABLE;
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        return extractDataFromCursor(cursor);
    }

    @NonNull
    public List<Person> searchPersons(String pattern){
        String query = "SELECT * FROM " + TABLE + " WHERE " + PERSON_NAME + " LIKE '%" + pattern + "%'";
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        return extractDataFromCursor(cursor);
    }

    private long insertOrReplace(@NonNull Person person) {
        long id;
        sqLiteDatabase.beginTransaction();
        id = sqLiteDatabase.replace(TABLE, null, personToContentValues(person));
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
        person.id = id;
        return id;
    }

    @NonNull
    private List<Person> extractDataFromCursor(Cursor cursor) {
        List<Person> result;
        if (cursor.moveToFirst()) {
            result = new ArrayList<>(cursor.getCount());

            while (!cursor.isAfterLast()) {
                result.add(cursorToEntity(cursor));
                cursor.moveToNext();
            }
        } else {
            result = Collections.emptyList();
        }

        cursor.close();
        return result;
    }

    @NonNull
    protected Person cursorToEntity(@NonNull Cursor cursor) {

        Person person = new Person();
        person.id = cursor.getLong(0);
        person.name = cursor.getString(1);
        person.email = cursor.getString(2);
        person.fileName = cursor.getString(3);

        return person;
    }

    @Nullable
    public Person findById(long id) {
        return findFirstByColumn(String.valueOf(id));
    }

    @Nullable
    public Person findFirstByColumn(@NonNull String value) {
        Person entity = null;
        Cursor cursor = sqLiteDatabase.query(TABLE, null, "_ID = ?", new String[]{value}, null, null, null);

        if (cursor.moveToFirst()) {
            entity = cursorToEntity(cursor);
        }

        cursor.close();
        return entity;
    }

    public boolean hasPersons() {
        Cursor cursor = sqLiteDatabase.rawQuery("select count(*) from " + TABLE, null);
        if (cursor.moveToFirst()) {
            return cursor.getInt(0) > 0;
        }
        return false;
    }
}
