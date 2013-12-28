package net.virtalab.databazer.h2;

import net.virtalab.databazer.NamedDataSource;

import java.sql.Driver;
import java.util.HashMap;
import java.util.Map;

/**
 * DataSource for H2 Databases
 */

public class H2DataSource extends NamedDataSource {
    /**
     * Constructor invoked by Spring when creating bean. Sets default values.
     */
    public H2DataSource(){
        this.setName("default");
        this.setDriverClass(org.h2.Driver.class);
        this.setUrl("jdbc:h2:mem:");
        this.setUsername("sa");
        this.setPassword("");
    }

    /**
     * Provides Creator in static way
     *
     * @return Creator instance
     */
    public static Creator Creator(){
        return new Creator();
    }

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

        public Creator(){}

        /**
         * Constructs DataSource from provided connection URL
         *
         * @param url JDBC URL for H2 (@see http://h2database.com for more info)
         * @return Creator object
         */
        public Creator url(String url){
             this.url = url;
            return this;
        }

        public Creator name(String name){
             this.name = name;
            return this;
        }

        /**
         * Sets Memory Database
         * @return Creator
         */
        public Creator mem(){
            this.mode = DatabaseMode.MEMORY;
            this.storageType = StorageType.MEMORY;
            return this;
        }

        /**
         * Sets File Mode for Database
         * @return Creator
         */
        public Creator file(){
            this.mode = DatabaseMode.FILE;
            this.storageType = StorageType.FILE;
            return this;
        }

        /**
         * Sets Server Mode for Database
         * @return Creator
         */
        public Creator tcp(){
            this.mode = DatabaseMode.TCP;
            return this;
        }

        /**
         * Sets SSL Mode for Database
         * @return Creator
         */
        public Creator ssl(){
            this.mode = DatabaseMode.SSL;
            return this;
        }

        /**
         * Sets name of database.
         * For memory DB this is named in-memory db.
         * For File and Server+file is just database name without path to file.
         *
         * @param databaseName name of database
         * @return Creator
         */
        public Creator databaseName(String databaseName){
            this.databaseName = databaseName;
            return this;
        }

        /**
         * Sets path to Database file.
         * Path should not end with "/" (it added automatically)
         * Path could be relative to running directory. (Example ../db. Interpreted as: goto one level above running directory and goto db there)
         * Path could be relative to user home (*nix only) (Example: ~/db. Interpreted as: /home/<user_who_running_java_program>/db)
         * Path could be absolute to / (*nix only) (Example: /opt/db. Interpreted as: /opt/db)
         * Path could be absolute to drive (Windows only) (Example: C:/db . Attention: Use "/" here.)
         *
         * @param path filesystem path to database file
         * @return Creator
         */
        public Creator path(String path){
            this.path = path;
            return this;
        }

        /**
         * Sets hostname:port server running on
         *
         * @param host hostname or IP
         * @param port TCP Port.
         * @return Creator
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
         * Sets hostname and default port
         * @param host hostname or IP
         * @return Creator
         */
        public Creator server(String host){
            this.server(host,DEFAULT_PORT);
            return this;
        }

        /**
         * Sets Storage Type (@see StorageType) of Server accessible (TCP or SSL) Database
         *
         * @param storageType valid Storage
         * @return Creator
         */
        public Creator storageType(StorageType storageType){
            this.storageType = storageType;
            return this;
        }

        /**
         * Sets username
         * @param username username used to connect to DB
         * @return Creator
         */
        public Creator username(String username){
            this.username = username;
            return this;
        }

        /**
         * Sets password
         * @param password password used to connect to DB
         * @return Creator
         */
        public Creator password(String password){
            this.password = password;
            return this;
        }

        /**
         * Adds option to end of connection URL
         * @param key option name
         * @param value option value
         * @return Creator
         */
        public Creator option(String key,String value){
            this.options.put(key,value);
            return this;
        }

        /**
         * Sets custom driver. Do not use this unless you know what you're doing.
         * @param driver Driver object
         * @return Creator
         */
        public Creator driver(Driver driver){
            this.driver = driver;
            //null driverClass as we cannot use both at same time
            this.driverClass = null;

            return this;
        }

        /**
         * Sets custom driver with driver class. Do not use this unless you know what you're doing.
         * @param driverClass Class that extends java.sql.Driver
         * @return Creator
         */
        public Creator driver(Class<? extends Driver> driverClass){
            this.driverClass = driverClass;
            return this;
        }

        /**
         * Triggers DataSource creation
         * @return DataSource for H2 DB
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
