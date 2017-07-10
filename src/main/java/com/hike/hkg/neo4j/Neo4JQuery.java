package com.hike.hkg.neo4j;

import com.hike.hkg.model.RecommendedFriends;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This is another way of JDBC to connect to Neo4J. I am using JDBI approach which is much cleaner
 *
 * @author siddharthasingh
 */
public class Neo4JQuery implements AutoCloseable {

    private final String uri;
    private final String user;
    private final String password;

    public Neo4JQuery(String uri, String user, String password) {
        this.uri = uri;
        this.user = user;
        this.password = password;
    }

    @Override
    public void close() throws Exception {
    }

    public List<RecommendedFriends> getFriendOfFriendCount(final String uid) throws SQLException {
        List<RecommendedFriends> listOfRecFriends = new ArrayList();

        try (Connection con = DriverManager.getConnection(uri, user, password)) {
            String query = "MATCH (me:users {uid:{1}})-[:friend]->(people)-[:friend]->(fof) WITH me, fof,"
                + " COUNT(people) as friend_count WHERE me.uid = {1} AND NOT((me)-[:friend]->(fof)) AND me.uid <> fof.uid"
                + " RETURN fof.uid as uid, friend_count as fc  ORDER BY friend_count DESC";

            try (PreparedStatement prepStmt = con.prepareStatement(query)) {
                prepStmt.setString(1, uid);
                // Statement stmt = con.createStatement();
                //try (ResultSet rs = stmt.executeQuery(query)) {
                try (ResultSet rs = prepStmt.executeQuery()) {
                    while (rs.next()) {
                        String uidOfFriend = rs.getString("uid");
                        Integer friendCount = rs.getInt("fc");
                        RecommendedFriends recFriends = new RecommendedFriends(uidOfFriend, friendCount);
                        System.out.println("UID " + uidOfFriend + " friend count " + friendCount);
                        listOfRecFriends.add(recFriends);
                    }
                }
            }
        }
        catch (Exception e) {
            System.out.println("Exception : " + e.getMessage());
        }
        return listOfRecFriends;
    }
}
