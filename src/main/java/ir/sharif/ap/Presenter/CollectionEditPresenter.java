package ir.sharif.ap.Presenter;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.control.Snackbar;
import com.gluonhq.charm.glisten.mvc.View;
import ir.sharif.ap.Main;
import ir.sharif.ap.model.CollectionItem;
import ir.sharif.ap.model.CollectionItemType;
import ir.sharif.ap.model.EditCollectionListType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static ir.sharif.ap.Main.mainAppBar;

public class CollectionEditPresenter implements Initializable {

    private Snackbar snackbar;
    @FXML
    private View collectionEditTab;

    @FXML
    private Button cancelButton;

    @FXML
    private Button applyButton;

    @FXML
    private ListView<CollectionItem> editCollectionListView;
    private EditCollectionListType editCollectionListType;
    private long targetCollectionID;
    private String checkedItems = "";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        snackbar = new Snackbar("");

        editCollectionListView.setCellFactory(e -> {
            CollectionEditListCell collectionEditListCell = new CollectionEditListCell();

            return collectionEditListCell;
        });

        collectionEditTab.showingProperty().addListener((obs, ov, nv) -> {
            if (nv) {
                final AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.getActionItems().addAll(mainAppBar.getActionItems());
                editCollectionListType = Main.getEditCollectionListType();
                targetCollectionID = Main.getTargetCollectionID();
                appBar.setTitleText(editCollectionListType.toString());

                editCollectionListView.getItems().clear();
                onCollectionReceive("");

            }
        });
    }

    public void onCollectionReceive(String response){
        CollectionItem collectionItem = new CollectionItem();
        collectionItem.setCollectionID(231l);
        collectionItem.setCollectionName("user1");
        collectionItem.setCollectionItemType(CollectionItemType.USER);
        collectionItem.setSelected(false);

        CollectionItem collectionItem2 = new CollectionItem();
        collectionItem2.setCollectionID(1l);
        collectionItem2.setCollectionName("user12");
        collectionItem2.setCollectionItemType(CollectionItemType.USER);
        collectionItem2.setSelected(false);

        CollectionItem collectionItem3 = new CollectionItem();
        collectionItem3.setCollectionID(2l);
        collectionItem3.setCollectionName("user3");
        collectionItem3.setCollectionItemType(CollectionItemType.USER);
        collectionItem3.setSelected(false);

        CollectionItem collectionItem4 = new CollectionItem();
        collectionItem4.setCollectionID(3l);
        collectionItem4.setCollectionName("user4");
        collectionItem4.setCollectionItemType(CollectionItemType.USER);
        collectionItem4.setSelected(false);

        CollectionItem collectionItem5 = new CollectionItem();
        collectionItem5.setCollectionID(4l);
        collectionItem5.setCollectionName("user5");
        collectionItem5.setCollectionItemType(CollectionItemType.USER);
        collectionItem5.setSelected(false);

        editCollectionListView.getItems().add(collectionItem);
        editCollectionListView.getItems().add(collectionItem2);
        editCollectionListView.getItems().add(collectionItem3);
        editCollectionListView.getItems().add(collectionItem4);
        editCollectionListView.getItems().add(collectionItem5);


    }

    public long getTargetCollectionID() {
        return targetCollectionID;
    }

    public void setTargetCollectionID(long targetCollectionID) {
        this.targetCollectionID = targetCollectionID;
    }

    void onApplyResponse(String response){
        snackbar.setMessage(response);
        snackbar.show();
        MobileApplication.getInstance().switchToPreviousView();
    }
    @FXML
    void onApplyButton(ActionEvent event) {
        System.out.println("Apply");
        List<CollectionItem> numbers = editCollectionListView.getItems();
        ArrayList<CollectionItem> arrayList = new ArrayList<>(numbers);
        arrayList.removeIf(collectionItem -> !collectionItem.getSelected());

        if(editCollectionListType == EditCollectionListType.NEW_MESSAGE){
            Main.setMessageReceiverList(arrayList);
            MobileApplication.getInstance().switchToPreviousView();

        }else{
            checkedItems="";
            arrayList.forEach(collectionItem -> {checkedItems += collectionItem.getCollectionItemType() +","+collectionItem.getCollectionID()+","; });

        }

    }

    @FXML
    void onCancelButton(ActionEvent event) {
        MobileApplication.getInstance().switchToPreviousView();
    }

}
