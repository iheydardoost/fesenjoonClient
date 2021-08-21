package ir.sharif.ap.presenter.events;

import java.time.LocalDateTime;

public class NewTweetEvent {
    private String tweetText;
    private LocalDateTime tweetDateTime;
    private long parentTweetID;
    private boolean retweeted;
    private byte[] tweetImage;

    public NewTweetEvent(String tweetText, LocalDateTime tweetDateTime, long parentTweetID, boolean retweeted, byte[] tweetImage) {
        this.tweetText = tweetText;
        this.tweetDateTime = tweetDateTime;
        this.parentTweetID = parentTweetID;
        this.retweeted = retweeted;
        this.tweetImage = tweetImage;
    }

    public String getTweetText() {
        return tweetText;
    }

    public void setTweetText(String tweetText) {
        this.tweetText = tweetText;
    }

    public LocalDateTime getTweetDateTime() {
        return tweetDateTime;
    }

    public void setTweetDateTime(LocalDateTime tweetDateTime) {
        this.tweetDateTime = tweetDateTime;
    }

    public long getParentTweetID() {
        return parentTweetID;
    }

    public void setParentTweetID(long parentTweetID) {
        this.parentTweetID = parentTweetID;
    }

    public boolean isRetweeted() {
        return retweeted;
    }

    public void setRetweeted(boolean retweeted) {
        this.retweeted = retweeted;
    }

    public byte[] getTweetImage() {
        return tweetImage;
    }

    public void setTweetImage(byte[] tweetImage) {
        this.tweetImage = tweetImage;
    }
}
