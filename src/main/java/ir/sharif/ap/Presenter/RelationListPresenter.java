package ir.sharif.ap.Presenter;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.control.Snackbar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import ir.sharif.ap.Main;
import ir.sharif.ap.controller.MainController;
import ir.sharif.ap.model.RelationListType;
import ir.sharif.ap.model.RelationTile;
import ir.sharif.ap.model.RelationType;
import ir.sharif.ap.model.TweetListType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

import static ir.sharif.ap.Main.USERINFO_VIEW;
import static ir.sharif.ap.Main.mainAppBar;
import static ir.sharif.ap.Presenter.Styles.defaultButtonStyle;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        snackbar = new Snackbar("");
        searchButton = MaterialDesignIcon.SEARCH.button(e -> {
            onSearchResultReceive("Ok");
        });
        searchButton.setStyle(defaultButtonStyle);
        searchBar.getChildren().add(searchButton);

        relationListView.setCellFactory(p->new RelationListCell());
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

                onRelationListReceive("");
            }
        });
    }

    public void onRelationListReceive(String response){
        RelationTile relationTile = new RelationTile();
        relationTile.setRelationType(relationListType);
        relationTile.setUserFullName("Mamad Agha");
        relationTile.setUsername("Javaad");
        relationListView.getItems().add(relationTile);
        relationListView.getItems().add(relationTile);
        relationListView.getItems().add(relationTile);
        relationListView.getItems().add(relationTile);
        relationListView.getItems().add(relationTile);
        relationListView.getItems().add(relationTile);
    }

    public void onSearchResultReceive(String searchResult){
        if(false){
            snackbar.setMessage("User not found");
            snackbar.show();
        }else{
            MobileApplication.getInstance().switchView(USERINFO_VIEW);
            Main.getUserInfoPresenter().setUserID(1);
            Main.getUserInfoPresenter().update();
        }
    }
}
