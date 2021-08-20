package ir.sharif.ap.presenter;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.ResourceBundle;

import static ir.sharif.ap.Main.mainAppBar;

public class SamplePresenter implements Initializable {

    @FXML
    private View tab;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tab.showingProperty().addListener((obs, ov, nv) -> {
            if (nv) {
                final AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setTitleText("");
                appBar.getActionItems().addAll(mainAppBar.getActionItems());
            }
        });
    }

}
