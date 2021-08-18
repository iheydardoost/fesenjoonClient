package ir.sharif.ap.Presenter;

import ir.sharif.ap.model.RelationTile;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.ResourceBundle;

public class RelationListPresenter implements Initializable {
    @FXML
    private ListView<RelationTile> relationListView;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        relationListView.setCellFactory(p->new RelationListCell());
        relationListView.setPlaceholder(new Label("There are no Tweets"));

    }
}
