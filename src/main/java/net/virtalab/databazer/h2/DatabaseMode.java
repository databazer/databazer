package net.virtalab.databazer.h2;

/**
 * Represents H2 Database running mode.
 * <p>
 * H2 can be run in several modes:
 * <ol>
 *     <li>Embedded DB stored in-memory (RAM) and not accessible outside.</li>
 *     <li>File DB accessible by application using filesystem calls and locks</li>
 *     <li>Embedded or file stored DB accessible by TCP or SSL transport via TCP/IP</li>
 * </ol>
 *
 * @author Alexander Muravya
 * @see net.virtalab.databazer.h2.H2DataSource
 * @version 0.1
 * @since 0.1
 */
public enum DatabaseMode {
    /**
     * In-Memory Database, not persistent, fastest ever mode.
     */
    MEMORY,
    /**
     * File Database, stores at file and accessible as file in FileSystem , normally used when no concurrent access needed.
     */
    FILE,
    /**
     * Server mode, {@link StorageType Storage} - file or memory, access by TCP/IP with plain text, multiple connections allowed.
     */
    TCP,
    /**
     * Server mode with SSL, same as TCP, but used for encrypted access via TCP/IP.
     */
    SSL
}
