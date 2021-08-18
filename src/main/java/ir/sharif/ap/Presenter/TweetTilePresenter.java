package ir.sharif.ap.Presenter;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.Avatar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import ir.sharif.ap.Main;
import ir.sharif.ap.View.TimeLineView;
import ir.sharif.ap.View.TweetDetailView;
import ir.sharif.ap.model.TweetTile;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


import static ir.sharif.ap.Main.NEW_TWEET_VIEW;
import static ir.sharif.ap.Main.getNextViewName;
import static ir.sharif.ap.Presenter.Styles.detailButtonStyle;
import static ir.sharif.ap.Presenter.Styles.darkBackgroundStyle;
import static ir.sharif.ap.Presenter.Styles.lightBackgroundStyle;


public class TweetTilePresenter implements Initializable {
    @FXML
    private BorderPane tweetPane;

    @FXML
    private Avatar tweetAvatar;

    @FXML
    private Label tweetUsername;

    @FXML
    private Text tweetText;

    @FXML
    private ImageView tweetImage;

    @FXML
    private HBox tweetButtonBox;

    private Button retweetButton, likeButton, commentButton, tweetDetailButton;

    private Label likesNum, commentsNum;
    private TweetTile tweet;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        tweetImage.setManaged(false);
        tweetImage.setVisible(false);

        retweetButton = MaterialDesignIcon.REPEAT.button(e -> System.out.println("retweet"));
        likeButton = MaterialDesignIcon.FAVORITE.button(e -> System.out.println("likeButton"));
        commentButton = MaterialDesignIcon.MESSAGE.button(e -> {
                    System.out.println("hi");
                    MobileApplication.getInstance().switchView(NEW_TWEET_VIEW);
                }
        );
        tweetDetailButton = MaterialDesignIcon.MORE_HORIZ.button(e -> {
            if(tweet.isMainTweet()){//user is in tweet detail form and had pressed detail on main tweet, no need to create another detail form
                return;
            }

            String newViewLayer = getNextViewName();
            MobileApplication.getInstance().addViewFactory(newViewLayer,() -> {
                final TweetDetailView tweetDetailView = new TweetDetailView();
                TweetDetailPresenter tweetDetailPresenter=(TweetDetailPresenter) tweetDetailView.getPresenter();
                tweetDetailPresenter.setMainTweet(new TweetTile(tweet));
                return (View) tweetDetailView.getView();
            });

            MobileApplication.getInstance().switchView(newViewLayer);

        });

        retweetButton.setStyle(detailButtonStyle);
        likeButton.setStyle(detailButtonStyle);
        commentButton.setStyle(detailButtonStyle);
        tweetDetailButton.setStyle(detailButtonStyle);

        likesNum = new Label("0");
        commentsNum = new Label("0");

        tweetButtonBox.getChildren().addAll(retweetButton, likeButton,likesNum, commentButton, commentsNum, tweetDetailButton);
    }

    public void setTweetAvatar(Image tweetAvatar) {
        if(tweetAvatar != null){
            this.tweetAvatar.setImage(tweetAvatar);
        }
    }

    public void setTweetUsername(String tweetUsername) {
        this.tweetUsername.setText(tweetUsername);
    }

    public void setTweetText(String tweetText) {
        this.tweetText.setText(tweetText);
    }

    public void setTweetImage(Image tweetImage) {
        if(tweetImage != null){
            this.tweetImage.setManaged(true);
            this.tweetImage.setVisible(true);
            this.tweetImage.setImage(tweetImage);
        }else{
            this.tweetImage.setManaged(false);
            this.tweetImage.setVisible(false);
        }
    }

    public void setLikesNum(String likesNum) {
        this.likesNum.setText(likesNum);
    }

    public void setCommentsNum(String commentsNum) {
        this.commentsNum.setText(commentsNum);
    }
    public void setDarkBackgroundStyle(boolean setDarkBackgroundStyle){
        if(setDarkBackgroundStyle){
            this.tweetPane.setStyle(darkBackgroundStyle);
        }else {
            this.tweetPane.setStyle(lightBackgroundStyle);
        }
    }

    public void setTweet(TweetTile tweet) {
        this.tweet = tweet;

        setTweetText(tweet.getTweetText());
        setTweetUsername(tweet.getTweetUsername());
        setCommentsNum(tweet.getCommentsNum());
        setLikesNum(tweet.getLikesNum());
        byte[] userImageData = tweet.getUserImage(), tweetImageDate = tweet.getTweetImage();
        Image userImage = null, tweetImage = null;

        if(userImageData!=null){
            ByteArrayInputStream bis = new ByteArrayInputStream(userImageData);
            userImage = new Image(bis);
            try {
                bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            userImage = new Image(Main.class.getResourceAsStream("/ir/sharif/ap/images/avatar.png"));
        }

        if(tweetImageDate != null){
            ByteArrayInputStream bis = new ByteArrayInputStream(tweetImageDate);
            tweetImage = new Image(bis);
            try {
                bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        setTweetAvatar(userImage);
        setTweetImage(tweetImage);
        setDarkBackgroundStyle(tweet.isMainTweet());
    }
}
