package ru.startandroid.p0001androidstudy;

import ru.startandroid.p0001androidstudy.model.Person;

/**
 * Created by Work on 2/4/2016.
 */
public interface PersonListener {
    void onPersonUpdated(Person person);
    void onPersonDelete(Person person);
}
