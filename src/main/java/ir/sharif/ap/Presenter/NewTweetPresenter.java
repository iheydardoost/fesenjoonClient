package ir.sharif.ap.Presenter;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.control.Snackbar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import ir.sharif.ap.Main;
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
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.ResourceBundle;

import static ir.sharif.ap.Main.mainAppBar;

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

    private long parentTweetID;

    private byte[] tweetImage;

    private NewTweetListener newTweetListener;
    private Snackbar snackbar;

    public void addNewTweetListener(NewTweetListener newTweetListener) {
        this.newTweetListener = newTweetListener;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        snackbar = new Snackbar("");
        parentTweetID = 0;
        newTweetTab.showingProperty().addListener((obs, ov, nv) -> {
            if (nv) {
                final AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setTitleText("New Tweet");
                appBar.getActionItems().addAll(mainAppBar.getActionItems());
            }
        });
    }

    public void setParentTweetID(long parentTweetID) {
        this.parentTweetID = parentTweetID;
    }

    @FXML
    void onImagePathChange(ActionEvent event) {
        tweetImage = null;
    }

    @FXML
    void onAddImageClick(ActionEvent event) {
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
            tweetImage = Arrays.copyOf(fileContent,fileContent.length);

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
        MobileApplication.getInstance().switchToPreviousView();
    }

    @FXML
    void onTweetButtonClick(ActionEvent event) {

        NewTweetEvent e =
                new NewTweetEvent(
                        tweetTxt.getText(),
                        LocalDateTime.now(),
                        parentTweetID,
                        false,
                        tweetImage
                );

        tweetImagePath.setText("");
        tweetImage=null;

        newTweetListener.newTweetEventOccurred(e);
    }

    public void onNewTweetResultRecive(String result){
        String args[] = result.split(",", -1);
        snackbar.setMessage(args[0]);
        snackbar.show();
        if(args[0].equals("success")){
            MobileApplication.getInstance().switchToPreviousView();
        }
    }
}
