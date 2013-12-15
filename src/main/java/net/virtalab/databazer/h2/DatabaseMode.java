package net.virtalab.databazer.h2;

/**
 * H2 Database running mode (variant)
 */
public enum DatabaseMode {
    /**
     * In-Memory Database. Not persistent. Fastest ever mode.
     */
    MEMORY,
    /**
     * File Database. Stores at file and accessible as file in FileSystem. Normally when no multiaccess needed.
     */
    FILE,
    /**
     * Server mode. Storage - file or memory. Access by TCP/IP. Multiple connections allowed.
     */
    TCP,
    /**
     * Server mode with SSL. For encrypted access thru TCP/IP.
     */
    SSL
}
