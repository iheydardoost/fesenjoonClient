package ir.sharif.ap.Presenter;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.control.BottomNavigationButton;
import com.gluonhq.charm.glisten.control.FloatingActionButton;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import ir.sharif.ap.Main;
import ir.sharif.ap.model.RelationListType;
import ir.sharif.ap.model.RelationType;
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
        MobileApplication.getInstance().switchView(RELATION_LIST_VIEW);
        Main.getRelationListPresenter().setRelationType(RelationListType.BLACKLIST);
        Main.getRelationListPresenter().update();
    }

    @FXML
    void onEditUserInfoClick(ActionEvent event) {
        MobileApplication.getInstance().switchView(EDIT_USER_INFO_VIEW);
    }

    @FXML
    void onFollowersClick(ActionEvent event) {
        MobileApplication.getInstance().switchView(RELATION_LIST_VIEW);
        Main.getRelationListPresenter().setRelationType(RelationListType.FOLLOWER_LIST);
        Main.getRelationListPresenter().update();
    }

    @FXML
    void onFollowingsClick(ActionEvent event) {
        MobileApplication.getInstance().switchView(RELATION_LIST_VIEW);
        Main.getRelationListPresenter().setRelationType(RelationListType.FOLLOWING_LIST);
        Main.getRelationListPresenter().update();
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
    void onNotificationsClick(ActionEvent event) {

    }
}
