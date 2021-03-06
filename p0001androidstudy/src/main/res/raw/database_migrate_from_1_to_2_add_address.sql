CREATE TABLE ADDRESSES(
    _ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    ADDRESS_STREET VARCHAR,
    ADDRESS_BUILDING INTEGER,
    ADDRESS_BLOCK INTEGER
    );

CREATE INDEX ADDRESSES__ID ON ADDRESSES (_ID);

CREATE TABLE PERSONS_TEMP (
    _ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    PERSON_NAME VARCHAR NOT NULL,
    PERSON_EMAIL VARCHAR NOT NULL,
    IMAGE_PATH VARCHAR,
    ADDRESS_ID INTEGER,
    FOREIGN KEY(ADDRESS_ID) REFERENCES ADDRESSES(_ID) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE INDEX PERSONS__ID ON PERSONS_TEMP (_ID);