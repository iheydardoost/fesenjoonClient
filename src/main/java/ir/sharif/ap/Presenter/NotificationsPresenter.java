package ir.sharif.ap.Presenter;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import ir.sharif.ap.Main;
import ir.sharif.ap.model.NotificationListType;
import ir.sharif.ap.model.NotificationTile;
import ir.sharif.ap.model.RelationTile;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.ResourceBundle;

import static ir.sharif.ap.Main.mainAppBar;

public class NotificationsPresenter implements Initializable {

    @FXML
    private View notificationListTab;

    @FXML
    private ListView<NotificationTile> notificationListView;
    private NotificationListType notificationListType;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        notificationListView.setCellFactory(p->new NotificationListCell());
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

                onNotificationReceive("");
            }
        });
    }

    public void onNotificationReceive(String response){
        NotificationTile notificationTile = new NotificationTile();
        notificationTile.setNotificationListType(notificationListType);
        notificationTile.setStatusText("Unfollowed You");
        notificationTile.setUsername("Javaad");
        notificationTile.setUserFullName("Mamad Agha");


        notificationListView.getItems().add(notificationTile);
        notificationListView.getItems().add(notificationTile);
        notificationListView.getItems().add(notificationTile);
        notificationListView.getItems().add(notificationTile);
        notificationListView.getItems().add(notificationTile);
        notificationListView.getItems().add(notificationTile);

    }

}
