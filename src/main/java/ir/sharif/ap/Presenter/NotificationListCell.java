package ir.sharif.ap.Presenter;

import com.gluonhq.charm.glisten.control.Avatar;
import com.gluonhq.charm.glisten.control.ListTile;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import ir.sharif.ap.Main;
import ir.sharif.ap.model.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static ir.sharif.ap.Presenter.Styles.defaultButtonStyle;

public class NotificationListCell extends ListCell<NotificationTile> {

    private final ListTile tile = new ListTile();
    private NotificationTile notificationTile;
    private Avatar avatar;
    private Button acceptButton, rejectWithNotifyButton, rejectWithoutNotifyButton;
    private Label statusText;
    private HBox buttonBox;

    private FollowResponseEventListener followResponseEventListener;

    public void addFollowResponseEventListener(FollowResponseEventListener followResponseEventListener) {
        this.followResponseEventListener = followResponseEventListener;
    }

    {
        avatar = new Avatar();
        final Button button = MaterialDesignIcon.DELETE.button();

        tile.setPrimaryGraphic(avatar);

        acceptButton = MaterialDesignIcon.THUMB_UP.button(e->{
            followResponseEventListener.followResponseEventOccurred(new FollowResponseEvent(FollowResponseType.ACCEPT,
                    notificationTile.getUsername()));
            listViewProperty().get().getItems().remove(notificationTile);
        });
        rejectWithNotifyButton = MaterialDesignIcon.THUMB_DOWN.button(e->{
            followResponseEventListener.followResponseEventOccurred(new FollowResponseEvent(FollowResponseType.REJECT_NOTIFY,
                    notificationTile.getUsername()));
            listViewProperty().get().getItems().remove(notificationTile);
        });
        rejectWithoutNotifyButton = MaterialDesignIcon.NOTIFICATIONS_OFF.button(e->{
            followResponseEventListener.followResponseEventOccurred(new FollowResponseEvent(FollowResponseType.REJECT,
                    notificationTile.getUsername()));
            listViewProperty().get().getItems().remove(notificationTile);
        });

        acceptButton.setStyle(defaultButtonStyle);
        rejectWithNotifyButton.setStyle(defaultButtonStyle);
        rejectWithoutNotifyButton.setStyle(defaultButtonStyle);
        buttonBox = new HBox(acceptButton, rejectWithNotifyButton, rejectWithoutNotifyButton);

        statusText = new Label("");
    }

    @Override
    protected void updateItem(NotificationTile item, boolean empty) {
        super.updateItem(item, empty);
        notificationTile = item;
        if (!empty && item != null) {
            tile.textProperty().setAll(item.getUserFullName(), item.getUsername());
            if(item.getUserImage()!=null){
                ByteArrayInputStream bis = new ByteArrayInputStream(item.getUserImage());
                avatar.setImage( new Image(bis));
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                avatar.setImage( new Image(Main.class.getResourceAsStream("/ir/sharif/ap/images/avatar.png")));
            }

            if(notificationTile.getNotificationListType() == NotificationListType.NOTIFICATION_LIST){
                buttonBox.setManaged(true);
                buttonBox.setVisible(true);

                tile.setSecondaryGraphic(buttonBox);
            }else {
                statusText.setText(notificationTile.getStatusText());
                buttonBox.setManaged(false);
                buttonBox.setVisible(false);

                tile.setSecondaryGraphic(statusText);
            }

            setGraphic(tile);
        } else {
            setGraphic(null);
        }
    }
}
