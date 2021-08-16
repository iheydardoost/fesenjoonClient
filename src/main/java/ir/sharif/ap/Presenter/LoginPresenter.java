package ir.sharif.ap.Presenter;

import com.gluonhq.charm.glisten.animation.FadeInLeftBigTransition;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.control.TextField;
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

import static ir.sharif.ap.Main.TIMELINE_VIEW;

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
    private Label loginErrorText;
    @FXML
    private TextField loginUsernameText;
    @FXML
    private PasswordField loginPasswordText;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        FloatingActionButton fab = new FloatingActionButton();
//        fab.setOnAction(e -> {
//            System.out.println("FAB click");
//        });
//        fab.showOn(home);

        home.setShowTransitionFactory(v -> new FadeInLeftBigTransition(v));
        showProperLoginView();
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
            System.out.println("loginRadio");
            signupView.setManaged(false);
            signupView.setVisible(false);

            loginView.setVisible(true);
            loginView.setManaged(true);
        }else if(sigupRadio.isSelected()){
            System.out.println("sigupRadio");

            loginView.setManaged(false);
            loginView.setVisible(false);

            signupView.setManaged(true);
            signupView.setVisible(true);
        }
    }

    public void onRadioChange(ActionEvent actionEvent) {
        showProperLoginView();
    }

    private void onAuthReceive(int result){
        if(result == 0){
            System.out.println("Authentication successful");
            MobileApplication.getInstance().switchView(TIMELINE_VIEW);
        }else if(result == 1){
            System.out.println("User does not exist");
        }else if(result == 2){
            System.out.println("Wrong password");
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

    public void addAuthFormListener(AuthFormListener loginFormListener) {
        this.authFormListener = loginFormListener;
    }
}
