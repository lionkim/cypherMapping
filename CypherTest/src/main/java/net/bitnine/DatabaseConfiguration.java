package net.bitnine;

import org.postgresql.ds.PGPoolingDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
//public class DatabaseConfiguration implements EnvironmentAware {

public class DatabaseConfiguration {
    private static final Logger log = LoggerFactory.getLogger(DatabaseConfiguration.class);

    private RelaxedPropertyResolver propertyResolver;

    /*@Override
    public  void setEnvironment(Environment env) {
        this.propertyResolver = new RelaxedPropertyResolver(env, "spring.datasource.");
    }

    @Primary
    @Bean(destroyMethod = "")
    public  DataSource dataSource() {
        log.debug("Configuring Datasource");

        if (propertyResolver.getProperty("url") == null && propertyResolver.getProperty("jndi") == null) {
            log.error("Your database connection pool configuration is incorrect! The application" +
                "cannot start. Please check your Spring profile, current profiles are: {}");

            throw new ApplicationContextException("Database connection pool is not configured correctly");
        }

        try {
            String jndi = propertyResolver.getProperty("jndi");

            if (jndi != null) {
                log.debug("Getting datasource from JNDI global resource link {}", jndi);
                JndiDataSourceLookup dataSourceLookup = new JndiDataSourceLookup();
                return dataSourceLookup.getDataSource(jndi);
            } else {

                String url = propertyResolver.getProperty("url");
                String username = propertyResolver.getProperty("username");

                log.debug("Initializing PGPoolingDataSource: url={}, username={}", url, username);
                PGPoolingDataSource source = new PGPoolingDataSource();
                source.setUrl(url);
                source.setUser(username);
                source.setPassword(propertyResolver.getProperty("password"));
                source.setMaxConnections(10);
                return source;
            }
        } catch (Exception e) {
            log.error("dataSource: msg=" + e.getMessage(), e);
            throw new ApplicationContextException("Database connection pool creation resulted in error");
        }
    }*/
}
