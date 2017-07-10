package com.hike.hkg.api;

/**
 *
 * @author siddharthasingh
 */
import com.codahale.metrics.annotation.Metered;
import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Optional;
import com.hike.hkg.config.HKGConfiguration;
import com.hike.hkg.dao.FriendCreationDAO;
import com.hike.hkg.dao.FriendRecommendationsDAO;
import com.hike.hkg.dao.UserCreationDAO;
import com.hike.hkg.model.RecommendedFriends;
import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/v1/neo4j")
@Produces(MediaType.APPLICATION_JSON)
public class HKGResource {

    private static final Logger LOG = LoggerFactory.getLogger(HKGResource.class);

    private final HKGConfiguration configuration;
    private final FriendRecommendationsDAO friendRecDAO;
    private final UserCreationDAO userCreationDAO;
    private final FriendCreationDAO friendCreationDAO;

    public HKGResource(HKGConfiguration configuration,
        FriendRecommendationsDAO friendRecDAO,
        UserCreationDAO userCreationDAO,
        FriendCreationDAO friendCreationDAO) {
        this.configuration = configuration;
        this.friendRecDAO = friendRecDAO;
        this.userCreationDAO = userCreationDAO;
        this.friendCreationDAO = friendCreationDAO;
    }

    @GET
    @Path("/rec_friends")
    @Produces(MediaType.APPLICATION_JSON)
    @Timed(absolute = true, name = "GET_Neo4j_Relationship_Time")
    @Metered(absolute = true, name = "GET_Neo4j_Relationship_Meter")
    public Response getRelationship(@QueryParam("uid") Optional<String> uid) {
        if (uid.isPresent() && uid.get().trim().isEmpty() != false) {
            LOG.info("rec_friends API uid : '{}'", uid.get());
            List<RecommendedFriends> recFriends = null;
            try {
                recFriends = getFoF(uid.get());
                return Response.ok(recFriends).build();
            }
            catch (Exception ex) {
                LOG.error("Error executing query : getFoF() '{}'", ex.getMessage());
                return Response.status(Response.Status.NO_CONTENT).build();
            }
        }
        return Response.ok("{Error : Invalid UID}").build();
    }

    private List<RecommendedFriends> getFoF(String uid) throws Exception {
        List<RecommendedFriends> friendOfFriends = (List<RecommendedFriends>) friendRecDAO.findFriendsOfFriends(uid);
        return friendOfFriends;
    }

    @POST
    @Path("/create_user/{uid}/name/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Timed(absolute = true, name = "POST_Neo4j_Create_User_Time")
    @Metered(absolute = true, name = "POST_Neo4j_Create_User_Meter")
    public Response createUser(@PathParam("uid") Optional<String> uid, @PathParam("name") Optional<String> name) {
        if (uid.isPresent() && !uid.get().trim().isEmpty() && name.isPresent() && !name.get().trim().isEmpty()) {
            LOG.info("create_user API uid : '{}' name : '{}'", uid.get(), name.get());
            try {
                createAUser(uid.get(), name.get());
                return Response.ok().build();
            }
            catch (Exception ex) {
                LOG.error("Error executing query : createAUser() '{}'", ex.getMessage());
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }
        }
        return Response.ok("{Error : Invalid UID}").build();
    }

    private void createAUser(String uid, String name) throws Exception {
        userCreationDAO.createUser(uid, name);
    }

    @POST
    @Path("/create_friend/{uid1}/{uid2}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Timed(absolute = true, name = "POST_Neo4j_Create_Friend_Time")
    @Metered(absolute = true, name = "POST_Neo4j_Create_Friend_Meter")
    public Response createFriend(@PathParam("uid1") Optional<String> uid1, @PathParam("uid2") Optional<String> uid2) {
        if (uid1.isPresent() && !uid1.get().trim().isEmpty() && uid2.isPresent() && !uid2.get().trim().isEmpty()) {
            LOG.info("create_friend API uid : '{}' name : '{}'", uid1.get(), uid2.get());
            try {
                createAFriend(uid1.get(), uid2.get());
                return Response.ok().build();
            }
            catch (Exception ex) {
                LOG.error("Error executing query : createAUser() '{}'", ex.getMessage());
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }
        }
        return Response.ok("{Error : Invalid UID}").build();
    }

    private void createAFriend(String uid1, String uid2) throws Exception {
        friendCreationDAO.createFriend(uid1, uid2);
    }

}
