package ir.sharif.ap.Presenter;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.control.BottomNavigationButton;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

import java.net.URL;
import java.util.ResourceBundle;

import static ir.sharif.ap.Main.*;

public class SettingPresenter implements Initializable {
    @FXML
    private View settingTab;

    @FXML
    private BottomNavigationButton navigationTimeline, navigationPrivate, navigationExplore, navigationMessaging, navigationSetting;


    final static String tabName="Setting";

    @FXML
    private ComboBox privacyCombo;

    @FXML
    private ComboBox lastSeenCombo;

    @FXML
    private ComboBox activityCombo;

    @FXML
    private PasswordField passwordText;

    @FXML
    private Button deleteUserButton;

    @FXML
    private Button applyButton;

    @FXML
    private Button logOutButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> privacyList= FXCollections.observableArrayList("Private","Public");
        ObservableList<String> lastSeenList= FXCollections.observableArrayList("Everyone","No one", "Followings");
        ObservableList<String> activityList= FXCollections.observableArrayList("Active","Inactive");
        privacyCombo.setItems(privacyList);
        lastSeenCombo.setItems(lastSeenList);
        activityCombo.setItems(activityList);

        settingTab.showingProperty().addListener((obs, ov, nv) -> {
            if (nv) {
                privacyCombo.getSelectionModel().selectFirst();
                lastSeenCombo.getSelectionModel().selectFirst();
                activityCombo.getSelectionModel().selectFirst();

                navigationSetting.setSelected(true);

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

    @FXML
    void onApplyClick(ActionEvent event) {
        String s = privacyCombo.getSelectionModel().getSelectedItem().toString();
        System.out.println("THIS IS "+ s );
    }

    @FXML
    void onDeleteUserClick(ActionEvent event) {

    }

    @FXML
    void onLogoutClick(ActionEvent event) {

    }

}
