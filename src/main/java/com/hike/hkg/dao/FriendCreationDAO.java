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
public interface FriendCreationDAO {
    @Metered
    @Timed
    @SqlUpdate("MATCH (a\\:users), (b\\:users) WHERE a.uid = ::uid1 AND b.uid = ::uid2 "
        + "CREATE (a)-[r\\:friend]->(b) "
        + "RETURN a.uid, b.uid")
    void createFriend(@Bind("uid1") String uid1, @Bind("uid2") String uid2);
}
