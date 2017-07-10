package com.hike.hkg.dao;

import com.codahale.metrics.annotation.Metered;
import com.codahale.metrics.annotation.Timed;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

/**
 * DAO to create a user
 *
 * @author siddharthasingh
 */
public interface UserCreationDAO {
    @Metered
    @Timed
    @SqlUpdate("CREATE (n\\:users { uid\\::id, name\\::name})")
    void createUser(@Bind("id") String id, @Bind("name") String name);
}
