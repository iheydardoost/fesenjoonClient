package ir.sharif.ap.presenter;

import com.gluonhq.charm.glisten.afterburner.GluonPresenter;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.control.BottomNavigationButton;
import com.gluonhq.charm.glisten.mvc.View;
import ir.sharif.ap.Main;
import ir.sharif.ap.controller.MainController;
import ir.sharif.ap.model.TweetListType;
import ir.sharif.ap.model.TweetTile;
import ir.sharif.ap.presenter.events.ListTweetEvent;
import ir.sharif.ap.presenter.listeners.ListTweetEventListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

import static ir.sharif.ap.Main.*;

public class TimeLinePresenter extends GluonPresenter<Main> implements Initializable {
    @FXML
    private View timelineTab;

    @FXML
    private BottomNavigationButton navigationTimeline, navigationPrivate, navigationExplore, navigationMessaging, navigationSetting;

    @FXML
    private ListView<TweetTile> timelineListView;

    private ListTweetEventListener listTweetEventListener;
    private final String TAB_NAME ="Timeline";
    private static LocalDateTime previousLastTweetTime = null;

    public void addListTweetEventListener(ListTweetEventListener listTweetEventListener) {
        this.listTweetEventListener = listTweetEventListener;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        timelineListView.setCellFactory(p -> {
            TweetListCell tweetListCell = new TweetListCell();
            tweetListCell.addActionTweetEventListener(e -> Main.getMainController().handleActionTweetEvent(e));
            tweetListCell.addNewTweetListener(e -> Main.getMainController().handleNewTweetEvent(e));

            return tweetListCell;
        });

        timelineListView.setPlaceholder(new Label("There are no Tweets"));
        timelineListView.setOnScroll(e -> {
            if(e.getDeltaY()<0) {
                LocalDateTime lastTweetTime = null;
                if(timelineListView.getItems().size()>0){
                    lastTweetTime = timelineListView.getItems().get(timelineListView.getItems().size()-1).getTweetDateTime();
                }
                if(previousLastTweetTime != null)
                    if(previousLastTweetTime.equals(lastTweetTime)){
                        return;
                    }
                previousLastTweetTime = lastTweetTime;
                ListTweetEvent event = new ListTweetEvent(
                        MainController.MAX_TWEET_LIST_REQUEST_NUMBER,
                        lastTweetTime,
                        TweetListType.TIMELINE,
                        0);
                listTweetEventListener.listTweetEventOccurred(event);
            }
        });


        timelineTab.showingProperty().addListener((obs, ov, nv) -> {
            if (nv) {
                if(Main.refreshTimeline()){
                    Main.setRefreshTimeline(false);
                    timelineListView.getItems().clear();
                    ListTweetEvent event = new ListTweetEvent(
                            MainController.MAX_TWEET_LIST_REQUEST_NUMBER,
                            null,
                            TweetListType.TIMELINE,
                            0);
                    listTweetEventListener.listTweetEventOccurred(event);
                }

                navigationTimeline.setSelected(true);

                final AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setTitleText(TAB_NAME);
                appBar.getActionItems().addAll(mainAppBar.getActionItems());
            }
        });
    }

    public void onTweetReceive(String response){
        TweetTile tweet = Utility.createTweetFromResponse(response);
        timelineListView.getItems().add(tweet);
    }

    public void onNavigationBarChange(ActionEvent e){
        if(e.getSource() == navigationTimeline){
            Main.setRefreshTimeline(true);
            MobileApplication.getInstance().switchView(TIMELINE_VIEW);
        }else if(e.getSource() == navigationPrivate){
            MobileApplication.getInstance().switchView(PRIVATE_VIEW);
        }else if(e.getSource() == navigationExplore){
            Main.setRefreshExplore(true);
            MobileApplication.getInstance().switchView(EXPLORE_VIEW);
        }else if(e.getSource() == navigationMessaging){
            MobileApplication.getInstance().switchView(MESSAGING_VIEW);
        }else if(e.getSource() == navigationSetting){
            MobileApplication.getInstance().switchView(SETTING_VIEW);
        }
    }


}
