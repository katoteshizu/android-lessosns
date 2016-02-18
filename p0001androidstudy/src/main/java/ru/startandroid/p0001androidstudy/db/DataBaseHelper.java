package ru.startandroid.p0001androidstudy.db;

import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import ru.startandroid.p0001androidstudy.R;
import utilities.Utilities;

/**
 * Created by Work on 2/5/2016.
 */
public class DataBaseHelper extends SQLiteOpenDatabaseHelper {

    private final static String TAG = DataBaseHelper.class.getSimpleName();
    private static final int DATABASE_VERSION_1 = 1; // persons only
    private static final int DATABASE_VERSION_2 = 2; // persons + addresses
    private static final String DATABASE_NAME = "database.db";

    @NonNull
    private final Resources resources;

    public DataBaseHelper(@NonNull Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION_2);

        resources = context.getResources();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        Utilities.logD(TAG, "On create database");

        SQLFileHelper.executeRawResource(resources, R.raw.database, db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Utilities.logD(TAG, "On update database. oldVersion: " + oldVersion + " newVersion: " + newVersion);

        switch (oldVersion) {
            case DATABASE_VERSION_1:
                new UpdateDatabaseFrom1_To_2(this, db).update();
                break;
            default:
                executeMultipleCommands(db, R.raw.drop_tables);
                onCreate(db);
                break;
        }
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
        Utilities.logD(TAG, "On downgrade database. oldVersion: " + oldVersion + " newVersion: " + newVersion);
    }
}
