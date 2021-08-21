package ir.sharif.ap.presenter;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.Alert;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.control.Snackbar;
import com.gluonhq.charm.glisten.mvc.View;
import ir.sharif.ap.Main;
import ir.sharif.ap.controller.PacketHandler;
import ir.sharif.ap.model.CollectionItem;
import ir.sharif.ap.model.CollectionItemType;
import ir.sharif.ap.model.CollectionListType;
import ir.sharif.ap.model.EditCollectionListType;
import ir.sharif.ap.presenter.events.GetEditCollectionListEvent;
import ir.sharif.ap.presenter.events.NewMessageEvent;
import ir.sharif.ap.presenter.events.SetEditCollectionListEvent;
import ir.sharif.ap.presenter.listeners.GetEditCollectionListEventListener;
import ir.sharif.ap.presenter.listeners.GetSelectListEventListener;
import ir.sharif.ap.presenter.listeners.NewMessageEventListener;
import ir.sharif.ap.presenter.listeners.SetEditCollectionListEventListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.net.URL;
import java.time.LocalDateTime;
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

    private GetEditCollectionListEventListener getEditCollectionListEventListener;
    private SetEditCollectionListEventListener setEditCollectionListEventListener;
    private GetSelectListEventListener getSelectListEventListener;
    private NewMessageEventListener newMessageEventListener;

    public void addNewMessageEventListener(NewMessageEventListener newMessageEventListener) {
        this.newMessageEventListener = newMessageEventListener;
    }

    public void addGetEditCollectionListEventListener(GetEditCollectionListEventListener getEditCollectionListEventListener) {
        this.getEditCollectionListEventListener = getEditCollectionListEventListener;
    }

    public void addSetEditCollectionListEventListener(SetEditCollectionListEventListener setEditCollectionListEventListener) {
        this.setEditCollectionListEventListener = setEditCollectionListEventListener;
    }

    public void addGetSelectListEventListener(GetSelectListEventListener getSelectListEventListener) {
        this.getSelectListEventListener = getSelectListEventListener;
    }

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
                switch (editCollectionListType){
                    case EDIT_CHAT:
                        getEditCollectionListEventListener.getEditCollectionListEventOccurred(new GetEditCollectionListEvent(CollectionListType.GROUP, targetCollectionID));
                        break;
                    case EDIT_FOLDER:
                        getEditCollectionListEventListener.getEditCollectionListEventOccurred(new GetEditCollectionListEvent(CollectionListType.FOLDER, targetCollectionID));
                        break;
                    case FORWARD_LIST:
                    case NEW_MESSAGE:
                        getSelectListEventListener.getSelectListEventOccurred(true);
                        break;
                }
            }
        });
    }

    public void onEditCollectionReceive(String response){

        String[] args = response.split(",", -1);
        CollectionItem collectionItem = new CollectionItem();
        collectionItem.setCollectionID(0L);
        collectionItem.setCollectionName(PacketHandler.getDecodedArg(args[0]) + " " + PacketHandler.getDecodedArg(args[1]));
        collectionItem.setCollectionItemType(CollectionItemType.USER);
        collectionItem.setUserName(PacketHandler.getDecodedArg(args[2]));
        collectionItem.setSelected(Boolean.parseBoolean(args[3]));
        editCollectionListView.getItems().add(collectionItem);
    }

    public void onSelectCollectionReceive(String response){
        String[] args = response.split(",", -1);
        CollectionItem collectionItem = new CollectionItem();
        collectionItem.setCollectionID(Long.parseLong(args[2]));
        collectionItem.setCollectionName(PacketHandler.getDecodedArg(args[0]));
        collectionItem.setCollectionItemType(CollectionItemType.valueOf(args[1]));
        collectionItem.setSelected(false);
        editCollectionListView.getItems().add(collectionItem);
    }


    public long getTargetCollectionID() {
        return targetCollectionID;
    }

    public void setTargetCollectionID(long targetCollectionID) {
        this.targetCollectionID = targetCollectionID;
    }

    @FXML
    void onApplyButton(ActionEvent event) {

        List<CollectionItem> numbers = editCollectionListView.getItems();
        ArrayList<CollectionItem> arrayList = new ArrayList<>(numbers);
        arrayList.removeIf(collectionItem -> !collectionItem.getSelected());

        if(editCollectionListType == EditCollectionListType.NEW_MESSAGE){
            Main.setMessageReceiverList(arrayList);
            MobileApplication.getInstance().switchToPreviousView();

        } else if( editCollectionListType == EditCollectionListType.FORWARD_LIST){

            if(arrayList == null || arrayList.size()<1){
                Alert alert = new Alert(javafx.scene.control.Alert.AlertType.ERROR);
                alert.setTitleText("Error");
                alert.setContentText("Please select message receiver.");
                alert.showAndWait();
                return;
            }
            NewMessageEvent newMessageEvent =new NewMessageEvent();
            newMessageEvent.setForwarded(true)
                    .setMsgText(Main.getForwardMessageText())
                    .setMsgImage(Main.getForwardMessageImage())
                    .setMsgDateTime(LocalDateTime.now());

            for (CollectionItem c: arrayList) {
                newMessageEvent
                        .addID(c.getCollectionID())
                        .addCollectionItemType(c.getCollectionItemType());
            }

            newMessageEventListener.newMessageEventOccurred(newMessageEvent);

            Main.clearMessageReceiverList();
            MobileApplication.getInstance().switchToPreviousView();

        }else{
            checkedItems="";
            arrayList.forEach(collectionItem -> {checkedItems += collectionItem.getCollectionItemType() +","+collectionItem.getCollectionID()+","; });
            SetEditCollectionListEvent setEditCollectionListEvent = new SetEditCollectionListEvent();
            setEditCollectionListEvent.setCollectionID(targetCollectionID);
            for (CollectionItem collectionItem: arrayList) {
                setEditCollectionListEvent.addUserName(collectionItem.getUserName());
            }

            switch (editCollectionListType){
                case EDIT_CHAT:
                    setEditCollectionListEvent.setCollectionListType(CollectionListType.GROUP);
                    break;
                case EDIT_FOLDER:
                    setEditCollectionListEvent.setCollectionListType(CollectionListType.FOLDER);
                    break;
            }
            setEditCollectionListEventListener.setEditCollectionListEventOccurred(setEditCollectionListEvent);
        }
    }

    public void onResponseReceive(String response){
        snackbar.setMessage(response);
        snackbar.show();
        if(response.contains("success"))
            MobileApplication.getInstance().switchToPreviousView();
    }

    @FXML
    void onCancelButton(ActionEvent event) {
        MobileApplication.getInstance().switchToPreviousView();
    }

}
