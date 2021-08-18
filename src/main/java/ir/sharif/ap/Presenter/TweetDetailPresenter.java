package ir.sharif.ap.Presenter;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import ir.sharif.ap.controller.MainController;
import ir.sharif.ap.model.ActionType;
import ir.sharif.ap.model.RelationType;
import ir.sharif.ap.model.TweetListType;
import ir.sharif.ap.model.TweetTile;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.ResourceBundle;

import static ir.sharif.ap.Main.mainAppBar;

public class TweetDetailPresenter implements Initializable {
    @FXML
    private Label text;


    @FXML
    private View tweetDetailTab;

    @FXML
    private Button forwardButton;

    @FXML
    private Button blockButton;

    @FXML
    private Button muteButton;

    @FXML
    private Button reportButton;

    @FXML
    private Button visitButton;


    @FXML
    private ListView<TweetTile> tweetDetailList;

    private TweetTile mainTweet;
    private long parentTweetID;

    private ActionTweetEventListener actionTweetEventListener;
    private RelationUserEventListener relationUserEventListener;
    private ListTweetEventListener listTweetEventListener;
    private GetNewTweetEventListener getNewTweetEventListener;

    public void addListTweetEventListener(ListTweetEventListener listTweetEventListener) {
        this.listTweetEventListener = listTweetEventListener;
    }

    public void addGetNewTweetEventListener(GetNewTweetEventListener getNewTweetEventListener) {
        this.getNewTweetEventListener = getNewTweetEventListener;
    }

    public void addRelationUserEventListener(RelationUserEventListener relationUserEventListener) {
        this.relationUserEventListener = relationUserEventListener;
    }

    public void addActionTweetEventListener(ActionTweetEventListener actionTweetEventListener) {
        this.actionTweetEventListener = actionTweetEventListener;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        tweetDetailList.setCellFactory(p -> new TweetListCell());
        tweetDetailList.setPlaceholder(new Label("There are no Tweets"));
        tweetDetailList.setOnScroll(e -> {
            if(e.getDeltaY()<0){

                LocalDateTime lastTweetTime = null;
                if(tweetDetailList.getItems().size()>1){
                    lastTweetTime = tweetDetailList.getItems().get(tweetDetailList.getItems().size()-1).getTweetDateTime();
                }
                ListTweetEvent event = new ListTweetEvent(
                        MainController.MAX_TWEET_LIST_REQUEST_NUMBER,
                        lastTweetTime,
                        TweetListType.COMMENT,
                        this.parentTweetID);
                listTweetEventListener.listTweetEventOccurred(event);
            }

        });

        tweetDetailTab.showingProperty().addListener((obs, ov, nv) -> {
            if (nv) {
                final AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setTitleText("Tweet");
                appBar.getActionItems().addAll(mainAppBar.getActionItems());
            }
        });
    }

    @FXML
    void onBlockClick(ActionEvent event) {
        RelationUserEvent e =
                new RelationUserEvent(RelationType.BLOCK,mainTweet.getUserID());
        relationUserEventListener.relationUserEventOccurred(e);
    }

    @FXML
    void onForwardClick(ActionEvent event) {

    }

    @FXML
    void onMuteClick(ActionEvent event) {
        RelationUserEvent e =
                new RelationUserEvent(RelationType.MUTE,mainTweet.getUserID());
        relationUserEventListener.relationUserEventOccurred(e);
    }



    @FXML
    void onReportClick(ActionEvent event) {
        ActionTweetEvent e =
                new ActionTweetEvent(ActionType.SPAM,mainTweet.getTweetID());
        actionTweetEventListener.actionTweetEventOccurred(e);
    }

    @FXML
    void onVisitClick(ActionEvent event) {

    }

    void setMainTweet(TweetTile mainTweet){
        this.mainTweet = mainTweet;//new TweetTile(mainTweet);
        mainTweet.setMainTweet(true);
        tweetDetailList.getItems().clear();
        tweetDetailList.getItems().add(mainTweet);

    }

    public long getParentTweetID() {
        return parentTweetID;
    }

    public void setParentTweetID(long parentTweetID) {
        this.parentTweetID = parentTweetID;
        tweetDetailList.getItems().clear();
        getNewTweetEventListener.newTweetEventOccurred(this.parentTweetID);
        ListTweetEvent event = new ListTweetEvent(
                MainController.MAX_TWEET_LIST_REQUEST_NUMBER,
                null,
                TweetListType.COMMENT,
                this.parentTweetID);

        listTweetEventListener.listTweetEventOccurred(event);
    }

    public void onTweetReceive(Boolean isMainTweet, String response){
        TweetTile tweet = Utility.createTweetFromResponse(response);
        if(isMainTweet)
            tweet.setMainTweet(true);
        tweetDetailList.getItems().add(tweet);
    }


}
