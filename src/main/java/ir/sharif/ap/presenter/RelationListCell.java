package ir.sharif.ap.presenter;

import com.gluonhq.charm.glisten.control.Avatar;
import com.gluonhq.charm.glisten.control.ListTile;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import ir.sharif.ap.Main;
import ir.sharif.ap.model.RelationListType;
import ir.sharif.ap.model.RelationTile;
import ir.sharif.ap.model.RelationType;
import ir.sharif.ap.presenter.events.RelationUserEvent;
import ir.sharif.ap.presenter.listeners.RelationUserEventListener;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static ir.sharif.ap.presenter.Styles.DEFAULT_BUTTON_STYLE;

public class RelationListCell extends ListCell<RelationTile> {

    private final ListTile tile = new ListTile();
    private RelationTile relationTile;
    private Avatar avatar;
    private Button unlockButton;
    private long userID;

    private RelationUserEventListener relationUserEventListener;

    public void addRelationUserEventListener(RelationUserEventListener relationUserEventListener) {
        this.relationUserEventListener = relationUserEventListener;
    }

    {
        avatar = new Avatar();
        final Button button = MaterialDesignIcon.DELETE.button();

        tile.setPrimaryGraphic(avatar);

        unlockButton = MaterialDesignIcon.LOCK_OPEN.button(e->{
            listViewProperty().get().getItems().remove(relationTile);
            RelationUserEvent relationUserEvent = new RelationUserEvent(RelationType.UNBLOCK, userID);
            relationUserEventListener.relationUserEventOccurred(relationUserEvent);

        });
        unlockButton.setStyle(DEFAULT_BUTTON_STYLE);
        tile.setSecondaryGraphic(new VBox(unlockButton));
    }

    @Override
    protected void updateItem(RelationTile item, boolean empty) {
        super.updateItem(item, empty);
        relationTile = item;
        if (!empty && item != null) {
            userID = relationTile.getUserID();
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

            if(relationTile.getRelationType() == RelationListType.BLACKLIST){
                unlockButton.setManaged(true);
                unlockButton.setVisible(true);
            }else{
                unlockButton.setManaged(false);
                unlockButton.setVisible(false);
            }

            setGraphic(tile);
        } else {
            setGraphic(null);
        }
    }
}
