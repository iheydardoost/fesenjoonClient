package ir.sharif.ap.model;

import ir.sharif.ap.Main;

import java.time.LocalDateTime;

public class TweetTile {
    private String tweetText, tweetUsername, likesNum, commentsNum;
    private LocalDateTime tweetDateTime;
    private byte[] tweetImage, userImage;
    private boolean isMainTweet;

    public TweetTile(String tweetText, String tweetUsername, String likesNum, String commentsNum, LocalDateTime tweetDateTime, byte[] tweetImage, byte[] userImage) {
        this.tweetText = tweetText;
        this.tweetUsername = tweetUsername;
        this.likesNum = likesNum;
        this.commentsNum = commentsNum;
        this.tweetDateTime = tweetDateTime;
        this.tweetImage = tweetImage;
        this.userImage = userImage;
        isMainTweet = false;
    }

    public TweetTile(TweetTile tweet) {
        this.tweetText = tweet.tweetText;
        this.tweetUsername = tweet.tweetUsername;
        this.likesNum = tweet.likesNum;
        this.commentsNum = tweet.commentsNum;
        this.tweetDateTime = tweet.tweetDateTime;
        this.tweetImage = tweet.tweetImage;
        this.userImage = tweet.userImage;
        this.isMainTweet = tweet.isMainTweet;
    }

    public String getTweetText() {
        return tweetText;
    }

    public void setTweetText(String tweetText) {
        this.tweetText = tweetText;
    }

    public String getTweetUsername() {
        return tweetUsername;
    }

    public void setTweetUsername(String tweetUsername) {
        this.tweetUsername = tweetUsername;
    }

    public String getLikesNum() {
        return likesNum;
    }

    public void setLikesNum(String likesNum) {
        this.likesNum = likesNum;
    }

    public String getCommentsNum() {
        return commentsNum;
    }

    public void setCommentsNum(String commentsNum) {
        this.commentsNum = commentsNum;
    }

    public LocalDateTime getTweetDateTime() {
        return tweetDateTime;
    }

    public void setTweetDateTime(LocalDateTime tweetDateTime) {
        this.tweetDateTime = tweetDateTime;
    }

    public byte[] getTweetImage() {
        return tweetImage;
    }

    public void setTweetImage(byte[] tweetImage) {
        this.tweetImage = tweetImage;
    }

    public byte[] getUserImage() {
        return userImage;
    }

    public void setUserImage(byte[] userImage) {
        this.userImage = userImage;
    }

    public void setMainTweet(boolean mainTweet) {
        isMainTweet = mainTweet;
    }

    public boolean isMainTweet() {
        return isMainTweet;
    }
}
