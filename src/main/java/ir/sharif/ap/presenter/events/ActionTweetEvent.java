package ir.sharif.ap.presenter.events;

import ir.sharif.ap.model.ActionType;

public class ActionTweetEvent {
    private ActionType actionType;
    private long tweetID;

    public ActionTweetEvent(ActionType actionType, long tweetID) {
        this.actionType = actionType;
        this.tweetID = tweetID;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    public long getTweetID() {
        return tweetID;
    }

    public void setTweetID(long tweetID) {
        this.tweetID = tweetID;
    }
}
