package ru.startandroid.p0001androidstudy.db;

import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.RawRes;

import com.google.common.base.Splitter;
import com.google.common.io.CharStreams;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import utilities.Utilities;

/**
 * Created by Andrey on 2/8/2016.
 */
public class SQLFileHelper {
    private static final String TAG = SQLFileHelper.class.getSimpleName();

    public static void executeRawResource(Resources resources, @RawRes int resId, SQLiteDatabase db) {
        InputStream inputStream = resources.openRawResource(resId);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

        try {
            String sql = CharStreams.toString(inputStreamReader);
            Iterable<String> statements = Splitter
                    .on(';')
                    .trimResults()
                    .omitEmptyStrings()
                    .split(sql);

            for (String statement : statements) {
                db.execSQL(statement);
            }
        } catch (IOException e) {
            Utilities.logE(TAG, "Cannot read sql file!");
        }
    }

}
