package ir.sharif.ap.Presenter;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.control.BottomNavigation;
import com.gluonhq.charm.glisten.control.FloatingActionButton;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import com.gluonhq.charm.glisten.visual.Swatch;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class TimeLinePresenter implements Initializable {
    @FXML
    private View timeline;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

//        FloatingActionButton fab = new FloatingActionButton();
//        fab.showOn(timeline);

        Label b = new Label("Hello Dear this is Time Line PAge");

        timeline.setCenter(b);
        timeline.showingProperty().addListener((obs, ov, nv) -> {
            if (nv) {
                final AppBar appBar = MobileApplication.getInstance().getAppBar();
//                appBar.setNavIcon(MaterialDesignIcon.MENU.button(e -> System.out.println("menu")));
                appBar.setTitleText("TimeLine");
                appBar.getActionItems().addAll(
                        MaterialDesignIcon.ARROW_BACK.button(),
                        MaterialDesignIcon.POWER_SETTINGS_NEW.button());

            }
        });

//        timeline.setBottom(b2);
//        fab.setOnAction(e -> {
//            System.out.println("timeline click");
//        });
//        fab.showOn(timeline);

    }
}
