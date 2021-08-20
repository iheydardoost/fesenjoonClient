package ir.sharif.ap.Presenter;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.application.ViewStackPolicy;
import com.gluonhq.charm.glisten.control.Alert;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.control.Snackbar;
import com.gluonhq.charm.glisten.mvc.View;
import ir.sharif.ap.Main;
import ir.sharif.ap.model.CollectionItem;
import ir.sharif.ap.model.EditCollectionListType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.ResourceBundle;

import static ir.sharif.ap.Main.COLLECTION_EDIT_VIEW;
import static ir.sharif.ap.Main.mainAppBar;

public class NewMessagePresenter implements Initializable {
    @FXML
    private View newMessageTab;

    @FXML
    private TextArea messageTxt;

    @FXML
    private TextField messageImagePath;

    @FXML
    private Button addImageButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Button messageButton;

    private byte[] messageImage;

    private Snackbar snackbar;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        snackbar = new Snackbar("");
        newMessageTab.showingProperty().addListener((obs, ov, nv) -> {
            if (nv) {
                final AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setTitleText("New Message");
                appBar.getActionItems().addAll(mainAppBar.getActionItems());
            }
        });
    }

    @FXML
    void onImagePathChange(ActionEvent event) {
        messageImage = null;
    }

    public void resetToDefault(){
        messageImage = null;
        messageImagePath.setText("");
        messageTxt.setText("");
    }

    @FXML
    void onAddImageClick(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        configureFileChooser(fileChooser);
        File file = fileChooser.showOpenDialog(null);
        if(file == null){
            return;
        }
        messageImagePath.setText(file.toPath().toString());

        FileInputStream fin = null;
        try {
            fin = new FileInputStream(file);
            byte fileContent[] = new byte[(int)file.length()];
            fin.read(fileContent);
            messageImage = Arrays.copyOf(fileContent,fileContent.length);

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
    void onCancelButtonClick(ActionEvent event) {
        Main.clearMessageReceiverList();
        resetToDefault();
        MobileApplication.getInstance().switchToPreviousView();
    }

    @FXML
    void onSelectReceiverClick(ActionEvent event) {
        Main.clearMessageReceiverList();
        Main.setEditCollectionListType(EditCollectionListType.NEW_MESSAGE);
        Main.setTargetCollectionID(0);
        MobileApplication.getInstance().switchView(COLLECTION_EDIT_VIEW);
    }

    @FXML
    void onSendButtonClick(ActionEvent event) {
        ArrayList<CollectionItem> collectionItems = Main.getMessageReceiverList();
        if(collectionItems == null || collectionItems.size()<1){
            Alert alert = new Alert(javafx.scene.control.Alert.AlertType.ERROR);
            alert.setTitleText("Error");
            alert.setContentText("Please select message receiver.");
            alert.showAndWait();
            return;
        }
        System.out.println(collectionItems.size());
        resetToDefault();
        Main.clearMessageReceiverList();
    }

    public void onNewMessageResultReceive(String result){
        String args[] = result.split(",", -1);
        snackbar.setMessage(args[0]);
        snackbar.show();
        if(args[0].equals("success")){
            MobileApplication.getInstance().switchToPreviousView();
        }
    }
}
