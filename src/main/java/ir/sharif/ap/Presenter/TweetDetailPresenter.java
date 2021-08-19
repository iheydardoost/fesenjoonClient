package ir.sharif.ap.Presenter;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import ir.sharif.ap.Main;
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

import static ir.sharif.ap.Main.*;

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
    private long parentTweetID = -1 ;

    private ActionTweetEventListener actionTweetEventListener;
    private RelationUserEventListener relationUserEventListener;
    private ListTweetEventListener listTweetEventListener;
    private GetTweetEventListener getTweetEventListener;

    public void addListTweetEventListener(ListTweetEventListener listTweetEventListener) {
        this.listTweetEventListener = listTweetEventListener;
    }

    public void addGetTweetEventListener(GetTweetEventListener getTweetEventListener) {
        this.getTweetEventListener = getTweetEventListener;
    }

    public void addRelationUserEventListener(RelationUserEventListener relationUserEventListener) {
        this.relationUserEventListener = relationUserEventListener;
    }

    public void addActionTweetEventListener(ActionTweetEventListener actionTweetEventListener) {
        this.actionTweetEventListener = actionTweetEventListener;
    }

    static LocalDateTime previousLastTweetTime = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        tweetDetailList.setCellFactory(p -> {
            TweetListCell tweetListCell = new TweetListCell();
            tweetListCell.addActionTweetEventListener(e -> Main.getMainController().handleActionTweetEvent(e));
            tweetListCell.addNewTweetListener(e -> Main.getMainController().handleNewTweetEvent(e));
            return tweetListCell;
        });
        tweetDetailList.setPlaceholder(new Label("There are no Tweets"));
        tweetDetailList.setOnScroll(e -> {
            if(e.getDeltaY()<0){

                LocalDateTime lastTweetTime = null;
                if(tweetDetailList.getItems().size()>1){
                    lastTweetTime = tweetDetailList.getItems().get(tweetDetailList.getItems().size()-1).getTweetDateTime();
                }
                if(previousLastTweetTime != null)
                    if(previousLastTweetTime.equals(lastTweetTime)){
                        return;
                    }
                previousLastTweetTime = lastTweetTime;
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
                if(parentTweetID>0){
                    tweetDetailList.getItems().clear();
                    getTweetEventListener.getTweetEventOccurred(this.parentTweetID);
                    ListTweetEvent event = new ListTweetEvent(
                            MainController.MAX_TWEET_LIST_REQUEST_NUMBER,
                            null,
                            TweetListType.COMMENT,
                            this.parentTweetID);

                    listTweetEventListener.listTweetEventOccurred(event);
                }

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
        RelationUserEvent e;
        if(mainTweet.isMute()){
            e =  new RelationUserEvent(RelationType.UNMUTE,mainTweet.getUserID());
        }else {
            e =  new RelationUserEvent(RelationType.MUTE,mainTweet.getUserID());
        }

        relationUserEventListener.relationUserEventOccurred(e);
    }

    public void onMuteResponse(String response){
        if(response.contains("success")){
            mainTweet.setMute(!mainTweet.isMute());
        }
        updateMuteText();
    }

    public void updateMuteText(){
        if(mainTweet.isMute())
            muteButton.setText("UnMute");
        else
            muteButton.setText("Mute");
    }

    @FXML
    void onReportClick(ActionEvent event) {
        ActionTweetEvent e =
                new ActionTweetEvent(ActionType.SPAM,mainTweet.getTweetID());
        actionTweetEventListener.actionTweetEventOccurred(e);
    }

    @FXML
    void onVisitClick(ActionEvent event) {

        if(Main.getUserName().equals(mainTweet.getUserName())){
            MobileApplication.getInstance().switchView(MY_INFO_VIEW);
        }else{
            MobileApplication.getInstance().switchView(USERINFO_VIEW);
            Main.getUserInfoPresenter().setUserID(mainTweet.getUserID());
            Main.getUserInfoPresenter().update();
        }


    }

    void setMainTweet(TweetTile mainTweet){
        this.mainTweet = mainTweet;//new TweetTile(mainTweet);
        mainTweet.setMainTweet(true);
        tweetDetailList.getItems().clear();
        updateMuteText();

        if(Main.getUserName().equals(mainTweet.getUserName())){

            blockButton.setManaged(false);
            blockButton.setVisible(false);

            reportButton.setManaged(false);
            reportButton.setVisible(false);

            muteButton.setManaged(false);
            muteButton.setVisible(false);

        }else{
            blockButton.setManaged(true);
            blockButton.setVisible(true);

            reportButton.setManaged(true);
            reportButton.setVisible(true);

            muteButton.setManaged(true);
            muteButton.setVisible(true);
        }
    }

    public long getParentTweetID() {
        return parentTweetID;
    }

    public void setParentTweetID(long parentTweetID) {
        this.parentTweetID = parentTweetID;

        tweetDetailList.getItems().clear();
        getTweetEventListener.getTweetEventOccurred(this.parentTweetID);
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
            setMainTweet(tweet);
        tweetDetailList.getItems().add(tweet);
    }


}
