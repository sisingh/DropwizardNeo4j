package com.hike.hkg.model;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author siddharthasingh
 */
public class RecommendedFriends {
    @NotNull
    @NotEmpty
    private String uid;
    @NotNull
    private Integer friendCount;

    public RecommendedFriends(String uid, Integer friendCount) {
        this.uid = uid;
        this.friendCount = friendCount;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Integer getFriendCount() {
        return friendCount;
    }

    public void setFriendCount(Integer friendCount) {
        this.friendCount = friendCount;
    }

    @Override
    public String toString() {
        return "RecommendedFriends{" + "uid=" + uid + ", friendCount=" + friendCount + '}';
    }
}
