package ir.sharif.ap.presenter;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import ir.sharif.ap.Main;
import ir.sharif.ap.controller.MainController;
import ir.sharif.ap.model.TweetListType;
import ir.sharif.ap.model.TweetTile;
import ir.sharif.ap.presenter.events.ListTweetEvent;
import ir.sharif.ap.presenter.listeners.ListTweetEventListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

import static ir.sharif.ap.Main.mainAppBar;

public class MyTweetListPresenter implements Initializable {
    

    @FXML
    private View myTweetView;
    private ListTweetEventListener listTweetEventListener;

    @FXML
    private ListView<TweetTile> myTweetListView;
    static LocalDateTime previousLastTweetTime = null;

    public void addListTweetEventListener(ListTweetEventListener listTweetEventListener) {
        this.listTweetEventListener = listTweetEventListener;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        myTweetListView.setCellFactory(p -> {
            TweetListCell tweetListCell = new TweetListCell();
            tweetListCell.addActionTweetEventListener(e -> Main.getMainController().handleActionTweetEvent(e));
            tweetListCell.addNewTweetListener(e -> Main.getMainController().handleNewTweetEvent(e));

            return tweetListCell;
        });

        myTweetListView.setPlaceholder(new Label("There are no Tweets"));
        myTweetListView.setOnScroll(e -> {
            if(e.getDeltaY()<0) {
                LocalDateTime lastTweetTime = null;
                if(myTweetListView.getItems().size()>0){
                    lastTweetTime = myTweetListView.getItems().get(myTweetListView.getItems().size()-1).getTweetDateTime();
                }
                if(previousLastTweetTime != null)
                    if(previousLastTweetTime.equals(lastTweetTime)){
                        return;
                    }
                previousLastTweetTime = lastTweetTime;
                ListTweetEvent event = new ListTweetEvent(
                        MainController.MAX_TWEET_LIST_REQUEST_NUMBER,
                        lastTweetTime,
                        TweetListType.MY_TWEETS,
                        0);
                listTweetEventListener.listTweetEventOccurred(event);
            }
        });

        myTweetView.showingProperty().addListener((obs, ov, nv) -> {
            if (nv) {
                myTweetListView.getItems().clear();
                ListTweetEvent event = new ListTweetEvent(
                        MainController.MAX_TWEET_LIST_REQUEST_NUMBER,
                        null,
                        TweetListType.MY_TWEETS,
                        0);
                listTweetEventListener.listTweetEventOccurred(event);


                final AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setTitleText("My Tweet List");
                appBar.getActionItems().addAll(mainAppBar.getActionItems());
            }
        });
    }

    public void onTweetReceive(String response){
        TweetTile tweet = Utility.createTweetFromResponse(response);
        myTweetListView.getItems().add(tweet);
    }
}
