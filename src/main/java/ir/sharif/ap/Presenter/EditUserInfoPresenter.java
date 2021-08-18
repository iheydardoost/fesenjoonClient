package ir.sharif.ap.Presenter;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
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
    private byte[] userImage, lastUserImage;
    private String lastFirstnameStr, lastLastnameStr, lastPhoneNumberStr, lastBioStr;
    LocalDate lastDateOfBirth;

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
        if (!phoneNumberText.getText().equals(lastPhoneNumberStr)) {

        }
        if (!bioText.getText().equals(lastBioStr)) {

        }
        if (lastDateOfBirth.compareTo(dateOfBirthText.getValue()) != 0) {

        }
        if (!firstnameText.getText().equals(lastFirstnameStr)) {

        }
        if (!lastnameText.getText().equals(lastLastnameStr)) {

        }
        if (!Arrays.equals(lastUserImage, userImage)) {

        }
    }

    @FXML
    void onCancelButtonClick(ActionEvent event) {
        MobileApplication.getInstance().switchToPreviousView();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        editUserInfoTab.showingProperty().addListener((obs, ov, nv) -> {
            if (nv) {
                final AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setTitleText("Edit User Info");
                appBar.getActionItems().addAll(mainAppBar.getActionItems());
            }
        });
    }

    public void onUserInfoReceive() {
        lastBioStr = "";
        lastDateOfBirth = LocalDate.parse("");
        lastFirstnameStr = "";
        lastLastnameStr = "";
        lastUserImage = null;
        lastPhoneNumberStr = "";

        phoneNumberText.setText(lastPhoneNumberStr);
        bioText.setText(lastBioStr);
        dateOfBirthText.setValue(lastDateOfBirth);
        firstnameText.setText(lastFirstnameStr);
        lastnameText.setText(lastLastnameStr);
        Arrays.equals(lastUserImage, userImage);


    }

}
