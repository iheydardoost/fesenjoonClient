package ir.sharif.ap.presenter;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.control.Snackbar;
import com.gluonhq.charm.glisten.mvc.View;
import ir.sharif.ap.Main;
import ir.sharif.ap.controller.PacketHandler;
import ir.sharif.ap.model.RelationType;
import ir.sharif.ap.presenter.events.RelationUserEvent;
import ir.sharif.ap.presenter.listeners.GetChatIDByUserIDEventListener;
import ir.sharif.ap.presenter.listeners.GetUserInfoEventListener;
import ir.sharif.ap.presenter.listeners.RelationUserEventListener;
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
import java.util.Base64;
import java.util.ResourceBundle;

import static ir.sharif.ap.Main.CHAT_VIEW;
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
    private boolean isFollowing;

    private GetUserInfoEventListener getUserInfoEventListener;
    private RelationUserEventListener relationUserEventListener;
    private GetChatIDByUserIDEventListener getChatIDByUserIDEventListener;

    public void addGetChatIDByUserIDEventListener(GetChatIDByUserIDEventListener getChatIDByUserIDEventListener) {
        this.getChatIDByUserIDEventListener = getChatIDByUserIDEventListener;
    }

    public void addRelationUserEventListener(RelationUserEventListener relationUserEventListener) {
        this.relationUserEventListener = relationUserEventListener;
    }

    public void addGetUserInfoEventListener(GetUserInfoEventListener getUserInfoEventListener) {
        this.getUserInfoEventListener = getUserInfoEventListener;
    }


    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public void update(){
        getUserInfoEventListener.getUserInfoEventOccurred(userID);
    }

    @FXML
    void onBlockButton(ActionEvent event) {
        RelationUserEvent e = new RelationUserEvent(RelationType.BLOCK,userID);
        relationUserEventListener.relationUserEventOccurred(e);
    }

    @FXML
    void onFollowButton(ActionEvent event) {
        RelationType relationType=RelationType.FOLLOW;

        if(isFollowing)
            relationType = RelationType.UNFOLLOW;
        RelationUserEvent e = new RelationUserEvent(relationType, userID);
        relationUserEventListener.relationUserEventOccurred(e);
        update();
    }

    @FXML
    void onMessageButton(ActionEvent event) {
        getChatIDByUserIDEventListener.getChatIDByUserIDEventOccurred(userID);
    }

    public void onChatIDReceive(String response){
        Main.setChatID(Long.parseLong(response));
        MobileApplication.getInstance().switchView(CHAT_VIEW);
    }

    @FXML
    void onReportButton(ActionEvent event) {
        RelationUserEvent e = new RelationUserEvent(RelationType.REPORT,userID);
        relationUserEventListener.relationUserEventOccurred(e);
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

    public void onResponseReceive(String response){

        showMessage(response);
    }

    public void updateFollowText(){
        followButton.setText("Follow");
        if(isFollowing){
            followButton.setText("Unfollow");
        }
    }

    public void onInfoReceive(String response){
        String[] args = response.split(",",-1);

        firstnameText.setText(PacketHandler.getDecodedArg(args[2]));
        lastnameText.setText(PacketHandler.getDecodedArg(args[3]));
        usernameTxt.setText(PacketHandler.getDecodedArg(args[1]));

        String bio = PacketHandler.getDecodedArg(args[10]);
        bioText.setText(bio);
        lastSeenText.setText(args[8]);
        isFollowing = Boolean.parseBoolean(args[9]);

        showIsFollowingSection(isFollowing);
        isFollowingText.setText(isFollowing?"Yes":"No");
        blockButton.setText("Block");

        if(isFollowing){
            emailText.setText(PacketHandler.getDecodedArg(args[5]));
            phoneText.setText(PacketHandler.getDecodedArg(args[6]));
            dateOfBirthText.setText(args[4]);
        }
        updateFollowText();

        Image userImage = null;
        if(!args[7].isEmpty()){
            byte[] imageByteArray = Base64.getDecoder().decode(args[7]);

            ByteArrayInputStream bis = new ByteArrayInputStream(imageByteArray);
            userImage = new Image(bis);
            try {
                bis.close();
            } catch (IOException e) {
                //e.printStackTrace();
            }
        } else {
            userImage = new Image(Main.class.getResourceAsStream("/ir/sharif/ap/images/avatar.png"));
        }

        userImageView.setImage(userImage);
    }
}
