package net.virtalab.databazer.mysql;

import net.virtalab.databazer.NamedDataSource;

import java.sql.Driver;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * DataSource for MySQL Databases
 */

public class MySQLDataSource extends NamedDataSource {
    /**
     * Constructor invoked by Spring and sets default values
     */
    public MySQLDataSource(){
        //set defaults
        this.setName("default");
        this.setDriverClass(com.mysql.jdbc.Driver.class);
        this.setUrl("jdbc:mysql://localhost:3306/test");
        this.setUsername("root");
        this.setPassword("");
    }

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

        public Creator(){}

        public Creator(String url){
              this.url = url;
        }

        public Creator name(String name){
            this.name =name;
            return this;
        }

        public Creator username(String username){
            this.username = username;
            return this;
        }

        public Creator password(String password){
            this.password = password;
            return this;
        }

        public Creator host(String host){
            this.host = host;
            return this;
        }

        public Creator port(int port){
            this.port = port;
            return this;
        }

        public Creator databaseName(String database){
            this.databaseName = database;
            return this;
        }

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

        public Creator failoverHost(String host){
            this.failoverHost(host,DEFAULT_PORT);
            return this;
        }

        public Creator options(Map<String,String> options){
            this.options.putAll(options);
            return this;
        }

        public Creator option(String key,String value){
            this.options.put(key,value);
            return this;
        }

        public Creator driver(Driver driver){
            this.driver =driver;
            return this;
        }
        public Creator driver(Class<? extends Driver> driver){
            this.driverClass = driver;
            return this;
        }
        public Creator url(String url){
            this.url = url;
            return this;
        }

        public MySQLDataSource create(){
           return new MySQLDataSource(this);
        }
    }

    private MySQLDataSource(Creator creator){
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
                    sb.append(host).append(":").append(port);
                    sb.append(","); //host delimiter
                }
                sb.setLength(sb.length()-1);
            } else{
                //single host
                sb.append(creator.host).append(":").append(creator.port);
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
