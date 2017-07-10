package com.hike.hkg.dao;

import com.hike.hkg.model.RecommendedFriends;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author siddharthasingh
 */
public class RecommendedFriendsMapper implements ResultSetMapper<RecommendedFriends> {

    private static final Logger LOG = LoggerFactory.getLogger(RecommendedFriendsMapper.class);

    @Override
    public RecommendedFriends map(int i, ResultSet rs, StatementContext sc) throws SQLException {
        String uidOfFriend = rs.getString("uid");
        Integer friendCount = rs.getInt("fc");
        RecommendedFriends recFriends = new RecommendedFriends(uidOfFriend, friendCount);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Recommended friends : {}", recFriends);
        }
        return recFriends;
    }
}
