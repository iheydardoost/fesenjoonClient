package ir.sharif.ap.Presenter;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import ir.sharif.ap.Main;
import ir.sharif.ap.controller.LogHandler;
import ir.sharif.ap.controller.MainController;
import ir.sharif.ap.model.Message;
import ir.sharif.ap.model.MessageStatus;
import ir.sharif.ap.model.TweetListType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
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
import static ir.sharif.ap.Presenter.Styles.defaultButtonStyle;

public class ChatPresenter implements Initializable {

    @FXML
    private View chatTab;

    private long chatID;
    @FXML
    private ListView<Message> messageListView;

    @FXML
    private HBox bottomBar;
    byte[] messageImage = null;

    @FXML
    private TextField messageText;
    private Button sendButton, attachButton;
    static LocalDateTime previousLastMessageTime = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        sendButton = MaterialDesignIcon.SEND.button(e -> {

            System.out.println("SEND");

            resetImage();
            messageText.setText("");

        });
        attachButton = MaterialDesignIcon.ATTACH_FILE.button(e -> {
            onAddImageClick(e);
        });
        sendButton.setStyle(defaultButtonStyle);
        attachButton.setStyle(defaultButtonStyle);
        bottomBar.getChildren().addAll(sendButton, attachButton);

        messageListView.setCellFactory(e ->{
            ChatListCell chatListCell = new ChatListCell();
            return chatListCell;
        });
        messageListView.setPlaceholder(new Label("There are no messages"));
        messageListView.setOnScroll(e -> {
            if(e.getDeltaY()<0){

                LocalDateTime lastMessageTime = null;
                if(messageListView.getItems().size()>1){
                    lastMessageTime = messageListView.getItems().get(messageListView.getItems().size()-1).getMsgDateTime();
                }
                if(previousLastMessageTime != null)
                    if(previousLastMessageTime.equals(lastMessageTime)){
                        return;
                    }
                previousLastMessageTime = lastMessageTime;

            }

        });

        chatTab.showingProperty().addListener((obs, ov, nv) -> {
            if (nv) {
                final AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setTitleText("Chat");
                appBar.getActionItems().addAll(mainAppBar.getActionItems());
                chatID = Main.getChatID();
                messageListView.getItems().clear();
                onMessageReceive("");
            }
        });
    }

    public void onMessageReceive(String response){
        Message message1 = new Message("First Message", LocalDateTime.now(),
            1, 1, 1,
            true, MessageStatus.SENDING, null, "mamad Agha",true);
        Message message2 = new Message("Second", LocalDateTime.now(),
                1, 1, 2,
                true, MessageStatus.SEEN, null, "javad Agha",false);
        Message message3 = new Message("ThirdMessage", LocalDateTime.now(),
                1, 1, 3,
                true, MessageStatus.RECEIVED, null, "mamad Agha",true);

        Message message4 = new Message("Forth message", LocalDateTime.now(),
                1, 1, 4,
                true, MessageStatus.SENT, null, "javad Agha",false);

        messageListView.getItems().add(message1);
        messageListView.getItems().add(message2);
        messageListView.getItems().add(message3);
        messageListView.getItems().add(message4);
    }

    void resetImage(){
        messageImage = null;
    }

    @FXML
    void onAddImageClick(ActionEvent event) {
        resetImage();
        FileChooser fileChooser = new FileChooser();
        configureFileChooser(fileChooser);
        File file = fileChooser.showOpenDialog(null);
        if(file == null){
            return;
        }

        FileInputStream fin = null;
        try {
            fin = new FileInputStream(file);
            byte fileContent[] = new byte[(int)file.length()];
            fin.read(fileContent);
            messageImage = Arrays.copyOf(fileContent,fileContent.length);
        }
        catch (FileNotFoundException e) {
        }
        catch (IOException ioe) {
        }
        finally {
            // close the streams using close method
            try {
                if (fin != null) {
                    fin.close();
                }
            }
            catch (IOException ioe) {
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
}
