package ir.sharif.ap.presenter;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.control.BottomNavigationButton;
import com.gluonhq.charm.glisten.mvc.View;
import ir.sharif.ap.Main;
import ir.sharif.ap.model.NotificationListType;
import ir.sharif.ap.model.RelationListType;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

import static ir.sharif.ap.Main.*;

public class PrivatePresenter implements Initializable {
    @FXML
    private View privateTab;

    @FXML
    private BottomNavigationButton navigationTimeline, navigationPrivate, navigationExplore, navigationMessaging, navigationSetting;

    @FXML
    private Button newTweetButton;

    @FXML
    private Button followersButton;

    final static String tabName="Private";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        privateTab.showingProperty().addListener((obs, ov, nv) -> {
            if (nv) {
                navigationPrivate.setSelected(true);

                final AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setTitleText(tabName);
                appBar.getActionItems().addAll(mainAppBar.getActionItems());


            }
        });
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

    @FXML
    public void onFollowersClicked(ActionEvent event) {
        System.out.println("clicked Followers");
    }

    @FXML
    void onNewTweetClicked(ActionEvent event) {
        System.out.println("New Tweet");
        MobileApplication.getInstance().switchView(NEW_TWEET_VIEW);
        Main.getNewTweetPresenter().setParentTweetID(0);
    }


    @FXML
    void onBlockListClick(ActionEvent event) {
        Main.setRelationListType(RelationListType.BLACKLIST);
        MobileApplication.getInstance().switchView(RELATION_LIST_VIEW);
    }

    @FXML
    void onEditUserInfoClick(ActionEvent event) {
        MobileApplication.getInstance().switchView(EDIT_USER_INFO_VIEW);
    }

    @FXML
    void onFollowersClick(ActionEvent event) {
        Main.setRelationListType(RelationListType.FOLLOWER_LIST);
        MobileApplication.getInstance().switchView(RELATION_LIST_VIEW);
    }

    @FXML
    void onFollowingsClick(ActionEvent event) {
        Main.setRelationListType(RelationListType.FOLLOWING_LIST);
        MobileApplication.getInstance().switchView(RELATION_LIST_VIEW);
    }

    @FXML
    void onMyInfoButtonClick(ActionEvent event) {
        MobileApplication.getInstance().switchView(MY_INFO_VIEW);
    }

    @FXML
    void onMyTweetListClick(ActionEvent event) {
        MobileApplication.getInstance().switchView(MY_TWEET_LIST_VIEW);
    }

    @FXML
    void onNotificationListButton(ActionEvent event) {
        Main.setNotificationListType(NotificationListType.NOTIFICATION_LIST);
        MobileApplication.getInstance().switchView(NOTIFICATIONS_VIEW);
    }

    @FXML
    void onSystemNotificationClick(ActionEvent event) {
        Main.setNotificationListType(NotificationListType.SYSTEM_NOTIFICATIONS);
        MobileApplication.getInstance().switchView(NOTIFICATIONS_VIEW);
    }

    @FXML
    void onMyPendingListClick(ActionEvent event) {
        Main.setNotificationListType(NotificationListType.MY_PENDING_LIST);
        MobileApplication.getInstance().switchView(NOTIFICATIONS_VIEW);
    }
}
