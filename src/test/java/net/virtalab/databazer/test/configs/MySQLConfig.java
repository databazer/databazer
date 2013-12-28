package net.virtalab.databazer.test.configs;

import net.virtalab.databazer.mysql.MySQLDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Java (annotation-based) beans for MySQL Database
 */
@Configuration
public class MySQLConfig {
    @Bean
    @Qualifier("MySQLBean")
    public MySQLDataSource getMySQLDS(){
        MySQLDataSource ds = new MySQLDataSource();
        ds.setName("myDs");
        ds.setUsername("user");
        ds.setPassword("superPass");

        return ds;
    }

    @Bean
    @Qualifier("MySQLCreatedBean")
    public MySQLDataSource getConstructedMySQLDS(){
        return MySQLDataSource.Creator()
                .host("localhost").databaseName("myDB")
                .username("user").password("mySuperPass")
                .create();
    }

}
