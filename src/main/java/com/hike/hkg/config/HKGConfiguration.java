package com.hike.hkg.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 *
 * @author siddharthasingh
 */
public class HKGConfiguration extends Configuration {

    @NotNull
    @JsonProperty("hkgConfiguration")
    private HKGNeo4jConfiguration hkgNeo4jConfiguration;

    public HKGConfiguration() {
        this.database = new DataSourceFactory();
    }

    @JsonProperty("hkgConfiguration")
    public HKGNeo4jConfiguration getHkgNeo4jConfiguration() {
        return hkgNeo4jConfiguration;
    }

    @JsonProperty("hkgConfiguration")
    public void setHkgNeo4jConfiguration(HKGNeo4jConfiguration hkgNeo4jConfiguration) {
        this.hkgNeo4jConfiguration = hkgNeo4jConfiguration;
    }

    @Valid
    @NotNull
    @JsonProperty
    private DataSourceFactory database;

    public DataSourceFactory getDataSourceFactory() {
        return database;
    }
}
