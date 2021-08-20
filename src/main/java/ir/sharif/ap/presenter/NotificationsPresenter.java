package ir.sharif.ap.presenter;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import ir.sharif.ap.Main;
import ir.sharif.ap.model.NotificationListType;
import ir.sharif.ap.model.NotificationTile;
import ir.sharif.ap.presenter.listeners.FollowResponseEventListener;
import ir.sharif.ap.presenter.listeners.GetNotificationsEventListener;
import ir.sharif.ap.presenter.listeners.GetPendingListEventListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.Base64;
import java.util.ResourceBundle;

import static ir.sharif.ap.Main.mainAppBar;

public class NotificationsPresenter implements Initializable {

    @FXML
    private View notificationListTab;

    @FXML
    private ListView<NotificationTile> notificationListView;
    private NotificationListType notificationListType;

    private FollowResponseEventListener followResponseEventListener;
    private GetNotificationsEventListener getNotificationsEventListener;
    private GetPendingListEventListener getPendingListEventListener;

    public void addFollowResponseEventListener(FollowResponseEventListener followResponseEventListener) {
        this.followResponseEventListener = followResponseEventListener;
    }

    public void addGetNotificationsEventListener(GetNotificationsEventListener getNotificationsEventListener) {
        this.getNotificationsEventListener = getNotificationsEventListener;
    }

    public void addGetPendingListEventListener(GetPendingListEventListener getPendingListEventListener) {
        this.getPendingListEventListener = getPendingListEventListener;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        notificationListView.setCellFactory(p->{
            NotificationListCell notificationListCel= new NotificationListCell();
            notificationListCel.addFollowResponseEventListener(followResponseEventListener);
            return notificationListCel;
        });
        notificationListView.setPlaceholder(new Label("There are no notifications"));

        notificationListTab.showingProperty().addListener((obs, ov, nv) -> {
            if (nv) {
                final AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setTitleText("");
                appBar.getActionItems().addAll(mainAppBar.getActionItems());


                notificationListType = Main.getNotificationListType();
                if(notificationListType!=null){
                    appBar.setTitleText(notificationListType.toString());
                }
                notificationListView.getItems().clear();
                switch (notificationListType){
                    case NOTIFICATION_LIST:
                        getNotificationsEventListener.getNotificationsEventOccurred(false);
                        break;
                    case SYSTEM_NOTIFICATIONS:
                        getNotificationsEventListener.getNotificationsEventOccurred(true);
                        break;
                    case MY_PENDING_LIST:
                        getPendingListEventListener.getPendingListEventOccurred(true);
                        break;
                }
            }
        });
    }

    public void onNotificationReceive(String response){
        String[] args = response.split(",", -1);

        NotificationTile notificationTile = new NotificationTile();
        notificationTile.setNotificationListType(notificationListType);
        notificationTile.setStatusText(args[4]);
        notificationTile.setUsername(args[0]);
        notificationTile.setUserFullName(args[1] + " "+ args[2]);
        byte[] userImage = null;
        if(!args[3].isEmpty())
            userImage = Base64.getDecoder().decode(args[3]);
        notificationTile.setUserImage(userImage);

        notificationListView.getItems().add(notificationTile);
    }

}
