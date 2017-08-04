package net.bitnine.jwt;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.postgresql.ds.PGPoolingDataSource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("application")
public class DataSourceMap {

    /*private Map<String, DataSource> dataSources = new HashMap<>();

    public Map<String, DataSource> getDataSources() {
        return dataSources;
    }

    public void setDataSources(Map<String, DataSource> dataSources) {
        this.dataSources = dataSources;
    }*/
    private Map<String, PGPoolingDataSource> dataSources = new HashMap<>();

    public Map<String, PGPoolingDataSource> getDataSources() {
        return dataSources;
    }

    public void setDataSources(Map<String, PGPoolingDataSource> dataSources) {
        this.dataSources = dataSources;
    }
    
}
