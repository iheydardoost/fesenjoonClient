package ir.sharif.ap.presenter;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.ListTile;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import ir.sharif.ap.Main;
import ir.sharif.ap.model.CollectionItem;
import ir.sharif.ap.model.CollectionListType;
import ir.sharif.ap.model.EditCollectionListType;
import ir.sharif.ap.presenter.listeners.DeleteCollectionEventListener;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;

import static ir.sharif.ap.Main.COLLECTION_EDIT_VIEW;
import static ir.sharif.ap.presenter.Styles.defaultButtonStyle;

public class ManageCollectionListCell extends ListCell<CollectionItem> {

    private final ListTile tile = new ListTile();
    private CollectionItem collectionItem;
    private Button editButton, deleteButton;
    private HBox buttonBox;
    private DeleteCollectionEventListener deleteCollectionEventListener;

    public void addDeleteCollectionEventListener(DeleteCollectionEventListener deleteCollectionEventListener) {
        this.deleteCollectionEventListener = deleteCollectionEventListener;
    }

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
        deleteButton = MaterialDesignIcon.DELETE.button(e->{
            CollectionListType collectionListType =  null;

            switch (collectionItem.getCollectionItemType()){
                    case CHAT:
                        collectionListType = CollectionListType.GROUP;
                        break;
                    case FOLDER:
                        collectionListType = CollectionListType.FOLDER;
                        break;
                    default:
                        break;
                }
            deleteCollectionEventListener.deleteCollectionEventOccurred(new DeleteCollectionEvent(collectionListType, collectionItem.getCollectionID()));
            listViewProperty().get().getItems().remove(collectionItem);
        });

        editButton.setStyle(defaultButtonStyle);
        deleteButton.setStyle(defaultButtonStyle);
        buttonBox = new HBox(editButton, deleteButton);
        tile.setSecondaryGraphic(buttonBox);
    }

    @Override
    protected void updateItem(CollectionItem item, boolean empty) {
        super.updateItem(item, empty);
        collectionItem = item;
        if (!empty && item != null) {
            if(collectionItem.getCollectionName().equals("savedMessages")) {
                deleteButton.setManaged(false);
                deleteButton.setVisible(false);
                editButton.setManaged(false);
                editButton.setVisible(false);
            }
            tile.textProperty().setAll(item.getCollectionName());
            setGraphic(tile);
        } else {
            setGraphic(null);
        }
    }
}
