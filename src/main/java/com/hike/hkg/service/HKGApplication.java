package com.hike.hkg.service;

/**
 *
 * @author siddharthasingh
 */
import com.hike.hkg.api.HKGResource;
import com.hike.hkg.config.HKGConfiguration;
import com.hike.hkg.dao.FriendCreationDAO;
import com.hike.hkg.dao.FriendRecommendationsDAO;
import com.hike.hkg.dao.UserCreationDAO;
import com.hike.hkg.healthcheck.Neo4JHealthCheck;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HKGApplication extends Application<HKGConfiguration> {

    private static final Logger LOG = LoggerFactory.getLogger(HKGApplication.class);

    public static void main(String[] args) throws Exception {
        new HKGApplication().run(args);
    }

    @Override
    public String getName() {
        return "Hike Knowledge Graph";
    }

    @Override
    public void initialize(Bootstrap<HKGConfiguration> bootstrap) {
    }

    @Override
    public void run(HKGConfiguration configuration, Environment env)
        throws MqttException, InterruptedException, ClassNotFoundException {
        final DBIFactory factory = new DBIFactory();
        DataSourceFactory dataSourceFactory = configuration.getDataSourceFactory();
        final DBI jdbi = factory.build(env, dataSourceFactory, "dsfactory");
        final FriendRecommendationsDAO friendRecDAO = jdbi.onDemand(FriendRecommendationsDAO.class);
        final UserCreationDAO userCreationDAO = jdbi.onDemand(UserCreationDAO.class);
        final FriendCreationDAO friendCreationDAO = jdbi.onDemand(FriendCreationDAO.class);
        final HKGResource resource = new HKGResource(configuration, friendRecDAO, userCreationDAO, friendCreationDAO);
        env.jersey().register(resource);
        env.healthChecks().register("Neo4J", new Neo4JHealthCheck(jdbi, dataSourceFactory.getValidationQuery()));
        LOG.info("Service started...");
    }

}
