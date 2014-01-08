package net.virtalab.databazer.test.h2;

import net.virtalab.databazer.h2.H2DataSource;
import net.virtalab.databazer.h2.StorageType;
import net.virtalab.databazer.test.driver.DummyDriver;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for H2 Creator
 */
public class H2CreatorTest extends Assert {

    @Test
    public void defaults(){
        H2DataSource.Creator creator = H2DataSource.Creator();
        H2DataSource ds = creator.mem().create();

        String expectedURL = "jdbc:h2:mem:";
        String actualURL = ds.getUrl();
        assertEquals("URL ain't match",expectedURL,actualURL);

        String expectedUser = "sa";
        String actualUser = ds.getUsername();
        assertEquals("User ain't match",expectedUser,actualUser);

        String expectedPassword = "";
        String actualPassword = ds.getPassword();
        assertEquals("Password ain't match",expectedPassword,actualPassword);
    }

    @Test
    public void namedDataSource(){
        H2DataSource ds = H2DataSource.Creator().name("myDS").create();

        String exceptedName = "myDS";
        String actualName = ds.getName();

        assertEquals(exceptedName,actualName);
    }

    @SuppressWarnings("UnnecessaryLocalVariable")
    @Test
    public void userAndPasswordTest(){
        String user = "user";
        String pass = "passa";

        H2DataSource ds = new H2DataSource.Creator()
                .username(user).password(pass)
                .create();

        String exceptedUserName = user;
        String actualUserName = ds.getUsername();
        assertEquals("User Name",exceptedUserName,actualUserName);

        String exceptedPassword = pass;
        String actualPassword = ds.getPassword();
        assertEquals("Password",exceptedPassword,actualPassword);
    }

    @Test
    public void customURLPlusOptions(){
        String customURL = "jdbc:h2:mem:test";

        String key = "IFEXISTS";
        String value = "TRUE";

        H2DataSource ds = H2DataSource.Creator()
                .url(customURL)
                .option(key, value)
                .create();

        String exceptedURL = customURL+";IFEXISTS=TRUE";
        String actualURL = ds.getUrl();

        assertEquals(exceptedURL,actualURL);
    }

    @Test
    public void namedInMemory(){
        String dbName = "myDB";

        H2DataSource ds = H2DataSource.Creator()
                .mem().databaseName(dbName)
                .create();

        String exceptedURL = "jdbc:h2:mem:myDB";
        String actualURL = ds.getUrl();

        assertEquals(exceptedURL,actualURL);
    }

    @Test
    public void memPlusOptions(){
        String dbName = "memDB";

        String key = "IFEXISTS";
        String value = "TRUE";

        H2DataSource ds = H2DataSource.Creator()
                .mem().databaseName(dbName)
                .option(key,value)
                .create();

        String exceptedURL="jdbc:h2:mem:memDB;IFEXISTS=TRUE";
        String actualURL = ds.getUrl();

        assertEquals(exceptedURL,actualURL);

    }

    @Test
    public void fileDb(){
        String dbName = "myFileDb";
        String pathTo = "/opt";

        String key = "IFEXISTS";
        String value = "TRUE";

        H2DataSource ds = H2DataSource.Creator()
                .databaseName(dbName)
                .file().path(pathTo)
                .option(key,value)
                .create();

        String exceptedURL = "jdbc:h2:file:/opt/myFileDb;IFEXISTS=TRUE";
        String actualURL = ds.getUrl();

        assertEquals(exceptedURL,actualURL);
    }

    @Test
    public void correctMyPath(){
        String dbName = "myFileDb";
        String pathToDb = "/opt/";

        String key = "IFEXISTS";
        String value = "TRUE";

        H2DataSource ds = H2DataSource.Creator()
                .databaseName(dbName)
                .file().path(pathToDb)
                .option(key,value)
                .create();

        String exceptedURL = "jdbc:h2:file:/opt/myFileDb;IFEXISTS=TRUE";
        String actualURL = ds.getUrl();

        assertEquals(exceptedURL,actualURL);
    }

    @Test
    public void tcpServerWithCustomPort(){
        String host = "localhost";
        int port = 10000;
        String dbName = "tcpMem";

        String key = "IFEXISTS";
        String value = "TRUE";

        H2DataSource ds = H2DataSource.Creator()
                .tcp().server(host,port)
                .databaseName(dbName)
                .storageType(StorageType.MEMORY)
                .option(key,value)
                .create();

        String exceptedURL = "jdbc:h2:tcp://localhost:10000/mem:tcpMem;IFEXISTS=TRUE";
        String actualURL = ds.getUrl();

        assertEquals(exceptedURL,actualURL);

    }

    @Test
    public void tcpServerWithFileStorage(){
        String host = "localhost";
        String dbName = "tcpFileDb";
        String pathToDb = "/opt";

        String key = "IFEXISTS";
        String value = "TRUE";

        H2DataSource ds = H2DataSource.Creator()
                .tcp().server(host)
                .databaseName(dbName)
                .storageType(StorageType.FILE).path(pathToDb)
                .option(key,value)
                .create();

        String exceptedURL = "jdbc:h2:tcp://localhost/opt/tcpFileDb;IFEXISTS=TRUE";
        String actualURL = ds.getUrl();

        assertEquals(exceptedURL,actualURL);

    }

    @Test
    public void sslServerWithFileStore(){
        String host = "localhost";
        String dbName = "tcpFileDb";
        String pathToDb = "/opt";

        String key = "IFEXISTS";
        String value = "TRUE";

        H2DataSource ds = H2DataSource.Creator()
                .ssl().server(host)
                .databaseName(dbName)
                .storageType(StorageType.FILE).path(pathToDb)
                .option(key,value)
                .create();


        String exceptedURL = "jdbc:h2:ssl://localhost/opt/tcpFileDb;IFEXISTS=TRUE";
        String actualURL = ds.getUrl();

        assertEquals(exceptedURL,actualURL);
    }

    @Test
    public void DSWithTwoOptions(){
        String dbName= "testDb";

        String dbCloseKey = "DB_CLOSE_ON_EXIT";
        String dbCloseValue = "TRUE";

        String modeKey = "MODE";
        String modeValue = "MYSQL";

        H2DataSource ds = H2DataSource.Creator()
                .mem().databaseName(dbName)
                .option(dbCloseKey,dbCloseValue)
                .option(modeKey, modeValue)
                .create();

        String expectedURL = "jdbc:h2:mem:testDb;DB_CLOSE_ON_EXIT=TRUE;MODE=MYSQL";
        String actualURL = ds.getUrl();

        assertEquals(expectedURL,actualURL);
    }

    @Test
    public void anotherDriverClass(){
        //using "alternative" (dummy) Driver
        H2DataSource ds = H2DataSource.Creator()
                .driver(DummyDriver.class)
                .create();

        Class expectedDriverClass = DummyDriver.class;
        Class actualDriverClass = ds.getDriver().getClass();

        assertEquals(expectedDriverClass,actualDriverClass);
    }

    @Test
    public void anotherDriver(){
        DummyDriver dummyDriver = new DummyDriver();
        H2DataSource ds = H2DataSource.Creator()
                .driver(dummyDriver)
                .create();

        Class expectedDriverClass = DummyDriver.class;
        Class actualDriverClass = ds.getDriver().getClass();

        assertEquals(expectedDriverClass,actualDriverClass);
    }

    @Test(expected = IllegalArgumentException.class)
    public void wrongName(){
       H2DataSource.Creator().name("").create();
    }

    @Test(expected = IllegalArgumentException.class)
    public void wrongHost(){
        H2DataSource.Creator().tcp().server("").mem().create();
    }

    @Test(expected = IllegalArgumentException.class)
    public void wrongPort(){
        H2DataSource.Creator().server("localhost", 80000).mem().create();
    }

    @Test(expected = IllegalStateException.class)
    public void noDbNameAtFileMode(){
        H2DataSource.Creator().file().create();
    }

    @Test(expected = IllegalStateException.class)
    public void emptyDbNameAtFileMode(){
        H2DataSource.Creator().file().databaseName("").create();
    }

    @Test(expected = IllegalStateException.class)
    public void serverModeWithoutDatabaseName(){
        H2DataSource.Creator().server("localhost").storageType(StorageType.FILE).create();
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullServer(){
        H2DataSource.Creator()
                .server(null)
                .databaseName("db")
                .create();
    }

    @Test(expected = IllegalStateException.class)
    public void inMemoryDBWithFileStorage(){
        H2DataSource.Creator().mem().storageType(StorageType.FILE).create();
    }

    @Test(expected = IllegalStateException.class)
    public void fileDBWithMemoryStorage(){
        H2DataSource.Creator().file().storageType(StorageType.MEMORY).create();
    }
}
