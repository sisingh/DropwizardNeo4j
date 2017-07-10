package com.hike.hkg.dao;

import com.codahale.metrics.annotation.Metered;
import com.codahale.metrics.annotation.Timed;
import com.hike.hkg.model.RecommendedFriends;
import java.util.List;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

/**
 * DAO to get friend recommendation list
 *
 * @author siddharthasingh
 */
public interface FriendRecommendationsDAO {
    @Metered
    @Timed
    @Mapper(RecommendedFriendsMapper.class)
    @SqlQuery("MATCH (me\\:users {uid\\::id})-[\\:friend]->(people)-[\\:friend]->(fof) WITH me, fof,"
        + " COUNT(people) as friend_count WHERE NOT((me)-[\\:friend]->(fof)) AND me.uid <> fof.uid"
        + " RETURN fof.uid as uid, friend_count as fc ORDER BY friend_count DESC")
    List<RecommendedFriends> findFriendsOfFriends(@Bind("id") String id);
}
