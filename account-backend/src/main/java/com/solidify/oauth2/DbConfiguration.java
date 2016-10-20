package com.solidify.oauth2;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Created by tomasz on 16.10.16.
 */
@Configuration
public class DbConfiguration {

    @Bean
    public SpringLiquibase liquibase(DataSource dataSource)  {
        SpringLiquibase liquibase = new SpringLiquibase();

        liquibase.setDataSource(dataSource);
        liquibase.setChangeLog("classpath:/db/changelog/db.changelog-master.xml");

        return liquibase;
    }

}
