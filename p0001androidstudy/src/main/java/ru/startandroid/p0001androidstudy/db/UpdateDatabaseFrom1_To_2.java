package ru.startandroid.p0001androidstudy.db;

import android.database.sqlite.SQLiteDatabase;

import ru.startandroid.p0001androidstudy.R;

/**
 * @author by Andrey on 2/15/2016.
 */
public class UpdateDatabaseFrom1_To_2 {
    private final SQLiteOpenDatabaseHelper dbHelper;
    private final SQLiteDatabase db;
    private static final String TABLE_NAME = "PERSONS";
    private static final String PERSONS_TEMP_TABLE = "PERSONS_TEMP";
    private final String[] mIntersectionOldAndNewPageTableColumns = new String[] {
            "_ID",
            "PERSON_NAME",
            "PERSON_EMAIL",
            "IMAGE_PATH"
    };

    public UpdateDatabaseFrom1_To_2(SQLiteOpenDatabaseHelper dbHelper, SQLiteDatabase db) {
        this.dbHelper = dbHelper;
        this.db = db;
    }

    public void update(){

        addAddressesTable();
        alterPersonsTable();
    }

    private void addAddressesTable() {
        createTable(R.raw.database_migrate_from_1_to_2_add_address);
    }

    private void alterPersonsTable() {
        copyPagesToTempTable();
        renameTableSafe(TABLE_NAME, PERSONS_TEMP_TABLE);
    }

    private void createTable(int resId) {
        dbHelper.executeMultipleCommands(db, resId);
    }

    private void copyPagesToTempTable() {
        String insertString = DBHelper.getColumnsStringFromArray(mIntersectionOldAndNewPageTableColumns);
        db.execSQL("insert into " + PERSONS_TEMP_TABLE + "(" + insertString + " ) " +
                "select " + insertString + " from " + TABLE_NAME);
    }

    private void renameTableSafe(String oldName, String newName) {
        setPragmaForeignKey(false);
        dropTable(oldName);
        renameTable(newName, oldName);
        setPragmaForeignKey(true);
    }

    private void setPragmaForeignKey(Boolean on) {
        db.execSQL("PRAGMA foreign_keys=" + (on ? "ON" : "OFF") + ";");
    }

    private void dropTable(String tableName) {
        db.execSQL("DROP TABLE IF EXISTS " + tableName);
    }

    private void renameTable(String oldName, String newName) {
        db.execSQL("ALTER TABLE " + oldName + " RENAME TO " + newName);
    }
}
