package ir.sharif.ap.Presenter;

import com.gluonhq.charm.glisten.control.ListTile;
import ir.sharif.ap.model.CollectionItem;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;

public class CollectionEditListCell extends ListCell<CollectionItem> {

    private final ListTile tile = new ListTile();
    private CollectionItem collectionItem;
    private CheckBox checkBox;
    private HBox buttonBox;

    {
        checkBox = new CheckBox();
        tile.setSecondaryGraphic(new HBox(checkBox));
        checkBox.setOnAction(actionEvent -> collectionItem.setSelected(checkBox.isSelected()));

    }

    @Override
    protected void updateItem(CollectionItem item, boolean empty) {
        super.updateItem(item, empty);
        collectionItem = item;
        if (!empty && item != null) {
            tile.textProperty().setAll(item.getCollectionName());
            checkBox.setSelected(item.getSelected());
            setGraphic(tile);
        } else {
            setGraphic(null);
        }
    }
}
