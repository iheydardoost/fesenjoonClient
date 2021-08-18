package ir.sharif.ap.Presenter;

import com.gluonhq.charm.glisten.control.Avatar;
import com.gluonhq.charm.glisten.control.ListTile;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import ir.sharif.ap.model.RelationTile;
import ir.sharif.ap.model.TweetTile;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class RelationListCell extends ListCell<RelationTile> {

    private final ListTile tile = new ListTile();
    private RelationTile relationTile;
    private Avatar avatar;

    {
        avatar = new Avatar();
        final Button button = MaterialDesignIcon.DELETE.button();
        tile.setSecondaryGraphic(new VBox(button));
        tile.setPrimaryGraphic(avatar);

    }

    @Override
    protected void updateItem(RelationTile item, boolean empty) {
        super.updateItem(item, empty);
        relationTile = item;
        if (!empty && item != null) {
            tile.textProperty().setAll(item.getUserFullName());
            if(item.getUserImage()!=null){
                ByteArrayInputStream bis = new ByteArrayInputStream(item.getUserImage());
                avatar.setImage( new Image(bis));
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            setGraphic(tile);
        } else {
            setGraphic(null);
        }
    }
}
