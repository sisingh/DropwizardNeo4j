package com.hike.hkg.healthcheck;

import com.codahale.metrics.health.HealthCheck;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Neo4JHealthCheck extends HealthCheck {

    private static final Logger LOG = LoggerFactory.getLogger(Neo4JHealthCheck.class);

    private final DBI dbi;
    private final String validationQuery;

    public Neo4JHealthCheck(DBI jdbi, String validationQuery) {
        this.dbi = jdbi;
        this.validationQuery = validationQuery;
    }

    @Override
    protected Result check() {
        try (Handle handle = dbi.open()) {
            handle.execute(validationQuery);
            LOG.info("Validation query '{}' successfully executed", validationQuery);
            return Result.healthy("Validation query : '" + validationQuery + "'");
        }
        catch (Exception e) {
            return Result.unhealthy("{Error : \"" + e.getMessage() + "\"}");
        }
    }
}
