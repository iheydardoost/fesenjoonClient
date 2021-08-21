package ir.sharif.ap.presenter;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import ir.sharif.ap.Main;
import ir.sharif.ap.controller.PacketHandler;
import ir.sharif.ap.presenter.listeners.GetPrivateInfoEventListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Base64;
import java.util.ResourceBundle;

import static ir.sharif.ap.Main.mainAppBar;

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

    private GetPrivateInfoEventListener getPrivateInfoEventListener;

    public void addGetPrivateInfoEventListener(GetPrivateInfoEventListener getPrivateInfoEventListener) {
        this.getPrivateInfoEventListener = getPrivateInfoEventListener;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        myInfoView.showingProperty().addListener((obs, ov, nv) -> {
            if (nv) {
                getPrivateInfoEventListener.getPrivateInfoEventOccurred(true);
                final AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setTitleText("My info");
                appBar.getActionItems().addAll(mainAppBar.getActionItems());
            }
        });
    }

    public void onInfoReceive(String response){
        String[] args = response.split(",",-1);

        usernameTxt.setText(PacketHandler.getDecodedArg(args[0]));
        firstnameText.setText(PacketHandler.getDecodedArg(args[1]));
        lastnameText.setText(PacketHandler.getDecodedArg(args[2]));
        dateOfBirthText.setText(args[3]);
        emailText.setText(PacketHandler.getDecodedArg(args[4]));
        phoneText.setText(PacketHandler.getDecodedArg(args[5]));

        String bio = PacketHandler.getDecodedArg(args[7]);
//        for(int i=8; i<args.length; i++){
//            bio += ("," + args[i]);
//        }
        bioText.setText(bio);

        Image userImage = null;
        byte[] imageByteArray = null;
        if(!args[6].isEmpty()){
            imageByteArray = Base64.getDecoder().decode(args[6]);
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
