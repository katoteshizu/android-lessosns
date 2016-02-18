package ru.startandroid.p0001androidstudy.db;

import android.content.Context;
import android.content.res.Resources;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.IOException;
import java.io.InputStream;

import utilities.Utilities;

/**
 * @author by Andrey on 2/15/2016.
 */
public abstract class SQLiteOpenDatabaseHelper extends SQLiteOpenHelper {

    private final static String TAG = SQLiteOpenDatabaseHelper.class.getSimpleName();

    private final Context mContext;

    public SQLiteOpenDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.mContext = context;
    }

    public SQLiteOpenDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version);
        this.mContext = context;
    }

    public void executeMultipleCommands(SQLiteDatabase db, int resourceId) {
        try {
            String sqlCode = getFileContent(getContext().getResources(), resourceId);
            for (String sqlStatements : sqlCode.trim().split(";")) {
                if (!sqlStatements.replaceAll("\\r|\\n", "").startsWith("--"))
                    db.execSQL(sqlStatements.replaceAll("\\r|\\n", ""));
            }
        } catch (IOException e) {
            Utilities.logE(TAG, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private static String getFileContent(Resources resources, int rawId) throws IOException {
        InputStream is = resources.openRawResource(rawId);
        int size = is.available();
        byte[] buffer = new byte[size];
        is.read(buffer);
        is.close();
        return new String(buffer);
    }

    protected Context getContext() {
        return mContext;
    }
}
