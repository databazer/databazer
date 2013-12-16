package net.virtalab.databazer.test.h2;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for H2 Creator
 */
public class H2CreatorTest extends Assert {

    @Test
    public void defaults(){
        //URL
        String expectedURL = "jdbc:h2:mem:";
        //TODO stub
        String actualURL = "";
        assertEquals("URL ain't match",expectedURL,actualURL);

        //User
        String expectedUser = "sa";
        //TODO stub
        String actualUser = "";
        assertEquals("User ain't match",expectedURL,actualURL);
        //Password
        String expectedPassword = "";
        String actualPassword = "";
        assertEquals("Password ain't match",expectedPassword,actualPassword);
    }

    @Test
    public void namedDataSource(){
        String exceptedName = "myDS";
        //TODO stub
        String actualName = "";
        assertEquals(exceptedName,actualName);
    }

    @Test
    public void customURLPlusOptions(){
        String customURL = "jdbc:h2:mem:test";
        //option
        String key = "IFEXISTS";
        String value = "TRUE";

        //expected
        String exceptedURL = customURL+";IFEXISTS=TRUE;";
        //TODO stub
        String actualURL = "";
        assertEquals(exceptedURL,actualURL);
    }

    @Test
    public void namedInMemory(){
        String dbName = "myDB";
        String exceptedURL = "jdbc:h2:mem:myDB";
        //TODO stub
        String actualURL = "";
        assertEquals(exceptedURL,actualURL);
    }

    @Test
    public void memPlusOptions(){
        String dbName = "memDB";

        //option
        String key = "IFEXISTS";
        String value = "TRUE";

        String exceptedURL="jdbc:h2:mem:memDB;IFEXISTS=TRUE;";

        //TODO stub
        String actualURL = "";
        assertEquals(exceptedURL,actualURL);

    }

    @Test
    public void fileDb(){
        String dbName = "myFileDb";
        String pathTo = "/opt";

        //option
        String key = "IFEXISTS";
        String value = "TRUE";

        String exceptedURL = "jdbc:h2:file:/opt/myFileDb;IFEXISTS=TRUE;";
        //TODO stub
        String actualURL = "";
        assertEquals(exceptedURL,actualURL);
    }

    @Test
    public void correctMyPath(){
        String dbName = "myFileDb";
        String pathToDb = "/opt/";

        //option
        String key = "IFEXISTS";
        String value = "TRUE";

        String exceptedURL = "jdbc:h2:file:/opt/myFileDb;IFEXISTS=TRUE;";
        //TODO stub
        String actualURL = "";
        assertEquals(exceptedURL,actualURL);
    }

    @Test
    public void tcpServerWithMemoryStore(){
        String host = "localhost";
        int port = 10000;

        String dbName = "tcpMem";

        //option
        String key = "IFEXISTS";
        String value = "TRUE";

        String exceptedURL = "jdbc:h2:tcp://localhost:10000/mem:tcpMem;IFEXISTS=TRUE;";

        //TODO stub
        String actualURL = "";
        assertEquals(exceptedURL,actualURL);

    }

    @Test
    public void tcpServerWithFileStore(){
        String host = "localhost";
        String dbName = "tcpFileDb";
        String pathToDb = "/opt";

        //option
        String key = "IFEXISTS";
        String value = "TRUE";

        String exceptedURL = "jdbc:h2:tcp://localhost:9092/opt/tcpFileDb;IFEXISTS=TRUE;";

        //TODO stub
        String actualURL = "";
        assertEquals(exceptedURL,actualURL);

    }

    @Test
    public void sslServerWithFileStore(){
        String host = "localhost";
        String dbName = "tcpFileDb";
        String pathToDb = "/opt";

        //option
        String key = "IFEXISTS";
        String value = "TRUE";

        String exceptedURL = "jdbc:h2:ssl://localhost:9092/opt/tcpFileDb;IFEXISTS=TRUE;";

        //TODO stub
        String actualURL = "";
        assertEquals(exceptedURL,actualURL);
    }
}
