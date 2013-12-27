package net.virtalab.databazer.test.configs;

import net.virtalab.databazer.h2.H2DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Java (annotation) based bean for H2 Databases
 */
@Configuration
public class H2Config {

    @Bean
    @Qualifier("H2JavaBean")
    public H2DataSource getH2JavaDS(){
         H2DataSource ds = new H2DataSource();
         ds.setName("myCustomDS");
         ds.setUsername("sa");
         ds.setPassword("myPassword");

        return ds;
    }

    @SuppressWarnings("UnnecessaryLocalVariable")
    @Bean
    @Qualifier("H2CreatedJavaBean")
    public H2DataSource getCreatedJavaDS(){
         H2DataSource ds = H2DataSource.Creator()
                 .mem().databaseName("creatorDb")
                 .username("sa")
                 .password("mySuperPass")
                 .create();
        return ds;
    }
}
