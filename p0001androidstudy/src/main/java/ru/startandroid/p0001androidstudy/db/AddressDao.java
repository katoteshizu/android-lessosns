package ru.startandroid.p0001androidstudy.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.startandroid.p0001androidstudy.model.Address;

/**
 * @author by Andrey on 2/15/2016.
 */
public class AddressDao {
    private final static String TABLE = "ADDRESSES";
    private SQLiteDatabase sqLiteDatabase;
    private static final String ID = "_ID";
    private static final String ADDRESS_STREET = "ADDRESS_STREET";
    private static final String ADDRESS_BUILDING = "ADDRESS_BUILDING";
    private static final String ADDRESS_BLOCK = "ADDRESS_BLOCK";
    public static final long NULL_LONG = -1l;

    public AddressDao(SQLiteDatabase sqLiteDatabase) {
        this.sqLiteDatabase = sqLiteDatabase;
    }

    protected ContentValues addressToContentValues(@NonNull Address address) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(ADDRESS_STREET, address.street);
        contentValues.put(ADDRESS_BUILDING, address.building);
        contentValues.put(ADDRESS_BLOCK, address.block);

        return contentValues;
    }

    public long save(@NonNull Address address) {
        long id;
        if (address.id != NULL_LONG) {
            id = address.id;
            update(address);
        } else {
            id = insertOrReplace(address);
        }

        return id;
    }

    private void update(Address address) {
        sqLiteDatabase.beginTransaction();
        sqLiteDatabase.update(TABLE, addressToContentValues(address), "_ID = " + address.id, null);
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
    }

    public boolean remove(@NonNull Address address) {
        boolean result = false;
        String value = String.valueOf(address.id);
        sqLiteDatabase.beginTransaction();
        if (sqLiteDatabase.delete(TABLE, "_ID = ?", new String[]{value}) > 0) {
            result = true;
        }
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
        return result;
    }

    @NonNull
    public List<Address> getAllAddresses() {
        String query = "SELECT * FROM " + TABLE;
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        return extractDataFromCursor(cursor);
    }

    @Nullable
    public List<Address> findAddressById(int id) {
        return searchAddressesByColumn(String.valueOf(id));
    }

    @NonNull
    public List<Address> searchAddressesByColumn(@NonNull String value) {
        Cursor cursor = sqLiteDatabase.query(TABLE, null, "_ID = ?", new String[]{value}, null, null, null);
        return extractDataFromCursor(cursor);
    }

    private long insertOrReplace(@NonNull Address address) {
        long id;
        sqLiteDatabase.beginTransaction();
        id = sqLiteDatabase.replace(TABLE, null, addressToContentValues(address));
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
        address.id = id;
        return id;
    }

    @NonNull
    private List<Address> extractDataFromCursor(Cursor cursor) {
        List<Address> result;
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
    protected Address cursorToEntity(@NonNull Cursor cursor) {

        Address address = new Address();
        address.id = cursor.getLong(0);
        address.street = cursor.getString(1);
        address.building = cursor.getInt(2);
        address.block = cursor.getInt(3);

        return address;
    }

    @Nullable
    public Address findById(long id) {
        return findFirstByColumn(String.valueOf(id));
    }

    @Nullable
    public Address findFirstByColumn(@NonNull String value) {
        Address entity = null;
        Cursor cursor = sqLiteDatabase.query(TABLE, null, "_ID = ?", new String[]{value}, null, null, null);

        if (cursor.moveToFirst()) {
            entity = cursorToEntity(cursor);
        }

        cursor.close();
        return entity;
    }

    public boolean hasAddresses() {
        Cursor cursor = sqLiteDatabase.rawQuery("select count(*) from " + TABLE, null);
        return cursor.moveToFirst() && cursor.getInt(0) > 0;
    }


    @NonNull
    public List<Address> searchAddresses(String pattern) {
        String query = "SELECT * FROM " + TABLE + " WHERE " + ADDRESS_STREET + " LIKE '%" + pattern + "%'";
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        return extractDataFromCursor(cursor);
    }
}
