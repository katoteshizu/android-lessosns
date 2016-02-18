package ru.startandroid.p0001androidstudy.model;

import ru.startandroid.p0001androidstudy.db.AddressDao;

/**
 * @author by Andrey on 2/15/2016.
 */
public class Address {
    public long id = AddressDao.NULL_LONG;
    public String street;
    public int building;
    public int block;

    public Address(long id, String street, int building, int block) {
        this.id = id;
        this.street = street;
        this.building = building;
        this.block = block;
    }

    public Address() {
        this.id = -1l;
        this.street = "";
        this.building = -1;
        this.block = -1;
    }
}
