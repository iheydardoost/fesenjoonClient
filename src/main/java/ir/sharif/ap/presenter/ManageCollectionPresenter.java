package ir.sharif.ap.presenter;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import ir.sharif.ap.Main;
import ir.sharif.ap.model.CollectionItemType;
import ir.sharif.ap.model.CollectionListType;
import ir.sharif.ap.model.CollectionItem;
import ir.sharif.ap.model.EditCollectionListType;
import ir.sharif.ap.presenter.listeners.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.LogManager;

import static ir.sharif.ap.Main.mainAppBar;
import static ir.sharif.ap.presenter.Styles.defaultButtonStyle;

public class ManageCollectionPresenter implements Initializable {

    @FXML
    private View manageCollectionTab;

    @FXML
    private HBox buttonBarBox, bottomButtonBar;

    @FXML
    private TextField newCollectionText;

    @FXML
    private ListView<CollectionItem> collectionListView;
    private Button addCollection;

    private CollectionListType collectionListType;
    private GetCollectionListEventListener getCollectionListEventListener;
    private NewCollectionEventListener newCollectionEventListener;
    private GetEditCollectionListEventListener getEditCollectionListEventListener;
    private SetEditCollectionListEventListener setEditCollectionListEventListener;
    private DeleteCollectionEventListener deleteCollectionEventListener;

    public void addDeleteCollectionEventListener(DeleteCollectionEventListener deleteCollectionEventListener) {
        this.deleteCollectionEventListener = deleteCollectionEventListener;
    }

    public void addGetEditCollectionListEventListener(GetEditCollectionListEventListener getEditCollectionListEventListener) {
        this.getEditCollectionListEventListener = getEditCollectionListEventListener;
    }

    public void addSetEditCollectionListEventListener(SetEditCollectionListEventListener setEditCollectionListEventListener) {
        this.setEditCollectionListEventListener = setEditCollectionListEventListener;
    }

    public void addGetCollectionListEventListener(GetCollectionListEventListener getCollectionListEventListener) {
        this.getCollectionListEventListener = getCollectionListEventListener;
    }

    public void addNewCollectionEventListener(NewCollectionEventListener newCollectionEventListener) {
        this.newCollectionEventListener = newCollectionEventListener;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        collectionListView.setCellFactory(e -> {
            ManageCollectionListCell manageCollectionListCell = new ManageCollectionListCell();

            return manageCollectionListCell;
        });

        addCollection = MaterialDesignIcon.ADD.button(actionEvent -> {
            if(newCollectionText.getText().isEmpty())
                return;
            newCollectionEventListener.newCollectionEventOccurred(new NewCollectionEvent(collectionListType, newCollectionText.getText()));
            newCollectionText.setText("");

            System.out.println("Added Collection");
        });
        addCollection.setStyle(defaultButtonStyle);
        buttonBarBox.getChildren().add(addCollection);

        manageCollectionTab.showingProperty().addListener((obs, ov, nv) -> {
            if (nv) {
                final AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.getActionItems().addAll(mainAppBar.getActionItems());
                collectionListType = Main.getCollectionType();
                appBar.setTitleText(collectionListType.toString());

                if(collectionListType == CollectionListType.GROUP){
                    collectionListView.setPlaceholder(new Label("There are no groups"));

                }else if(collectionListType == CollectionListType.FOLDER){
                    appBar.setTitleText("Mange Folders");
                    collectionListView.setPlaceholder(new Label("There are no folders"));

                }
                collectionListView.getItems().clear();
                getCollectionListEventListener.getCollectionListEventOccurred(collectionListType);
//                onCollectionReceive("");
            }
        });
    }

    public void onCollectionReceive(String response){
        String args[] = response.split(",", -1);
        CollectionItem collectionItem = new CollectionItem();
        if(collectionListType == CollectionListType.GROUP){
            collectionItem.setCollectionItemType(CollectionItemType.CHAT);
        }else if(collectionListType == CollectionListType.FOLDER){
            collectionItem.setCollectionItemType(CollectionItemType.FOLDER);
        }
        collectionItem.setCollectionID(Long.parseLong(args[0]));
        collectionItem.setCollectionName(args[1]);
        collectionListView.getItems().add(collectionItem);

    }


}
