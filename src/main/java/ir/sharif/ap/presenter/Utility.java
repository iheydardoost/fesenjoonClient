package ir.sharif.ap.presenter;

import ir.sharif.ap.model.TweetTile;

import java.time.LocalDateTime;
import java.util.Base64;

public class Utility {
    public static TweetTile createTweetFromResponse(String response){
        String[] args = response.split(",",-1);
        TweetTile tweet=new TweetTile();
        byte[] tweetImage = null, userImage = null;
        if(!args[6].isEmpty())
            tweetImage = Base64.getDecoder().decode(args[6]);
        if(!args[10].isEmpty())
            userImage = Base64.getDecoder().decode(args[10]);
        tweet.setTweetText(args[0])
                .setTweetDateTime(LocalDateTime.parse(args[1]))
                .setUserID(Long.parseLong(args[2]))
                .setTweetID(Long.parseLong(args[3]))
                .setRetweeted(Boolean.parseBoolean(args[4]))
                .setYouLiked(Boolean.parseBoolean(args[5]))
                .setTweetImage(tweetImage)
                .setUserName(args[7])
                .setFirstName(args[8])
                .setLastName(args[9])
                .setUserImage(userImage)
                .setLikesNum(Integer.parseInt(args[11]))
                .setCommentsNum(Integer.parseInt(args[12]));
        if(args.length > 13)
            tweet.setMute(Boolean.parseBoolean(args[13]));
        return tweet;
    }
}
