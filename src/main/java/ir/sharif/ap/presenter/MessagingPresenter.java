package ir.sharif.ap.presenter;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.control.BottomNavigationButton;
import com.gluonhq.charm.glisten.mvc.View;
import ir.sharif.ap.Main;
import ir.sharif.ap.model.CollectionListType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

import static ir.sharif.ap.Main.*;

public class MessagingPresenter implements Initializable {
    @FXML
    private View messagingTab;

    @FXML
    private BottomNavigationButton navigationTimeline, navigationPrivate, navigationExplore, navigationMessaging, navigationSetting;

    @FXML
    private Button chatsRoomButton;

    @FXML
    private Button manageCollectionButton;

    @FXML
    private Button manageGroupsButton;

    @FXML
    private Button newMessageButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        messagingTab.showingProperty().addListener((obs, ov, nv) -> {
            if (nv) {
                navigationMessaging.setSelected(true);

                final AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setTitleText("Messaging");
                appBar.getActionItems().addAll(mainAppBar.getActionItems());


            }
        });
    }

    @FXML
    void onChatsRoomClick(ActionEvent event) {
        MobileApplication.getInstance().switchView(CHATS_ROOM_VIEW);
    }

    @FXML
    void onManageCollectionClick(ActionEvent event) {
        Main.setCollectionType(CollectionListType.FOLDER);
        MobileApplication.getInstance().switchView(MANAGE_COLLECTION_VIEW);

    }

    @FXML
    void onManageGroupsClick(ActionEvent event) {
        Main.setCollectionType(CollectionListType.GROUP);
        MobileApplication.getInstance().switchView(MANAGE_COLLECTION_VIEW);

    }

    @FXML
    void onNewMessageClick(ActionEvent event) {
        MobileApplication.getInstance().switchView(NEW_MESSAGE_VIEW);
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
