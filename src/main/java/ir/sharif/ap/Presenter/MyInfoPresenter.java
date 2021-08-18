package ir.sharif.ap.Presenter;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import ir.sharif.ap.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static ir.sharif.ap.Main.mainAppBar;
import static ir.sharif.ap.Main.setRefreshTimeline;

public class MyInfoPresenter implements Initializable {

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        myInfoView.showingProperty().addListener((obs, ov, nv) -> {
            if (nv) {
                onInfoReceive();
                final AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setTitleText("My info");
                appBar.getActionItems().addAll(mainAppBar.getActionItems());
            }
        });
    }

    public void onInfoReceive(){

        dateOfBirthText.setText("Blah blah ");
        firstnameText.setText("Blah blah ");
        lastnameText.setText("Blah blah ");
        usernameTxt.setText("Blah blah ");
        emailText.setText("Blah blah ");
        phoneText.setText("Blah blah ");
        bioText.setText("Blah blah ");

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
