package ru.startandroid.p0001androidstudy.model;

import android.support.annotation.Nullable;

import ru.startandroid.p0001androidstudy.db.PersonDao;

/**
 * Created by Work on 2/4/2016.
 */
public class Person {
    public long id = PersonDao.NULL_LONG;
    @Nullable
    public String fileName;
    public String name;
    public String email;

    public Person(long id, int face, String name, String email) {
        this(face, null, name, email);
        this.id = id;
    }

    public Person(int face, @Nullable String fileName, String name, String email) {
        this.fileName = fileName;
        this.name = name;
        this.email = email;
    }

    public Person(long id, @Nullable String fileName, String name, String email) {
        this.id = id;
        this.fileName = fileName;
        this.name = name;
        this.email = email;
    }

    public Person() {
        this.id = -1l;
        this.fileName = "";
        this.name = "";
        this.email = "";
    }
}
