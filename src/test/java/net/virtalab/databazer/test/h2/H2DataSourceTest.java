package net.virtalab.databazer.test.h2;

import net.virtalab.databazer.h2.H2DataSource;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.Driver;

/**
 * Tests for H2 Datasource
 */
@ContextConfiguration(locations = "classpath:**/test-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class H2DataSourceTest extends Assert {
    //private static final Logger log = Logger.getLogger(H2DataSourceTest.class);

    @Autowired
    @Qualifier("h2XML")
    private H2DataSource h2DataSource;

    @Autowired
    @Qualifier("H2JavaBean")
    private H2DataSource javaBeanDefaultConstructed;

    @Autowired
    @Qualifier("H2CreatedJavaBean")
    private H2DataSource javaBeanCreatorConstructed;

    /**
     *  Default constructor which is called by Spring set some default values.
     *  This test intention is to check if we can use those default together and modify some props in same XML bean definition
     */
    @Test
    public void xmlH2Bean(){
        if(h2DataSource==null){
            assertTrue("Could not autowire bean. Test failed.",false);
        }

        //check if driver remains default
        Class<? extends Driver> expectedDriver = org.h2.Driver.class;
        Class<? extends Driver> actualDriver = h2DataSource.getDriver().getClass();

        assertEquals(expectedDriver,actualDriver);

        //check if default password is overridden
        String expectedPassword = "h2Pass";
        String actualPassword = h2DataSource.getPassword();

        assertEquals(expectedPassword,actualPassword);
    }

    @Test
    public void javaBeanDefaultConstructor(){
        if(javaBeanDefaultConstructed==null){
            assertTrue("Could not autowire bean. Test failed.",false);
        }

        //name was overriden
        String expectedName = "myCustomDS";
        String actualName = javaBeanDefaultConstructed.getName();

        assertEquals(expectedName,actualName);

        //and URL should be remain default as we did not set any URL related options
        String defaultURL = "jdbc:h2:mem:";
        String URLInDS = javaBeanDefaultConstructed.getUrl();

        assertEquals(defaultURL,URLInDS);
    }

    @Test
    public void javaBeanCreatorConstructed(){
        if(javaBeanCreatorConstructed==null){
            assertTrue("Could not autowire bean. Test failed.",false);
        }

        //name should be default as we didn't mention it
        String defaultName = "default";
        String nameInDataSource = javaBeanCreatorConstructed.getName();

        assertEquals(defaultName,nameInDataSource);

        //and URL should be changed, as we constructed named in-memory DB
        String expectedURL = "jdbc:h2:mem:creatorDb";
        String actualURL = javaBeanCreatorConstructed.getUrl();

        assertEquals(expectedURL,actualURL);
    }
}
