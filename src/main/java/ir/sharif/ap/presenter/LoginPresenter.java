package ir.sharif.ap.presenter;
import com.gluonhq.charm.glisten.application.ViewStackPolicy;
import com.gluonhq.charm.glisten.control.*;
import com.gluonhq.charm.glisten.control.TextArea;
import com.gluonhq.charm.glisten.control.TextField;
import ir.sharif.ap.Main;
import ir.sharif.ap.presenter.events.AuthFormEvent;
import ir.sharif.ap.presenter.listeners.AuthFormListener;
import javafx.scene.control.DatePicker;
import com.gluonhq.charm.glisten.animation.FadeInLeftBigTransition;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.layout.Layer;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import com.gluonhq.charm.glisten.visual.Swatch;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;


import static ir.sharif.ap.Main.PRIVATE_VIEW;

public class LoginPresenter implements Initializable {
    private AuthFormListener authFormListener;

    @FXML
    private View home;

    @FXML
    private RadioButton loginRadio, sigupRadio;

    @FXML
    private Layer loginView ;

    @FXML
    private Layer signupView ;

    @FXML
    private Button loginButton;
    @FXML
    private Label loginErrorText, signupErrorText;
    @FXML
    private TextField loginUsernameText, signupUsernameText, signupFirstNameTxt, signupLastNameTxt, signupPhoneTxt, signupEmailTxt;
    @FXML
    private PasswordField loginPasswordText, signupPasswordText;

    @FXML
    private DatePicker signupDateTxt;
    @FXML
    private TextArea signupBiographyTxt;

    private Snackbar snackbar;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        FloatingActionButton fab = new FloatingActionButton();
//        fab.setOnAction(e -> {
//            System.out.println("FAB click");
//        });
//        fab.showOn(home);

        home.setShowTransitionFactory(v -> new FadeInLeftBigTransition(v));
        showProperLoginView();

        snackbar = new Snackbar("");

        home.showingProperty().addListener((obs, ov, nv) -> {
            if (nv) {
                final AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setVisible(false);
                appBar.setNavIcon(MaterialDesignIcon.MENU.button(e -> System.out.println("menu")));
                appBar.setTitleText("The Home View");
                appBar.getActionItems().addAll(
                        MaterialDesignIcon.SEARCH.button(),
                        MaterialDesignIcon.FAVORITE.button());
                appBar.getMenuItems().addAll(new MenuItem("Settings"));

                Swatch.PURPLE.assignTo(home.getScene());
            }
        });
    }


    @FXML
    public void onClick() {
        System.out.println("cl1ick");
    }

    public void showProperLoginView(){

        if(loginRadio.isSelected()){
            signupView.setManaged(false);
            signupView.setVisible(false);

            loginView.setVisible(true);
            loginView.setManaged(true);
        }else if(sigupRadio.isSelected()){
            loginView.setManaged(false);
            loginView.setVisible(false);

            signupView.setManaged(true);
            signupView.setVisible(true);
        }
    }

    public void onRadioChange(ActionEvent actionEvent) {
        showProperLoginView();
    }

    public void onAuthReceive(String responseBody){
        String[] responseArray=responseBody.split(",");
        snackbar.setMessage(responseArray[1]);
        snackbar.show();
        if(responseArray[0].equals("success")){
            Main.setUserName(loginUsernameText.getText());
            MobileApplication.getInstance().switchView(PRIVATE_VIEW, ViewStackPolicy.SKIP);
        }
    }

    public void onSignupReceive(String responseBody){
        String[] responseArray=responseBody.split(",");
        snackbar.setMessage(responseArray[1]);
        snackbar.show();
        if(responseArray[0].equals("success")){
            Main.setUserName(signupUsernameText.getText());
            MobileApplication.getInstance().switchView(PRIVATE_VIEW, ViewStackPolicy.SKIP);
        }
    }


    public void onLoginClick(ActionEvent actionEvent) {

        loginErrorText.setText("");
        if(loginUsernameText.getText().isEmpty()){
            loginErrorText.setText("Please enter username");
        }else if(loginPasswordText.getText().isEmpty()){
            loginErrorText.setText("Please enter password");
        }else{
            AuthFormEvent authFormEvent = new AuthFormEvent();
            authFormEvent.setLoginReq(true)
                    .setUserName(loginUsernameText.getText())
                    .setPassword(loginPasswordText.getText());
            authFormListener.authFormEventOccurred(authFormEvent);
        }
    }

    public void onSignupClick(ActionEvent actionEvent) {
        signupErrorText.setText("");
        if(signupFirstNameTxt.getText().isEmpty()){
            signupErrorText.setText("Please enter first name");
        }else if(signupLastNameTxt.getText().isEmpty()) {
            signupErrorText.setText("Please enter last name");
        }else if(signupUsernameText.getText().isEmpty()) {
            signupErrorText.setText("Please enter user name");
        }else if(signupPasswordText.getText().isEmpty()) {
            signupErrorText.setText("Please enter password");
        }else if(signupEmailTxt.getText().isEmpty()) {
            signupErrorText.setText("Please enter email");
        }else{

            AuthFormEvent authFormEvent = new AuthFormEvent();
            authFormEvent.setLoginReq(false)
                    .setFirstName(signupFirstNameTxt.getText())
                    .setLastName(signupLastNameTxt.getText())
                    .setUserName(signupUsernameText.getText())
                    .setPassword(signupPasswordText.getText())
                    .setPhoneNumber(signupPhoneTxt.getText())
                    .setEmail(signupEmailTxt.getText())
                    .setDateOfBirth(signupDateTxt.getValue())
                    .setBio(signupBiographyTxt.getText());
            authFormListener.authFormEventOccurred(authFormEvent);
        }
    }

    public void addAuthFormListener(AuthFormListener loginFormListener) {
        this.authFormListener = loginFormListener;
    }
}
