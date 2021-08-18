package ir.sharif.ap.model;

import java.time.LocalDateTime;
import java.util.Arrays;

public class TweetTile {
    private String tweetText;
    private String userName, firstName, lastName;
    private int likesNum, commentsNum;
    private long tweetID, userID, parentTweetID;
    private LocalDateTime tweetDateTime;
    private byte[] tweetImage, userImage;
    private boolean isMainTweet;
    private boolean youLiked, retweeted;

    public TweetTile() {
        this.tweetText = null;
        this.userName = null;
        this.firstName = null;
        this.lastName = null;
        this.likesNum = 0;
        this.commentsNum = 0;
        this.tweetID = 0;
        this.userID = 0;
        this.parentTweetID = 0;
        this.tweetDateTime = null;
        this.tweetImage = null;
        this.userImage = null;
        this.isMainTweet = false;
        this.youLiked = false;
        this.retweeted = false;
    }

    public TweetTile copyTweetTile(TweetTile tweet) {
        TweetTile newTweetTile = new TweetTile();
        newTweetTile.setTweetText(String.copyValueOf(tweet.getTweetText().toCharArray()))
                .setUserName(String.copyValueOf(tweet.getUserName().toCharArray()))
                .setFirstName(String.copyValueOf(tweet.getFirstName().toCharArray()))
                .setLastName(String.copyValueOf(tweet.getLastName().toCharArray()))
                .setLikesNum(tweet.getLikesNum())
                .setCommentsNum(tweet.getCommentsNum())
                .setTweetID(tweet.getTweetID())
                .setUserID(tweet.getUserID())
                .setParentTweetID(tweet.getParentTweetID())
                .setTweetDateTime(LocalDateTime.from(tweet.getTweetDateTime()))
                .setTweetImage(Arrays.copyOf(tweet.getTweetImage(),tweet.getTweetImage().length))
                .setUserImage(Arrays.copyOf(tweet.getUserImage(),tweet.getUserImage().length))
                .setMainTweet(tweet.isMainTweet())
                .setYouLiked(tweet.isYouLiked())
                .setRetweeted(tweet.isRetweeted());
        return newTweetTile;
    }

    public TweetTile setTweetText(String tweetText) {
        this.tweetText = tweetText;
        return this;
    }

    public TweetTile setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public TweetTile setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public TweetTile setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public TweetTile setLikesNum(int likesNum) {
        this.likesNum = likesNum;
        return this;
    }

    public TweetTile setCommentsNum(int commentsNum) {
        this.commentsNum = commentsNum;
        return this;
    }

    public TweetTile setTweetID(long tweetID) {
        this.tweetID = tweetID;
        return this;
    }

    public TweetTile setUserID(long userID) {
        this.userID = userID;
        return this;
    }

    public TweetTile setParentTweetID(long parentTweetID) {
        this.parentTweetID = parentTweetID;
        return this;
    }

    public TweetTile setTweetDateTime(LocalDateTime tweetDateTime) {
        this.tweetDateTime = tweetDateTime;
        return this;
    }

    public TweetTile setTweetImage(byte[] tweetImage) {
        this.tweetImage = tweetImage;
        return this;
    }

    public TweetTile setUserImage(byte[] userImage) {
        this.userImage = userImage;
        return this;
    }

    public TweetTile setMainTweet(boolean mainTweet) {
        isMainTweet = mainTweet;
        return this;
    }

    public TweetTile setYouLiked(boolean youLiked) {
        this.youLiked = youLiked;
        return this;
    }

    public TweetTile setRetweeted(boolean retweeted) {
        this.retweeted = retweeted;
        return this;
    }

    public String getTweetText() {
        return tweetText;
    }

    public String getUserName() {
        return userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getLikesNum() {
        return likesNum;
    }

    public int getCommentsNum() {
        return commentsNum;
    }

    public long getTweetID() {
        return tweetID;
    }

    public long getUserID() {
        return userID;
    }

    public long getParentTweetID() {
        return parentTweetID;
    }

    public LocalDateTime getTweetDateTime() {
        return tweetDateTime;
    }

    public byte[] getTweetImage() {
        return tweetImage;
    }

    public byte[] getUserImage() {
        return userImage;
    }

    public boolean isMainTweet() {
        return isMainTweet;
    }

    public boolean isYouLiked() {
        return youLiked;
    }

    public boolean isRetweeted() {
        return retweeted;
    }
}
