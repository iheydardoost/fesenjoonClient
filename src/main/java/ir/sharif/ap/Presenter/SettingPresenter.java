package ir.sharif.ap.Presenter;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.control.BottomNavigationButton;
import com.gluonhq.charm.glisten.control.Snackbar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import ir.sharif.ap.Main;
import ir.sharif.ap.model.LastSeenStatus;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;

import java.net.URL;
import java.util.ResourceBundle;

import static ir.sharif.ap.Main.*;

public class SettingPresenter implements Initializable {


    private LogoutListener logoutListener;
    private UserDeleteListener userDeleteListener;
    private SettingInfoListener settingInfoListener;
    private SettingChangeFormListener settingChangeFormListener;

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

    private Snackbar snackbar;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        snackbar = new Snackbar("");

        ObservableList<String> privacyList= FXCollections.observableArrayList("Private","Public");
        ObservableList<String> lastSeenList=
                FXCollections.observableArrayList(
                        LastSeenStatus.EVERYONE.toString(),
                        LastSeenStatus.NO_ONE.toString(),
                        LastSeenStatus.FOLLOWINGS.toString());
        ObservableList<String> activityList= FXCollections.observableArrayList("Active","Inactive");
        privacyCombo.setItems(privacyList);
        lastSeenCombo.setItems(lastSeenList);
        activityCombo.setItems(activityList);

        settingTab.showingProperty().addListener((obs, ov, nv) -> {
            if (nv) {
                settingInfoListener.settingInfoRequestOccurred(true);
                privacyCombo.getSelectionModel().selectFirst();
                lastSeenCombo.getSelectionModel().selectFirst();
                activityCombo.getSelectionModel().selectFirst();

                navigationSetting.setSelected(true);

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


    @FXML
    void onApplyClick(ActionEvent event) {


        String privacyText = privacyCombo.getSelectionModel().getSelectedItem().toString();
        boolean accountPrivate = false;
        if(privacyText.equals("Private"))
            accountPrivate=true;

        String activityText = activityCombo.getSelectionModel().getSelectedItem().toString();
        boolean accountActive = false;
        if(activityText.equals("Active"))
            accountActive=true;

        String lastSeenText = lastSeenCombo.getSelectionModel().getSelectedItem().toString();

        SettingChangeFormEvent settingChangeFormEvent=new SettingChangeFormEvent();
        settingChangeFormEvent.setAccountActive(accountActive)
                .setPassword(passwordText.getText())
                .setAccountPrivate(accountPrivate)
                .setLastSeenStatus(LastSeenStatus.valueOf(lastSeenText));
        settingChangeFormListener.settingChangeOccurred(settingChangeFormEvent);
        settingInfoListener.settingInfoRequestOccurred(true);
    }

    public void onSettingInfoReceive(String responseBode){
        String[] args = responseBode.split(",",-1);
        String lastSeenStatus = args[1];
        String accountPrivate = args[3];
        String accountActive = args[5];

        lastSeenCombo.getSelectionModel().select(LastSeenStatus.valueOf(lastSeenStatus).ordinal());

        if(Boolean.parseBoolean(accountPrivate)){
            privacyCombo.getSelectionModel().select(0);
        }else {
            privacyCombo.getSelectionModel().select(1);
        }

        if(Boolean.parseBoolean(accountActive)){
            activityCombo.getSelectionModel().select(0);
        }else{
            activityCombo.getSelectionModel().select(1);
        }
    }

    public void onLogoutResponse(String result){
        showResult("Logout "+ result);
        if(result.equals("success")){
            MobileApplication.getInstance().switchView(HOME_VIEW);
        }
    }
    public void onUserDeleteResponse(String result){
        showResult("Delete "+ result);
        if(result.equals("success")){
            MobileApplication.getInstance().switchView(HOME_VIEW);
        }
    }

    public void onChangeResponse(String result){
        String[] args = result.split(",", -1);
        if(args[1].equals("password"))
            showResult("Change "+ args[1] + " " + args[0]);
    }

    public void showResult(String result){
        snackbar.setMessage(result);
        snackbar.show();
    }
    @FXML
    void onDeleteUserClick(ActionEvent event) {
        userDeleteListener.userDeleteOccurred(true);
    }

    @FXML
    void onLogoutClick(ActionEvent event) {
        logoutListener.logoutOccurred(true);
    }

    public void getSettingInfo(){
        settingInfoListener.settingInfoRequestOccurred(true);
    }

    public void addLogoutListener(LogoutListener logoutListener) {
        this.logoutListener = logoutListener;
    }

    public void addUserDeleteListener(UserDeleteListener userDeleteListener) {
        this.userDeleteListener = userDeleteListener;
    }

    public void addSettingInfoListener(SettingInfoListener settingInfoListener) {
        this.settingInfoListener = settingInfoListener;
    }

    public void addSettingChangeFormListener(SettingChangeFormListener settingChangeFormListener) {
        this.settingChangeFormListener = settingChangeFormListener;
    }


}
