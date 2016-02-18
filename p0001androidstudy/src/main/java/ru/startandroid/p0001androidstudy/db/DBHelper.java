package ru.startandroid.p0001androidstudy.db;

import utilities.NullValues;

/**
 * @author by Andrey on 2/15/2016.
 */
public class DBHelper {
    public static String getColumnsStringFromArray(String[] columns) {
        return getColumnsStringFromArray(columns, null);
    }

    public static String getColumnsStringFromArray(String[] columns, String alias) {
        StringBuilder builder = new StringBuilder();
        for (String column : columns) {
            if (alias != null && !alias.equals(NullValues.NULL_STRING)) {
                builder.append(alias).append('.');
            }
            builder.append(column).append(", ");
        }
        return builder.substring(0, builder.length() - 2);
    }
}
