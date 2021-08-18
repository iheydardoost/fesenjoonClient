package ir.sharif.ap.Presenter;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class NewTweetPresenter implements Initializable {
    @FXML
    private View newTweetTab;

    @FXML
    private TextArea tweetTxt;

    @FXML
    private TextField tweetImagePath;

    @FXML
    private Button addImageButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Button tweetButton;

    private int parentTweetID;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        parentTweetID = -1;
        newTweetTab.showingProperty().addListener((obs, ov, nv) -> {
            if (nv) {
                final AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setTitleText("New Tweet");
                appBar.getActionItems().addAll(
                        MaterialDesignIcon.ARROW_BACK.button(e->MobileApplication.getInstance().switchToPreviousView()),
                        MaterialDesignIcon.POWER_SETTINGS_NEW.button(e-> Platform.exit()));

            }
        });
    }

    public void setParentTweetID(int parentTweetID) {
        this.parentTweetID = parentTweetID;
    }

    @FXML
    void onAddImageClick(ActionEvent event) {
        System.out.println("Image clicked");
        FileChooser fileChooser = new FileChooser();
        configureFileChooser(fileChooser);
        File file = fileChooser.showOpenDialog(null);
        if(file == null){
            return;
        }
        tweetImagePath.setText(file.toPath().toString());

        FileInputStream fin = null;
        try {
            fin = new FileInputStream(file);
            byte fileContent[] = new byte[(int)file.length()];
            fin.read(fileContent);
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
        //MobileApplication.getInstance().switchToPreviousView();
    }

    @FXML
    void onTweetButtonClick(ActionEvent event) {
        //MobileApplication.getInstance().switchToPreviousView();
    }
}
