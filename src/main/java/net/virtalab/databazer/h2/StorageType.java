package net.virtalab.databazer.h2;

/**
 * Location of DB accessible by TCP or SSL
 */
public enum  StorageType {
    /**
     * In-memory DB
     */
    MEMORY,
    /**
     * DB in File
     */
    FILE

}
