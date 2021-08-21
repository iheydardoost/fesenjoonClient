package ir.sharif.ap.presenter;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import ir.sharif.ap.controller.PacketHandler;
import ir.sharif.ap.presenter.events.EditUserInfoEvent;
import ir.sharif.ap.presenter.listeners.EditUserInfoEventListener;
import ir.sharif.ap.presenter.listeners.GetPrivateInfoEventListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Base64;
import java.util.ResourceBundle;

import static ir.sharif.ap.Main.mainAppBar;

public class EditUserInfoPresenter implements Initializable {
    
    @FXML
    private View editUserInfoTab;

    @FXML
    private TextField userImagePath;

    @FXML
    private Button addImageButton;

    @FXML
    private TextArea bioText;

    @FXML
    private DatePicker dateOfBirthText;

    @FXML
    private TextField firstnameText;

    @FXML
    private TextField lastnameText;

    @FXML
    private TextField phoneNumberText;

    @FXML
    private Button applyButton;

    @FXML
    private Button cancelButton;
    private byte[] userImage=null, lastUserImage;
    private String lastFirstnameStr, lastLastnameStr, lastPhoneNumberStr, lastBioStr;
    private LocalDate lastDateOfBirth;
    private EditUserInfoEventListener editUserInfoEventListener;
    private GetPrivateInfoEventListener getPrivateInfoEventListener;

    public void addGetPrivateInfoEventListener(GetPrivateInfoEventListener getPrivateInfoEventListener) {
        this.getPrivateInfoEventListener = getPrivateInfoEventListener;
    }

    public void addEditUserInfoEventListener(EditUserInfoEventListener userInfoEventListener) {
        this.editUserInfoEventListener = userInfoEventListener;
    }


    @FXML
    void onAddImageClick(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        configureFileChooser(fileChooser);
        File file = fileChooser.showOpenDialog(null);
        if(file == null){
            return;
        }
        userImagePath.setText(file.toPath().toString());

        FileInputStream fin = null;
        try {
            fin = new FileInputStream(file);
            byte fileContent[] = new byte[(int)file.length()];
            fin.read(fileContent);
            userImage = Arrays.copyOf(fileContent,fileContent.length);

            //fileContent has data inside
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found" + e);
        }
        catch (IOException ioe) {
            System.out.println("Exception while reading file " + ioe);
        }
        finally {
            // close the streams using close method
            try {
                if (fin != null) {
                    fin.close();
                }
            }
            catch (IOException ioe) {
                System.out.println("Error while closing stream: " + ioe);
            }
        }
    }

    private static void configureFileChooser(final FileChooser fileChooser) {
        fileChooser.setTitle("Please select a picture");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png"),
                new FileChooser.ExtensionFilter("GIF", "*.gif")
        );
    }

    @FXML
    void onApplyButtonClick(ActionEvent event) {
        EditUserInfoEvent e = new EditUserInfoEvent();
        if (!phoneNumberText.getText().equals(lastPhoneNumberStr)) {
            e.setPhoneNumber(phoneNumberText.getText());
        }
        if (!bioText.getText().equals(lastBioStr)) {
            e.setBio(bioText.getText());
        }
        e.setDateOfBirth(dateOfBirthText.getValue());
        if (!firstnameText.getText().equals(lastFirstnameStr)) {
            e.setFirstName(firstnameText.getText());
        }
        if (!lastnameText.getText().equals(lastLastnameStr)) {
            e.setLastName(lastnameText.getText());
        }
        if (!Arrays.equals(lastUserImage, userImage)) {
            e.setUserImage(userImage);
        }
        editUserInfoEventListener.editUserInfoEventOccurred(e);
        getPrivateInfoEventListener.getPrivateInfoEventOccurred(true);

    }

    @FXML
    void onCancelButtonClick(ActionEvent event) {

        phoneNumberText.setText(lastPhoneNumberStr);
        bioText.setText(lastBioStr);
        dateOfBirthText.setValue(lastDateOfBirth);
        firstnameText.setText(lastFirstnameStr);
        lastnameText.setText(lastLastnameStr);


//        MobileApplication.getInstance().switchToPreviousView();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        editUserInfoTab.showingProperty().addListener((obs, ov, nv) -> {
            if (nv) {
                getPrivateInfoEventListener.getPrivateInfoEventOccurred(true);

                final AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setTitleText("Edit User Info");
                appBar.getActionItems().addAll(mainAppBar.getActionItems());
            }
        });
    }

    public void onUserInfoReceive(String response){
        String[] args = response.split(",",-1);

        String bio = PacketHandler.getDecodedArg(args[7]);
//        for(int i=8; i<args.length; i++){
//            bio += ("," + args[i]);
//        }
        lastBioStr = bio;

        lastDateOfBirth = null;
        if(!args[3].isEmpty())
            lastDateOfBirth = LocalDate.parse(args[3]);
        lastFirstnameStr = PacketHandler.getDecodedArg(args[1]);
        lastLastnameStr = PacketHandler.getDecodedArg(args[2]);
        lastUserImage = null;
        if(!args[6].isEmpty()){
            lastUserImage = Base64.getDecoder().decode(args[6]);
        }
        lastPhoneNumberStr = PacketHandler.getDecodedArg(args[5]);

        phoneNumberText.setText(lastPhoneNumberStr);
        bioText.setText(lastBioStr);
        dateOfBirthText.setValue(lastDateOfBirth);
        firstnameText.setText(lastFirstnameStr);
        lastnameText.setText(lastLastnameStr);

    }

}
