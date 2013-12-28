package net.virtalab.databazer;

import org.springframework.jdbc.datasource.SimpleDriverDataSource;

/**
 * SimpleDriverDataSource with name. This is base very simple class.
 * All concrete DB DataSources in Databazer extend this
 */
public class NamedDataSource extends SimpleDriverDataSource {
    /**
     * Name of DataSource
     */
    private String name;

    /**
     * Reports name of current DataSource
     *
     * @return name of DataSource
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for Name
     *
     * @param name DataSource name
     */
    public void setName(String name) {
        this.name = name;
    }
}
