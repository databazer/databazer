package net.virtalab.databazer.h2;

/**
 * Physical location of H2 Database which is accessible over TCP or SSL (aka ServerMode)
 *
 * @author Alexander Muravya
 * @see net.virtalab.databazer.h2.H2DataSource
 * @version 0.1
 * @since 0.1
 */
public enum  StorageType {
    /**
     * In-memory Database. No persistent RAM DB.
     */
    MEMORY,
    /**
     * Database stored in file
     */
    FILE

}
