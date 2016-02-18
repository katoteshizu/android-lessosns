package ru.startandroid.p0001androidstudy;

import ru.startandroid.p0001androidstudy.model.Address;

/**
 * @author by Andrey on 2/15/2016.
 */
public interface AddressListener {
    void onAddressUpdated(Address person);
    void onAddressDelete(Address person);
}
