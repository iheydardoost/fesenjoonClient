package ir.sharif.ap.Presenter;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.ListTile;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import ir.sharif.ap.Main;
import ir.sharif.ap.model.CollectionItem;
import ir.sharif.ap.model.EditCollectionListType;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;

import static ir.sharif.ap.Main.COLLECTION_EDIT_VIEW;
import static ir.sharif.ap.Presenter.Styles.defaultButtonStyle;

public class ManageCollectionListCell extends ListCell<CollectionItem> {

    private final ListTile tile = new ListTile();
    private CollectionItem collectionItem;
    private Button editButton, DeleteButton;
    private HBox buttonBox;

    {
        editButton = MaterialDesignIcon.EDIT.button(e->{
            switch (collectionItem.getCollectionItemType()){
                case CHAT:
                    Main.setEditCollectionListType(EditCollectionListType.EDIT_CHAT);
                    break;
                case FOLDER:
                    Main.setEditCollectionListType(EditCollectionListType.EDIT_FOLDER);
                    break;
                default:
                    break;
            }

            Main.setTargetCollectionID(collectionItem.getCollectionID());
            MobileApplication.getInstance().switchView(COLLECTION_EDIT_VIEW);
        });
        DeleteButton = MaterialDesignIcon.DELETE.button(e->{

            listViewProperty().get().getItems().remove(collectionItem);
        });

        editButton.setStyle(defaultButtonStyle);
        DeleteButton.setStyle(defaultButtonStyle);
        buttonBox = new HBox(editButton, DeleteButton);
        tile.setSecondaryGraphic(buttonBox);
    }

    @Override
    protected void updateItem(CollectionItem item, boolean empty) {
        super.updateItem(item, empty);
        collectionItem = item;
        if (!empty && item != null) {
            tile.textProperty().setAll(item.getCollectionName());
            setGraphic(tile);
        } else {
            setGraphic(null);
        }
    }
}