package ir.sharif.ap.presenter.events;

import ir.sharif.ap.model.FollowResponseType;

public class FollowResponseEvent {
    private FollowResponseType followResponseType;
    private String userName;

    public FollowResponseEvent(FollowResponseType followResponseType, String userName) {
        this.followResponseType = followResponseType;
        this.userName = userName;
    }

    public FollowResponseType getFollowResponseType() {
        return followResponseType;
    }

    public void setFollowResponseType(FollowResponseType followResponseType) {
        this.followResponseType = followResponseType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
