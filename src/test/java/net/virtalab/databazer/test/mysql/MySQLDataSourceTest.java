package net.virtalab.databazer.test.mysql;

import net.virtalab.databazer.mysql.MySQLDataSource;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.Driver;

/**
 * Test for MySQL DS
 *
 */
@ContextConfiguration(locations = "classpath:**/test-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class MySQLDataSourceTest extends Assert {
    //private static final Logger log = Logger.getLogger(MySQLDataSourceTest.class);

    @Autowired
    @Qualifier("mysqlXML")
    private MySQLDataSource mySQLDataSource;

    /**
     *  Default constructor which is called by Spring set some default values.
     *  This test intention is to check if we can use those default together and modify some props in same XML bean definition
     */
    @Test
    public void overrideDefaults(){
        if(mySQLDataSource==null){
            assertTrue("Running test failed. Cannot autowire bean.",false);
        }
        //check if driver remains default
        Class<? extends Driver> expectedDriver = com.mysql.jdbc.Driver.class;
        Class<? extends Driver> realDriver = mySQLDataSource.getDriver().getClass();

        assertEquals(expectedDriver,realDriver);

        //check if user is overridden
        String expectedUser = "myuser";
        String realUser = mySQLDataSource.getUsername();

        assertEquals(expectedUser,realUser);

    }

}
