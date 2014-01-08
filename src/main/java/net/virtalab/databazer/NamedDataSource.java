package net.virtalab.databazer;

import org.springframework.jdbc.datasource.SimpleDriverDataSource;

/**
 * The NamedDataSource is {@link org.springframework.jdbc.datasource.SimpleDriverDataSource} from Spring Framework with name.
 * </br>
 * Name field is only addition done in this class.
 * </br>
 * </br>
 * Where is name of DataSource useful?
 * <p>
 * Name can be useful when dealing with ActiveJDBC DB class. They require name when using more that one connection.
 * <p>
 * <p>
 * Is it compulsory to set name when using this class ?
 * <p>
 * No. But if you use {@link #getName()} without calling {@link #setName(String)} before, {@link java.lang.NullPointerException} will be thrown.
 * <p>
 * By design all concrete DataSources in Databazer extend this class.
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
     * Name is private field in this class,
     * so this method is proper way to set name of newly created DataSource.
     * This class does not check if provided name is already used somewhere in application.
     * So you have to implement this check at your application, if needed.
     *
     * @param name DataSource name. No check is implemented here,
     *             but it recommended no do pass empty String here
     */
    public void setName(String name) {
        this.name = name;
    }
}
