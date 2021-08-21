package ir.sharif.ap.presenter;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.Avatar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import ir.sharif.ap.Main;
import ir.sharif.ap.presenter.events.ActionTweetEvent;
import ir.sharif.ap.presenter.events.NewTweetEvent;
import ir.sharif.ap.view.TweetDetailView;
import ir.sharif.ap.controller.LogHandler;
import ir.sharif.ap.model.ActionType;
import ir.sharif.ap.model.TweetTile;
import ir.sharif.ap.presenter.listeners.ActionTweetEventListener;
import ir.sharif.ap.presenter.listeners.NewTweetListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

import static ir.sharif.ap.Main.NEW_TWEET_VIEW;
import static ir.sharif.ap.Main.getNextViewName;
import static ir.sharif.ap.presenter.Styles.*;


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

    private ActionTweetEventListener actionTweetEventListener;
    private NewTweetListener newTweetListener;

    public void addNewTweetListener(NewTweetListener newTweetListener) {
        this.newTweetListener = newTweetListener;
    }

    public void addActionTweetEventListener(ActionTweetEventListener actionTweetEventListener) {
        this.actionTweetEventListener = actionTweetEventListener;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        tweetImage.setManaged(false);
        tweetImage.setVisible(false);

        retweetButton = MaterialDesignIcon.REPEAT.button(e -> {
            NewTweetEvent event =
                    new NewTweetEvent(
                      tweet.getTweetText(),
                            LocalDateTime.now(),
                            0,
                      true,
                      tweet.getTweetImage()
                    );
            newTweetListener.newTweetEventOccurred(event);
        });
        likeButton = MaterialDesignIcon.FAVORITE.button(e -> {
            LogHandler.logger.info("tweet like button pressed");
            ActionTweetEvent event =
                    new ActionTweetEvent(ActionType.LIKE,tweet.getTweetID());
            actionTweetEventListener.actionTweetEventOccurred(event);

            if(tweet.isYouLiked()){
                tweet.setLikesNum(tweet.getLikesNum()-1);
            }else {
                tweet.setLikesNum(tweet.getLikesNum()+1);
            }
            tweet.setYouLiked(!tweet.isYouLiked());
            setLikeIcon(tweet.isYouLiked());
            setLikesNum(String.valueOf(tweet.getLikesNum()));
        });
        commentButton = MaterialDesignIcon.MESSAGE.button(e -> {
                    LogHandler.logger.info("comment button pressed");
                    MobileApplication.getInstance().switchView(NEW_TWEET_VIEW);
                    Main.getNewTweetPresenter().setParentTweetID(tweet.getTweetID());;

                }
        );
        tweetDetailButton = MaterialDesignIcon.MORE_HORIZ.button(e -> {
            LogHandler.logger.info("more button pressed");

            if(tweet.isMainTweet()){//user is in tweet detail form and had pressed detail on main tweet, no need to create another detail form
                return;
            }

            String newViewLayer = getNextViewName();
            MobileApplication.getInstance().addViewFactory(newViewLayer,() -> {
                final TweetDetailView tweetDetailView = new TweetDetailView();
                TweetDetailPresenter tweetDetailPresenter=(TweetDetailPresenter) tweetDetailView.getPresenter();
                Main.setCurrentTweetDetailPresenter(tweetDetailPresenter);
                //tweetDetailPresenter.setMainTweet(TweetTile.copyTweetTile(tweet));
                tweetDetailPresenter.addRelationUserEventListener(e1 -> Main.getMainController().handleRelationUserEvent(e1));
                tweetDetailPresenter.addActionTweetEventListener(e1 -> Main.getMainController().handleActionTweetEvent(e1));
                tweetDetailPresenter.addGetTweetEventListener(e1 -> Main.getMainController().handleGetTweetEvent(e1));
                tweetDetailPresenter.addListTweetEventListener(e1 -> Main.getMainController().handleListTweetEvent(e1));
                tweetDetailPresenter.setParentTweetID(tweet.getTweetID());

                return (View) tweetDetailView.getView();
            });

            MobileApplication.getInstance().switchView(newViewLayer);

        });

        retweetButton.setStyle(DEFAULT_BUTTON_STYLE);
        likeButton.setStyle(DEFAULT_BUTTON_STYLE);
        commentButton.setStyle(DEFAULT_BUTTON_STYLE);
        tweetDetailButton.setStyle(DEFAULT_BUTTON_STYLE);

        likesNum = new Label("0");
        commentsNum = new Label("0");

        tweetButtonBox.getChildren().addAll(retweetButton, likeButton,likesNum, commentButton, commentsNum, tweetDetailButton);
    }

    public void setTweetAvatar(Image tweetAvatar) {
        if(tweetAvatar != null){
            this.tweetAvatar.setImage(tweetAvatar);
        }
    }

    public void setTweetFullName(String tweetUsername) {
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
            this.tweetPane.setStyle(DARK_BACKGROUND_STYLE);
        }else {
            this.tweetPane.setStyle(LIGHT_BACKGROUND_STYLE);
        }
    }

    public void setLikeIcon(boolean isLiked){
        if(isLiked){
            likeButton.setStyle(RED_BUTTON_STYLE);
        }else {
            likeButton.setStyle(DEFAULT_BUTTON_STYLE);
        }
    }

    public void setTweet(TweetTile tweet) {
        this.tweet = tweet;

        setTweetText(tweet.getTweetText());
        setTweetFullName(tweet.getFirstName() + " " + tweet.getLastName());
        setCommentsNum(Integer.toString(tweet.getCommentsNum()));
        setLikesNum(Integer.toString(tweet.getLikesNum()));
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
        setLikeIcon(tweet.isYouLiked());
    }
}
