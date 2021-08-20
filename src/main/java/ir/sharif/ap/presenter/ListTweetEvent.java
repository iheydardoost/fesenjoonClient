package ir.sharif.ap.presenter;

import ir.sharif.ap.model.TweetListType;

import java.time.LocalDateTime;

public class ListTweetEvent {

    private int maxNum;
    private LocalDateTime lastTweetDateTime;
    private TweetListType tweetListType;
    private long parentTweetID;

    public ListTweetEvent(int maxNum, LocalDateTime lastTweetDateTime, TweetListType tweetListType,
                          long parentTweetID) {
        this.maxNum = maxNum;
        this.lastTweetDateTime = lastTweetDateTime;
        this.tweetListType = tweetListType;
        this.parentTweetID=parentTweetID;
    }

    public int getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(int maxNum) {
        this.maxNum = maxNum;
    }

    public LocalDateTime getLastTweetDateTime() {
        return lastTweetDateTime;
    }

    public void setLastTweetDateTime(LocalDateTime lastTweetDateTime) {
        this.lastTweetDateTime = lastTweetDateTime;
    }

    public TweetListType getTweetListType() {
        return tweetListType;
    }

    public void setTweetListType(TweetListType tweetListType) {
        this.tweetListType = tweetListType;
    }

    public long getParentTweetID() {
        return parentTweetID;
    }

    public void setParentTweetID(long parentTweetID) {
        this.parentTweetID = parentTweetID;
    }
}

