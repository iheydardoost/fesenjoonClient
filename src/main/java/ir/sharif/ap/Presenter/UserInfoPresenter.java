package ir.sharif.ap.Presenter;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.control.Snackbar;
import com.gluonhq.charm.glisten.mvc.View;
import ir.sharif.ap.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Struct;
import java.util.ResourceBundle;

import static ir.sharif.ap.Main.mainAppBar;

public class UserInfoPresenter implements Initializable {

    @FXML
    private View myInfoView;

    @FXML
    private ImageView userImageView;

    @FXML
    private Label dateOfBirthText;

    @FXML
    private Label firstnameText;

    @FXML
    private Label lastnameText;

    @FXML
    private Label usernameTxt;

    @FXML
    private Label emailText;

    @FXML
    private Label phoneText;

    @FXML
    private Label bioText;

    @FXML
    private Label dateOfBirthLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Label phoneLabel;

    @FXML
    private Label bioLabel;

    @FXML
    private Button followButton;

    @FXML
    private Button blockButton;

    @FXML
    private Button reportButton;

    @FXML
    private Button messageButton;

    @FXML
    private Label lastSeenText;

    @FXML
    private Label isFollowingText;
    private Snackbar snackbar;
    private long userID;
    private boolean isFollowing, hasBlocked;

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public void update(){
        onInfoReceive();

    }

    @FXML
    void onBlockButton(ActionEvent event) {

    }

    @FXML
    void onFollowButton(ActionEvent event) {

    }

    @FXML
    void onMessageButton(ActionEvent event) {

    }

    @FXML
    void onReportButton(ActionEvent event) {
        showMessage("You reported Him");
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        snackbar = new Snackbar("");
        myInfoView.showingProperty().addListener((obs, ov, nv) -> {
            if (nv) {
                final AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setTitleText("User info");
                appBar.getActionItems().addAll(mainAppBar.getActionItems());
            }
        });
    }

    public void showIsFollowingSection(boolean show) {
        if (show) {
            emailText.setManaged(true);
            emailText.setVisible(true);

            phoneText.setManaged(true);
            phoneText.setVisible(true);

            dateOfBirthText.setManaged(true);
            dateOfBirthText.setVisible(true);

            emailLabel.setManaged(true);
            emailLabel.setVisible(true);

            phoneLabel.setManaged(true);
            phoneLabel.setVisible(true);

            dateOfBirthLabel.setManaged(true);
            dateOfBirthLabel.setVisible(true);

        } else {
            emailText.setManaged(false);
            emailText.setVisible(false);

            phoneText.setManaged(false);
            phoneText.setVisible(false);

            dateOfBirthText.setManaged(false);
            dateOfBirthText.setVisible(false);

            emailLabel.setManaged(false);
            emailLabel.setVisible(false);

            phoneLabel.setManaged(false);
            phoneLabel.setVisible(false);

            dateOfBirthLabel.setManaged(false);
            dateOfBirthLabel.setVisible(false);
        }
    }

    public void showMessage(String message){
        snackbar.setMessage(message);
        snackbar.show();
    }

    public void onInfoReceive(){
        firstnameText.setText("Blah blah ");
        lastnameText.setText("Blah blah ");
        usernameTxt.setText("Blah blah ");

        bioText.setText("Blah blah ");
        lastSeenText.setText("Yesterday");
        isFollowing = false;
        hasBlocked = true;

        showIsFollowingSection(isFollowing);
        isFollowingText.setText(isFollowing?"Yes":"No");
        followButton.setText("Follow");
        blockButton.setText("Block");

        if(isFollowing){
            emailText.setText("Blah blah ");
            phoneText.setText("Blah blah ");
            dateOfBirthText.setText("Blah blah ");
            followButton.setText("Unfollow");
        }

        if(hasBlocked){
            blockButton.setText("Unblock");
        }

        Image userImage = null;
        byte[] imageByteArray = null;
        if(imageByteArray!=null){
            System.out.println("Or Here ");;

            ByteArrayInputStream bis = new ByteArrayInputStream(imageByteArray);
            userImage = new Image(bis);
            try {
                bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            userImage = new Image(Main.class.getResourceAsStream("/ir/sharif/ap/images/avatar.png"));
            System.out.println("Heeereee ");;

        }

        userImageView.setImage(userImage);
    }
}
