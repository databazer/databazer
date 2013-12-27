package net.virtalab.databazer.test;

import net.virtalab.databazer.mysql.MySQLDataSource;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for creator
 */
public class MySQLCreatorTest extends Assert {

    @Test
    public void defaults(){
        MySQLDataSource ds = MySQLDataSource.Creator().create();

        String exceptedUrl = "jdbc:mysql://localhost:3306/default";
        String actualUrl = ds.getUrl();
        assertEquals(exceptedUrl,actualUrl);
    }

    @Test
    public void namedDataSource(){
        MySQLDataSource ds = MySQLDataSource.Creator().name("myDS").create();

        String expectedName = "myDS";
        String actualName = ds.getName();

        assertEquals(expectedName,actualName);
    }

    @Test
    public void customUrl(){
        String url = "jdbc:mysql://127.0.0.2:3306/myDB";
        MySQLDataSource ds = MySQLDataSource.Creator().url(url).create();

        String exceptedUrl = "jdbc:mysql://127.0.0.2:3306/myDB";
        String actualUrl = ds.getUrl();
        assertEquals(exceptedUrl,actualUrl);

    }

    @Test
    public void singleProperty(){
        MySQLDataSource ds = MySQLDataSource.Creator().option("characterEncoding","UTF-8").create();

        String exceptedUrl = "jdbc:mysql://localhost:3306/default?characterEncoding=UTF-8";
        String actualUrl = ds.getUrl();
        assertEquals(exceptedUrl,actualUrl);
    }

    @Test
    public void multiHosts(){
        MySQLDataSource ds = MySQLDataSource.Creator()
                .host("localhost").failoverHost("localhost2").failoverHost("localhost3",3307)
                .databaseName("db")
                .create();
        String exceptedUrl = "jdbc:mysql://localhost:3306,localhost2:3306,localhost3:3307/db";
        String actualUrl = ds.getUrl();
        assertEquals(exceptedUrl,actualUrl);
    }

    @Test
    public void multiProps(){
        MySQLDataSource ds = MySQLDataSource.Creator()
                .host("localhost")
                .databaseName("db")
                .option("profileSQL","true").option("characterEncoding","UTF-8")
                .create();

        String exceptedUrl = "jdbc:mysql://localhost:3306/db?profileSQL=true&characterEncoding=UTF-8";
        String actualUrl = ds.getUrl();
        assertEquals(exceptedUrl, actualUrl);
    }

    @Test
    public void multiEveryThing(){
        MySQLDataSource ds = MySQLDataSource.Creator()
                .host("localhost").failoverHost("localhost2",3307)
                .databaseName("db")
                .option("profileSQL","true").option("characterEncoding","UTF-8")
                .create();
        String exceptedUrl = "jdbc:mysql://localhost:3306,localhost2:3307/db?profileSQL=true&characterEncoding=UTF-8";
        String actualUrl = ds.getUrl();
        assertEquals(exceptedUrl, actualUrl);
    }
}
