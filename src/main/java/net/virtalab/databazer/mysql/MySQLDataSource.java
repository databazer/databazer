package net.virtalab.databazer.mysql;

import net.virtalab.databazer.NamedDataSource;

import java.lang.reflect.Field;
import java.sql.Driver;
import java.util.*;

/**
 * The MySQLDataSource represents implementation of DataSource interface {@link javax.sql.DataSource} for MySQL Databases
 * <p>
 * By default this class defines some default values such as:
 * <ol>
 *     <li>MySQL Driver - we use {@link com.mysql.jdbc.Driver}. This is non-XA driver.</li>
 *     <li>Standard URL - {@code jdbc:mysql://localhost:3306/test}</li>
 *     <li>Username - {@code root}</li>
 *     <li>Password - empty password</li>
 *     <li>Name of DataSource - {@code default}</li>
 * </ol>
 * <p>
 * Those defaults can easily be overridden by using following methods:
 * <ol>
 *     <li>MySQL Driver - {@link #setDriverClass(Class) setDriverClass(Class)}</li>
 *     <li>URL - {@link #setUrl(String) setUrl(String)}</li>
 *     <li>Username - {@link #setUsername(String) setUsername(String)}</li>
 *     <li>Password - {@link #setPassword(String) setPassword(String)}</li>
 *     <li>Name of DataSource - {@link #setName(String) setName(String)}</li>
 * </ol>
 *
 * <p>
 * Alongside with default constructor there another way to create this DataSource.
 * It can be done using Builder pattern. This DataSource provides inner class <code>Creator</code>
 * For more information on Creator class see {@link MySQLDataSource.Creator its documentation}
 * <p>
 * Creator class can be accessed via two approaches:
 * <ol>
 *     <li>
 *         Creating new instance of Creator:
 *     <p><pre>
 *       MySQLDataSource.Creator creator = new MySQLDataSource.Creator();
 *     </pre>
 *     </li>
 *     <li>
 *         Static methods {@link #Creator()} and {@link #Creator(String) }
 *     <p><pre>
 *       MySQLDataSource.Creator = MySQLDataSource.Creator();
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
public class MySQLDataSource extends NamedDataSource {
    /**
     * Constructor invoked by Spring, this constructor sets default values.
     */
    public MySQLDataSource(){
        //set defaults
        this.setName("default");
        this.setDriverClass(com.mysql.jdbc.Driver.class);
        this.setUrl("jdbc:mysql://localhost:3306/test");
        this.setUsername("root");
        this.setPassword("");
    }

    /**
     * Provides {@link Creator} object in static way
     *
     * @return Creator instance
     */
    public static Creator Creator(){
        return new Creator();
    }

    /**
     * Provides {@link Creator} object with preset URL in static way
     *
     * @param url custom JDBC URL for MySQL
     * @return Creator instance
     */
    public static Creator Creator(String url){
        return new Creator(url);
    }

    /**
     * Class that creates MySQLDataSource using fluent interface approach.
     * Also it contains methods that allow to build JDBC URL step-by-step by setting each single component.
     * <p>
     * For example:
     * <p>
     * Instead of
     * <pre>
     *  String url = "jdbc:mysql://dbServer/myDatabase";
     *  MySQLDataSource ds = MySQLDataSource.Creator(url).create();
     * </pre><p>
     *  You can set each component method-by-method
     *  <pre>
     *   MySQLDataSource ds = MySQLDataSource.Creator().host("dbServer").databaseName("myDatabase").create();
     *  </pre>
     *  <p>
     *  There are some default values will be use (all of them can be overridden by Creator methods)
     *  <ol>
     *      <li>Name - "default"</li>
     *      <li>Host - "localhost"</li>
     *      <li>Port - 3306</li>
     *      <li>Database Name - "default"</li>
     *      <li>Username - "root"</li>
     *      <li>Password is empty</li>
     *      <li>Driver Class - {@link com.mysql.jdbc.Driver}</li>
     *  </ol>
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
        private static final String DEFAULT_HOST = "localhost";
        private static final int DEFAULT_PORT = 3306;
        private static final String DEFAULT_DBNAME = "default";
        private static final String DEFAULT_USER = "root";
        private static final String DEFAULT_PASSWORD = "";

        private static final Class<? extends Driver> DEFAULT_DRIVER = com.mysql.jdbc.Driver.class;

        private Map<String,Integer> hosts = new LinkedHashMap<String, Integer>();
        private Map<String,String> options = new HashMap<String, String>();

        //optional params
        String name = DEFAULT_NAME;
        String host = DEFAULT_HOST;
        int port = DEFAULT_PORT;
        String databaseName = DEFAULT_DBNAME;
        String username = DEFAULT_USER;
        String password = DEFAULT_PASSWORD;

        //params that override other optional params if set
        private Driver driver;
        private Class<? extends Driver> driverClass = DEFAULT_DRIVER;
        private String url;

        /**
         * Creates empty Creator object
         */
        public Creator(){}

        /**
         * Creates Creator object and sets custom URL.
         * Validity of custom URL is not a subject to check at Creator.
         *
         * @param url valid MySQL JDBC URL
         */
        public Creator(String url){
              this.url = url;
        }

        /**
         * DataSource {@link NamedDataSource#setName(String) name} which overrides default value.
         *
         * @param name DataSource name. Should be not empty String.
         * @return {@link #Creator() Creator} instance
         */
        public Creator name(String name){
            this.name =name;
            return this;
        }

        /**
         * Username which be used as login for DataSource
         *
         * @param username Username for accessing DB. Should be not empty String.
         * @return {@link #Creator() Creator} instance
         */
        public Creator username(String username){
            this.username = username;
            return this;
        }

        /**
         * Password which corresponds with {@link Creator#username(java.lang.String) login}
         *
         * @param password Password string. No limits applied here.
         * @return {@link #Creator() Creator} instance
         */
        public Creator password(String password){
            this.password = password;
            return this;
        }

        /**
         * Host is String that represents Hostname or IP address of Database server.
         * @param host Database server hostname or IP address. If hostname used, make sure that it can be resolved to IP, because this method or class does not provide such functionality.
         * @return {@link #Creator() Creator} instance
         */
        public Creator host(String host){
            this.host = host;
            return this;
        }

        /**
         * TCP Port that is listen by Database server.
         * Using this method makes sense only when port is non-standard.
         *
         * @param port Valid TCP port. Integer from 1 to 65535.
         * @return {@link #Creator() Creator} instance
         */
        public Creator port(int port){
            this.port = port;
            return this;
        }

        /**
         * Name of Database. Must already exist at Database Server.
         * @param database name of Database. Should be not empty String.
         *
         * @return {@link #Creator() Creator} instance
         */
        public Creator databaseName(String database){
            this.databaseName = database;
            return this;
        }

        /**
         * Failover host definition with non-standard port.
         * MySQL supports several database locations (host,port). This locations will be used when primary host is inaccessible.
         * <p>
         * This method allow to add second, third DB location to URL.
         * If there is no primary host defined by using {@link #host(String)},
         * first mentioned "failover host" will be primary host.
         *
         * @param host valid hostname or IP address. Should be not empty String.
         * @param port valid TCP port. Integer from 1 to 65535.
         * @return {@link #Creator() Creator} instance
         */
        public Creator failoverHost(String host,int port){
            if(this.hosts.size()==0){
                //add primary host first
                this.hosts.put(this.host,this.port);
                this.hosts.put(host,port);
            }
            if(this.hosts.size()>0){
                this.hosts.put(host,port);
            }
            return this;
        }

        /**
         * Failover host definition with standard port.
         * See {@link Creator#failoverHost(String, int)}
         */
        public Creator failoverHost(String host){
            this.failoverHost(host,DEFAULT_PORT);
            return this;
        }

        /**
         * Allows to pass Map with MySQL options.
         *
         * @param options Map with valid MySQL options. Should have at least one record in map, but quantity and option validity are out of scope of this method.
         * @return {@link Creator} instance
         */
        public Creator options(Map<String,String> options){
            this.options.putAll(options);
            return this;
        }

        /**
         * Allows to set options one-by-one. Order of options at generated URL not guaranteed.
         *
         * @param key Option name. Should be valid MySQL option. Also pay attention to MySQL version and if option is supported at this version.
         * @param value option values. Should be among valid values for corresponding option.
         * @return {@link Creator} instance
         */
        public Creator option(String key,String value){
            this.options.put(key,value);
            return this;
        }

        /**
         * Custom driver from provided driver instance.
         *
         * @param driver instance of class that implements {@link java.sql.Driver} interface. Driver should be capable for MySQL Database.
         * @return {@link Creator} instance
         */
        public Creator driver(Driver driver){
            this.driver = driver;
            //we cannot use both driver and driverClass at same time
            this.driverClass = null;
            return this;
        }

        /**
         * DataSource custom driver from provided Driver class.
         *
         * @param driver Class of Driver implementation. Driver should be capable for MySQL Database.
         * @return {@link Creator} instance
         */
        public Creator driver(Class<? extends Driver> driver){
            this.driverClass = driver;
            return this;
        }

        /**
         * Custom URL in valid for MySQL format. This custom URL is not validated and used as is.
         *
         * @param url custom JDBC URL for MySQL
         * @return {@link Creator} instance
         */
        public Creator url(String url){
            this.url = url;
            return this;
        }

        /**
         * Triggers generation of DataSource with params set by default and adjusted by methods of Creator.
         *
         * @return generated {@link MySQLDataSource}
         * @throws java.lang.IllegalArgumentException when provided components are out of their valid scope.
         * <p>
         *     Checked values are:
         * <ul>
         *     <li>Name - cannot be empty</li>
         *     <li>Database name - cannot be empty</li>
         *     <li>Hostname - cannot be empty</li>
         *     <li>Port - must be from 1 to 65535</li>
         * </ul>
         */
        public MySQLDataSource create(){
            //noinspection CaughtExceptionImmediatelyRethrown
            try{
                List<String> optionalFields = new ArrayList<String>();
                optionalFields.add("driver");
                optionalFields.add("driverClass");
                optionalFields.add("url");

                nullValidator(this,optionalFields);
            }catch (IllegalArgumentException e){
                throw e;
            }
            if(this.name.length()==0){
                throw new IllegalArgumentException("Empty name is not allowed");
            }
            if(this.databaseName.length()==0){
                throw new IllegalArgumentException("Empty database name is not allowed");
            }

            int MIN_PORT=1;
            int MAX_PORT=65535;

            if(this.hosts.size()>0){
                for(String host: this.hosts.keySet()){
                    if(host.length()==0){
                        throw new IllegalArgumentException("Empty hostname is not allowed");
                    }
                    int port = this.hosts.get(host);
                    if(port < MIN_PORT || port > MAX_PORT){
                        throw new IllegalArgumentException("Port cannot be less then "+MIN_PORT+" and more then "+MAX_PORT);
                    }
                }
            } else {
                if(this.host.length()==0){
                    throw new IllegalArgumentException("Empty hostname is not allowed");
                }
                if(this.port < MIN_PORT || this.port > MAX_PORT){
                    throw new IllegalArgumentException("Port cannot be less then "+MIN_PORT+" and more then "+MAX_PORT);
                }
            }
            return new MySQLDataSource(this);
        }

        /**
         * Validates if creator instance has null values at fields
         *
         * @param creator Creator instance
         * @param excludedFields field names that can be empty
         * @throws java.lang.IllegalArgumentException when catches null-values field
         */
        private void nullValidator(Creator creator,List<String> excludedFields) throws IllegalArgumentException {
            Class<?> c = creator.getClass();
            Field[] fields = c.getDeclaredFields();
            for(Field f: fields){
                String name;
                Object value;
                try {
                    name = f.getName();
                    value = f.get(creator);
                } catch (IllegalAccessException e) {
                    continue;
                }

                boolean nameExcluded = excludedFields.contains(name);
                if(value==null && !nameExcluded){
                    String message = f.getName()+" cannot be NULL";
                    throw new IllegalArgumentException(message);
                }
            }
        }
    }

    private MySQLDataSource(Creator creator){
        //connection name
        this.setName(creator.name);

        //drivers
        if(creator.driver!=null){ this.setDriver(creator.driver); }
        if(creator.driverClass!=null){ this.setDriverClass(creator.driverClass); }

        //user and pass
        this.setUsername(creator.username);
        this.setPassword(creator.password);

        //make url
        if(creator.url!=null){
            //just use URL
            this.setUrl(creator.url);
        } else {
            //well, we have build URL
            String url;
            StringBuilder sb = new StringBuilder();
            sb.append("jdbc:mysql://");

            //hosts
            if(creator.hosts.size()>0){
                //multiple hosts
                for(String host: creator.hosts.keySet()){
                    int port = creator.hosts.get(host);
                    sb.append(host);

                    if(port != Creator.DEFAULT_PORT ){
                        sb.append(":").append(port);
                    }

                    sb.append(","); //host delimiter
                }
                sb.setLength(sb.length()-1);
            } else{
                //single host
                sb.append(creator.host);
                if(creator.port != Creator.DEFAULT_PORT){
                    sb.append(":").append(creator.port);
                }
            }

            //database name
            sb.append("/"); //delimiter
            sb.append(creator.databaseName);

            //options
            if(creator.options.size()>0){
                sb.append("?");
                for (String key : creator.options.keySet()) {
                    String value = creator.options.get(key);

                    sb.append(key).append("=").append(value);
                    sb.append("&"); //delimiter
                }
                sb.setLength(sb.length()-1);
            }

            url = sb.toString();

            this.setUrl(url);
        }
    }
}
