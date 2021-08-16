package ir.sharif.ap.Presenter;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.control.BottomNavigationButton;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

import static ir.sharif.ap.Main.*;

public class ExplorePresenter implements Initializable {
    @FXML
    private View exploreTab;

    @FXML
    private BottomNavigationButton navigationTimeline, navigationPrivate, navigationExplore, navigationMessaging, navigationSetting;


    final static String tabName="Explore";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Label b = new Label("Hello Dear this is "+tabName+" PAge");

        exploreTab.setCenter(b);
        exploreTab.showingProperty().addListener((obs, ov, nv) -> {
            if (nv) {
                navigationExplore.setSelected(true);

                final AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setTitleText(tabName);
                appBar.getActionItems().addAll(
                        MaterialDesignIcon.ARROW_BACK.button(e->MobileApplication.getInstance().switchToPreviousView()),
                        MaterialDesignIcon.POWER_SETTINGS_NEW.button(e-> Platform.exit()));

            }
        });
    }

    public void onNavigationBarChange(ActionEvent e){

        if(e.getSource() == navigationTimeline){
            MobileApplication.getInstance().switchView(TIMELINE_VIEW);

            System.out.println("This Action navigationTimeline");
        }else if(e.getSource() == navigationPrivate){
            MobileApplication.getInstance().switchView(PRIVATE_VIEW);

            System.out.println("This Action navigationPrivate");

        }else if(e.getSource() == navigationExplore){
            MobileApplication.getInstance().switchView(EXPLORE_VIEW);

            System.out.println("This Action navigationExplore");

        }else if(e.getSource() == navigationMessaging){
            MobileApplication.getInstance().switchView(MESSAGING_VIEW);

            System.out.println("This Action navigationMessaging");

        }else if(e.getSource() == navigationSetting){
            MobileApplication.getInstance().switchView(SETTING_VIEW);
            System.out.println("This Action NavigationSetting");
        }
    }


}
