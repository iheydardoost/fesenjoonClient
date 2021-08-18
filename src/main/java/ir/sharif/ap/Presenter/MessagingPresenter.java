package ir.sharif.ap.Presenter;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.control.BottomNavigationButton;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import ir.sharif.ap.Main;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

import static ir.sharif.ap.Main.*;

public class MessagingPresenter implements Initializable {
    @FXML
    private View messagingTab;

    @FXML
    private BottomNavigationButton navigationTimeline, navigationPrivate, navigationExplore, navigationMessaging, navigationSetting;


    final static String tabName="Messaging";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Label b = new Label("Hello Dear this is "+tabName+" PAge");

        messagingTab.setCenter(b);
        messagingTab.showingProperty().addListener((obs, ov, nv) -> {
            if (nv) {
                navigationMessaging.setSelected(true);

                final AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setTitleText(tabName);
                appBar.getActionItems().addAll(mainAppBar.getActionItems());


            }
        });
    }

    public void onNavigationBarChange(ActionEvent e){
        if(e.getSource() == navigationTimeline){
            Main.setRefreshTimeline(true);
            MobileApplication.getInstance().switchView(TIMELINE_VIEW);
        }else if(e.getSource() == navigationPrivate){
            MobileApplication.getInstance().switchView(PRIVATE_VIEW);
        }else if(e.getSource() == navigationExplore){
            Main.setRefreshExplore(true);
            MobileApplication.getInstance().switchView(EXPLORE_VIEW);
        }else if(e.getSource() == navigationMessaging){
            MobileApplication.getInstance().switchView(MESSAGING_VIEW);
        }else if(e.getSource() == navigationSetting){
            MobileApplication.getInstance().switchView(SETTING_VIEW);
        }
    }

}
