package ir.sharif.ap.presenter;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.control.Snackbar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import ir.sharif.ap.Main;
import ir.sharif.ap.controller.PacketHandler;
import ir.sharif.ap.model.RelationListType;
import ir.sharif.ap.model.RelationTile;
import ir.sharif.ap.presenter.events.SearchUsernameEvent;
import ir.sharif.ap.presenter.listeners.RelationListEventListener;
import ir.sharif.ap.presenter.listeners.RelationUserEventListener;
import ir.sharif.ap.presenter.listeners.SearchUsernameEventListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.Base64;
import java.util.ResourceBundle;

import static ir.sharif.ap.Main.*;
import static ir.sharif.ap.presenter.Styles.DEFAULT_BUTTON_STYLE;

public class RelationListPresenter implements Initializable {
    private Snackbar snackbar;
    @FXML
    private View relationListTab;
    @FXML
    private TextField searchText;
    @FXML
    private ListView<RelationTile> relationListView;
    @FXML
    private HBox searchBar;
    private Button searchButton;
    private RelationListType relationListType;

    private RelationListEventListener relationListEventListener;
    private RelationUserEventListener relationUserEventListener;
    private SearchUsernameEventListener searchUsernameEventListener;

    public void addSearchUsernameEventListener(SearchUsernameEventListener searchUsernameEventListener) {
        this.searchUsernameEventListener = searchUsernameEventListener;
    }

    public void addRelationUserEventListener(RelationUserEventListener relationUserEventListener) {
        this.relationUserEventListener = relationUserEventListener;
    }

    public void addRelationListEventListener(RelationListEventListener relationListEventListener) {
        this.relationListEventListener = relationListEventListener;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        snackbar = new Snackbar("");
        searchButton = MaterialDesignIcon.SEARCH.button(e -> {
            searchUsernameEventListener.searchUsernameEventOccurred(
                    new SearchUsernameEvent(false,searchText.getText()));
        });
        searchButton.setStyle(DEFAULT_BUTTON_STYLE);
        searchBar.getChildren().add(searchButton);

        relationListView.setCellFactory(p->{
            RelationListCell relationListCell = new RelationListCell();
            relationListCell.addRelationUserEventListener(relationUserEventListener);
            return relationListCell;
        });
        relationListView.setPlaceholder(new Label("There are no Tweets"));
        relationListTab.showingProperty().addListener((obs, ov, nv) -> {
            if (nv) {

                final AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setTitleText("");
                appBar.getActionItems().addAll(mainAppBar.getActionItems());
                searchText.setText("");

                relationListType = Main.getRelationListType();
                if(relationListType!=null){
                    appBar.setTitleText(relationListType.toString());
                }
                relationListView.getItems().clear();
                relationListEventListener.relationListEventOccurred(relationListType);

            }
        });
    }

    public void onRelationListReceive(String response){

        String[] args = response.split(",", -1);

        RelationTile relationTile = new RelationTile();
        relationTile.setRelationType(relationListType);
        relationTile.setUserFullName(PacketHandler.getDecodedArg(args[1])+" "+PacketHandler.getDecodedArg(args[2]));
        relationTile.setUsername(PacketHandler.getDecodedArg(args[0]));
        byte[] userImage = null;
        if(!args[3].isEmpty())
            userImage = Base64.getDecoder().decode(args[3]);
        relationTile.setUserImage(userImage);
        relationTile.setUserID(Long.parseLong(args[4]));

        relationListView.getItems().add(relationTile);
    }

    public void onSearchResultReceive(String searchResult){
        String args[] = searchResult.split(",",-1);
        snackbar.setMessage(args[0]);
        snackbar.show();
        if(args[0].equals("found")){
            if(Main.getUserName().equals(searchText.getText())){
                MobileApplication.getInstance().switchView(MY_INFO_VIEW);
            }else{
                MobileApplication.getInstance().switchView(USERINFO_VIEW);
                Main.getUserInfoPresenter().setUserID(Long.parseLong(args[1]));
                Main.getUserInfoPresenter().update();
            }

        }else{
            snackbar.setMessage("User not found");
            snackbar.show();
        }
    }

}
