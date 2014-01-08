package net.virtalab.databazer.h2;

import net.virtalab.databazer.NamedDataSource;

import java.sql.Driver;
import java.util.HashMap;
import java.util.Map;

/**
 * The H2DataSource represents implementation of DataSource interface {@link javax.sql.DataSource} for H2 Databases
 * <p>
 * By default this class defines some default values such as:
 * <ol>
 *     <li>Name of DataSource - {@code default}</li>
 *     <li>Driver class - official H2 Driver: {@link org.h2.Driver}.</li>
 *     <li>URL - {@code jdbc:h2:mem:} which is private (unnamed) in-memory DB</li>
 *     <li>Username - {@code sa}</li>
 *     <li>Password - empty password</li>
 *
 * </ol>
 * <p>
 * Those defaults can easily be overridden by using following methods:
 * <ol>
 *     <li>Name of DataSource - {@link #setName(String) setName(String)}</li>
 *     <li>Driver class - {@link #setDriverClass(Class) setDriverClass(Class)}</li>
 *     <li>URL - {@link #setUrl(String) setUrl(String)}</li>
 *     <li>Username - {@link #setUsername(String) setUsername(String)}</li>
 *     <li>Password - {@link #setPassword(String) setPassword(String)}</li>
 * </ol>
 *
 * <p>
 * Alongside with default constructor there another way to create this DataSource.
 * It can be done using Builder pattern. This DataSource provides inner class <code>Creator</code>
 * For more information on Creator class see {@link H2DataSource.Creator its documentation}
 * <p>
 * Creator class can be accessed via two approaches:
 * <ol>
 *     <li>
 *         Creating new instance of Creator:
 *     <p><pre>
 *       H2DataSource.Creator creator = new H2DataSource.Creator();
 *     </pre>
 *     </li>
 *     <li>
 *         Static method {@link #Creator()}
 *     <p><pre>
 *       H2DataSource.Creator = H2DataSource.Creator();
 *     </pre>
 *     </li>
 * </ol>
 *
 *
 * @author Alexander Muravya
 * @see net.virtalab.databazer.NamedDataSource
 * @version 0.1
 * @since 0.1
 */
public class H2DataSource extends NamedDataSource {
    /**
     * Constructor invoked by Spring when creating bean, sets default values.
     */
    public H2DataSource(){
        this.setName("default");
        this.setDriverClass(org.h2.Driver.class);
        this.setUrl("jdbc:h2:mem:");
        this.setUsername("sa");
        this.setPassword("");
    }

    /**
     * Provides {@link Creator} instance in static way
     *
     * @return Creator instance
     */
    public static Creator Creator(){
        return new Creator();
    }

    /**
     * Class that creates H2DataSource using fluent interface approach.
     * It contains methods that allow to build JDBC URL step-by-step by setting each single component.
     * <p>
     * For example:
     * <p>
     * Instead of
     * <pre>
     *  String url = "jdbc:h2:mem:dbName";
     *  H2DataSource ds = new H2DataSource();
     *  ds.setUrl(url);
     * </pre><p>
     *  You can set each component method-by-method
     *  <pre>
     *   H2DataSource ds = H2DataSource.Creator().databaseName("dbName").create();
     *  </pre>
     *  <p>
     *  There are some default values will be use (all of them can be overridden by Creator methods)
     *  <ol>
     *      <li>Name - "default"</li>
     *      <li>Driver Class - {@link org.h2.Driver org.h2.Driver}</li>
     *      <li>Path to DB - empty path</li>
     *      <li>Database Name - empty</li>
     *      <li>Host - "localhost"</li>
     *      <li>Port - 9092</li>
     *      <li>Username - "sa"</li>
     *      <li>Password is empty</li>
     *      <li>{@link DatabaseMode Database Mode} - Memory</li>
     *      <li>{@link StorageType Storage Type} - Memory</li>
     *  </ol>
     *  <p>
     *      Please note that:
     *  <p>
     *  <ul>
     *      <li>Empty Database name is accepted only for {@link StorageType#MEMORY in-memory database}</li>
     *      <li>Path is ignored while building in-memory database</li>
     *      <li>Host and port settings are ignored in {@link DatabaseMode#MEMORY in-memory} or {@link DatabaseMode#FILE file} database modes</li>
     *      <li>If you try to set multiple {@link DatabaseMode modes} i.e. memory and then file, last set mode wins.</li>
     *  </ul>
     *  <p>
     *  Customization note
     *  <p>
     *  Custom URL (set by using {@link #setUrl(String) setUrl(String)}) overrides all other URL-related settings
     *  <p>
     *  @version 0.1
     *  @since 0.1
     *  @author Alex Muravya
     *
     */
    public static class Creator{
        //defaults
        private static final String DEFAULT_NAME = "default";
        private static final Class<? extends Driver> DEFAULT_DRIVER = org.h2.Driver.class;

        private static final String DEFAULT_PATH = "";
        private static final String DEFAULT_DBNAME= "";

        private static final String DEFAULT_HOST = "localhost";
        private static final int DEFAULT_PORT = 9092;

        private static final String DEFAULT_USER="sa";
        private static final String DEFAULT_PASSWORD="";

        private Map<String,String> options = new HashMap<String, String>();

        //fields
        String name = DEFAULT_NAME;

        String path = DEFAULT_PATH;
        String databaseName = DEFAULT_DBNAME;

        String host = DEFAULT_HOST;
        int port = DEFAULT_PORT;

        String username = DEFAULT_USER;
        String password = DEFAULT_PASSWORD;

        DatabaseMode mode = DatabaseMode.MEMORY;
        StorageType storageType = StorageType.MEMORY;

        Driver driver;
        Class<? extends Driver> driverClass = DEFAULT_DRIVER;

        String url;

        /**
         * Constructs DataSource with defaults
         */
        public Creator(){}

        /**
         * Constructs DataSource from provided connection URL.
         * If used, overrides all URL-related settings.
         *
         * @param url JDBC URL for H2 (http://h2database.com for more info)
         * @return {@link #Creator() Creator} instance
         */
        public Creator url(String url){
             this.url = url;
            return this;
        }

        /**
         * DataSource {@link NamedDataSource#setName(String) name} which overrides default value.
         *
         * @param name DataSource name. Should be not empty String.
         * @return {@link #Creator() Creator} instance
         */
        public Creator name(String name){
             this.name = name;
            return this;
        }

        /**
         * In-Memory mode for database.
         * Database will be located at memory.
         * This is fastest mode, but no persistence provided.
         *
         * @return {@link #Creator() Creator} instance
         */
        public Creator mem(){
            this.mode = DatabaseMode.MEMORY;
            this.storageType = StorageType.MEMORY;
            return this;
        }

        /**
         * File mode for database.
         * Database stores at file and can be accessed only by one process at moment.
         *
         * @return {@link #Creator() Creator} instance
         */
        public Creator file(){
            this.mode = DatabaseMode.FILE;
            this.storageType = StorageType.FILE;
            return this;
        }

        /**
         * TCP mode for database.
         * Newly created DB can be accessed via TCP/IP.
         * You can adjust {@link #server(String, int)} host and port} if needed.
         *
         * @return {@link #Creator() Creator} instance
         */
        public Creator tcp(){
            this.mode = DatabaseMode.TCP;
            return this;
        }

        /**
         * SSL Mode for Database.
         * SSL Mode if same as TCP, but provides Secure transport over TCP/IP stack.
         * Host and port are also {@link #server(String, int) adjustable} for this mode as well.
         *
         * @return {@link #Creator() Creator} instance
         */
        public Creator ssl(){
            this.mode = DatabaseMode.SSL;
            return this;
        }

        /**
         * Defines database name.
         * For memory DB this is named in-memory db.
         * For File and Server+file is just database name without path to file.
         * If no {@link #path(String) path specified} H2 engine will search (or create) DB at current working directory
         *
         * @param databaseName database name. Should be 1 or more letters (without slashes and spaces).
         * @return {@link #Creator() Creator} instance
         */
        public Creator databaseName(String databaseName){
            this.databaseName = databaseName;
            return this;
        }

        /**
         * Defines path to Database file.
         * <P>
         * Path should not end with "/" (it added automatically)
         * <P>
         * Path could be relative to running directory. (Example ../db. Interpreted as: goto one level above running directory and goto db there)
         * <P>
         * Path could be relative to user home (*nix only) (Example: ~/db. Interpreted as: /home/<user_who_running_java_program>/db)
         * <P>
         * Path could be absolute to / (*nix only) (Example: /opt/db. Interpreted as: /opt/db)
         * <P>
         * Path could be absolute to drive (Windows only) (Example: C:/db . Attention: Use "/" here.)
         *
         * @param path filesystem path to database file.
         * @return {@link #Creator() Creator} instance
         */
        public Creator path(String path){
            this.path = path;
            return this;
        }

        /**
         * Server is combination of hostname and port.
         * If DatabaseMode is not equals to {@link DatabaseMode#TCP TCP} or {@link DatabaseMode#SSL SSL} this setting ignored.
         *
         * @param host hostname, which can be resolved to IP address or IP as String
         * @param port Integer representing valid TCP port (from 1 to 65535)
         * @return {@link #Creator() Creator} instance
         */
        public Creator server(String host, int port){
            this.host = host;
            this.port = port;
            //if server used - user need TCP or SSL
            if(this.mode!=DatabaseMode.SSL){
                this.mode = DatabaseMode.TCP;
            }
            return this;
        }

        /**
         * {@link #server(String, int)} with default port
         *
         * @param host hostname, which can be resolved to IP address or IP as String
         * @return {@link #Creator() Creator} instance
         */
        public Creator server(String host){
            this.server(host,DEFAULT_PORT);
            return this;
        }

        /**
         * Sets {@link StorageType Storage Type} of Server accessible (TCP or SSL) Database
         *
         * @param storageType valid Storage
         * @return {@link #Creator() Creator} instance
         */
        public Creator storageType(StorageType storageType){
            this.storageType = storageType;
            return this;
        }

        /**
         * Username which be used as login for DataSource
         *
         * @param username username used to connect to DB. Must not be empty.
         * @return {@link #Creator() Creator} instance
         */
        public Creator username(String username){
            this.username = username;
            return this;
        }

        /**
         * Password which corresponds with {@link Creator#username(java.lang.String) login}
         *
         * @param password password used to connect to DB. No limitation applied here.
         * @return {@link #Creator() Creator} instance
         */
        public Creator password(String password){
            this.password = password;
            return this;
        }

        /**
         * Adds option to end of connection URL.
         * List of options can be found at H2 Database site.
         *
         * @param key option name
         * @param value option value
         * @return {@link #Creator() Creator} instance
         */
        public Creator option(String key,String value){
            this.options.put(key,value);
            return this;
        }

        /**
         * Overrides driver setting with custom driver instance.
         * Do not use this unless you know what you're doing.
         *
         * @param driver Object of class that implements {@link java.sql.Driver} for H2 Database
         * @return {@link #Creator() Creator} instance
         */
        public Creator driver(Driver driver){
            this.driver = driver;
            //null driverClass as we cannot use both at same time
            this.driverClass = null;

            return this;
        }

        /**
         * Overrides driver class with custom driver class.
         * Do not use this unless you know what you're doing.
         *
         * @param driverClass Class that implements {@link java.sql.Driver} and able to work with H2 Database
         * @return Creator
         */
        public Creator driver(Class<? extends Driver> driverClass){
            this.driverClass = driverClass;
            return this;
        }

        /**
         * Triggers DataSource creation.
         *
         * @return generated {@link H2DataSource}
         * @throws java.lang.IllegalArgumentException when argument value out of valid scope
         * @throws java.lang.IllegalStateException when DB with settings provided settings cannot be created.
         * For example:
         * <ul>
         * <li>Empty database name for {@link StorageType#FILE File storage mode}</li>
         * </ul>
         */
        public H2DataSource create(){
            //check if we have all required params set
            boolean isURLDefined = (this.url !=null);
            boolean isDatabaseNameSet = (! this.databaseName.equals(DEFAULT_DBNAME));
            boolean isStoredInMemory = (this.storageType == StorageType.MEMORY);
            if(!isURLDefined){
                if( ! isDatabaseNameSet && ! isStoredInMemory){
                    throw new IllegalStateException("You didn't set database name. Noname DB is allowed only for memory mode or Server+Memory mode." +
                        "Use databaseName() or change mode to Memory by mem(), or storageType(StorageType.MEMORY) for server mode ");
                }
            }

            if(this.name.length()==0){
                throw new IllegalArgumentException("Empty name is not allowed");
            }

            if(this.host.length()==0){
                throw new IllegalArgumentException("Empty hostname is not allowed");
            }

            int MIN_PORT=1;
            int MAX_PORT=65535;

            if(port < MIN_PORT || port > MAX_PORT){
                throw new IllegalArgumentException("Port cannot be less then "+MIN_PORT+" and more then "+MAX_PORT);
            }

            return new H2DataSource(this);
        }
    }

    /**
     * Private constructor which creates object from its builder.
     *
     * @param creator Creator instance
     */
    private H2DataSource(Creator creator){
        //connection name
        this.setName(creator.name);

        //drivers
        if(creator.driver!=null){ this.setDriver(creator.driver); }
        if(creator.driverClass!=null){ this.setDriverClass(creator.driverClass); }

        //user and pass
        this.setUsername(creator.username);
        this.setPassword(creator.password);

        //making connection url
        StringBuilder URLBuilder = new StringBuilder();
        if(creator.url!=null){
            //we just use it
            URLBuilder.append(creator.url);
           //do we have options set?
           if(creator.options.size() > 0){
                this.addOptions(creator, URLBuilder);
           }
        } else {
            //ok, we have to build URL step-by-step
            URLBuilder.append("jdbc:h2:");
            //further steps are highly dependent on DB mode
            switch (creator.mode){
                case MEMORY:
                    URLBuilder.append("mem:");
                    URLBuilder.append(createMemoryDB(creator));
                    break;
                case FILE:
                    URLBuilder.append("file:");
                    URLBuilder.append(createFileDB(creator));
                    break;
                case TCP:
                    URLBuilder.append("tcp://");
                    URLBuilder.append(createServerDB(creator));
                    break;
                case SSL:
                    URLBuilder.append("ssl://");
                    URLBuilder.append(createServerDB(creator));
                    break;
                default:
                    //hope we never be here due to pre-set defaults
                    //but if so - we scream and exit in panic
                    return;
            }
            //options
            if(creator.options.size() > 0){
                 this.addOptions(creator, URLBuilder);
            }
        }
        this.setUrl(URLBuilder.toString());
    }

    private String createMemoryDB(Creator creator){
        StringBuilder builder = new StringBuilder();
        //adding prefix "mem" (but not for MEMORY mode DB, because it already has it)
        if(creator.mode != DatabaseMode.MEMORY){
            //delimiter
            builder.append("/");
            String prefix = "mem:";
            builder.append(prefix);
        }
        //check if have named or un-named memory DB
        if(! creator.databaseName.equals(Creator.DEFAULT_DBNAME)){
             builder.append(creator.databaseName);
        }
        return builder.toString();
    }

    private String createFileDB(Creator creator){
        StringBuilder builder = new StringBuilder();
        //path
        builder.append(creator.path);
        //delimiter
        //we need it only when path is not already ends with / and path is not empty
        if(! creator.path.endsWith("/") && ! creator.path.isEmpty() ){
            builder.append("/");
        }
        //database name
        builder.append(creator.databaseName);

        return builder.toString();
    }

    private String createServerDB(Creator creator){
        StringBuilder builder = new StringBuilder();

        //host
        builder.append(creator.host);
        //port
        if(creator.port!=Creator.DEFAULT_PORT){
            builder.append(":").append(creator.port);
        }
        switch (creator.storageType){
            case MEMORY:
                builder.append(createMemoryDB(creator));
                break;
            case FILE:
                builder.append(createFileDB(creator));
                break;
            default:
                break;
        }
        return builder.toString();
    }

    private StringBuilder addOptions(Creator creator,StringBuilder URLBuilder){
        URLBuilder.append(";"); //options delimiter
        for (String key : creator.options.keySet()) {
            String value = creator.options.get(key);
            URLBuilder.append(key).append("=").append(value);
            URLBuilder.append(";"); //delimiter
        }
        //remove trailing delimiter
        URLBuilder.setLength(URLBuilder.length()-1);
        return URLBuilder;
    }
}
