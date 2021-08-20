package ir.sharif.ap.presenter;

import com.gluonhq.charm.glisten.control.Icon;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import ir.sharif.ap.model.Message;
import ir.sharif.ap.model.MessageStatus;
import ir.sharif.ap.presenter.listeners.DeleteMessageEventListener;
import ir.sharif.ap.presenter.listeners.EditMessageEventListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

import static ir.sharif.ap.presenter.Styles.*;


public class ChatTilePresenter implements Initializable {

    @FXML
    private Label messageFullName;

    @FXML
    private Text messageText;

    @FXML
    private ImageView messageImage;

    private Message message;

    @FXML
    private BorderPane messagePane;

    @FXML
    private HBox messageDateBox;

    @FXML
    private Label messageDateText;

    @FXML
    private VBox statusBox;

    @FXML
    private Icon messageStatusIcon;

    @FXML
    private HBox actionBox;
    private Button deleteMessageButton, editMessageButton;

    private DeleteMessageEventListener deleteMessageEventListener;
    private EditMessageEventListener editMessageEventListener;

    public void addDeleteMessageEventListener(DeleteMessageEventListener deleteMessageEventListener) {
        this.deleteMessageEventListener = deleteMessageEventListener;
    }

    public void addEditMessageEventListener(EditMessageEventListener editMessageEventListener) {
        this.editMessageEventListener = editMessageEventListener;
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        messageImage.setManaged(false);
        messageImage.setVisible(false);

        editMessageButton = MaterialDesignIcon.EDIT.button(e->{
            editMessageEventListener.editMessageEventOccurred(new EditMessageEvent(0,""));
        });
        deleteMessageButton = MaterialDesignIcon.DELETE.button(e->{
            deleteMessageEventListener.deleteMessageEventOccurred(message.getMsgID());
        });

        editMessageButton.setStyle(defaultButtonStyle);

        deleteMessageButton.setStyle(defaultButtonStyle);

        actionBox.getChildren().addAll(editMessageButton, deleteMessageButton);

    }

    public void setMessageFullName(String imageUsername) {
        this.messageFullName.setText(imageUsername);
    }

    public void setMessageText(String messageText) {
        this.messageText.setText(messageText);
    }

    public void setMessageDate(LocalDateTime messageDate){
        if(messageDate!=null)
            messageDateText.setText(messageDate.toString());
    }
    public void setMessageStatus(MessageStatus messageStatus){
        switch (messageStatus){
            case SENDING:
                messageStatusIcon.setContent(MaterialDesignIcon.FILE_UPLOAD);
                break;
            case SENT:
                messageStatusIcon.setContent(MaterialDesignIcon.DONE);
                break;
            case RECEIVED:
                messageStatusIcon.setContent(MaterialDesignIcon.DONE_ALL);
                break;
            case SEEN:
                messageStatusIcon.setContent(MaterialDesignIcon.VISIBILITY);
                break;

        }
    }

    public void setMessageImage(Image messageImage) {
        if(messageImage != null){
            this.messageImage.setManaged(true);
            this.messageImage.setVisible(true);
            this.messageImage.setImage(messageImage);
        }else{
            this.messageImage.setManaged(false);
            this.messageImage.setVisible(false);
        }
    }

    public void setBackgroundStyle(boolean isOwner){
        if(isOwner){
            this.messagePane.setStyle(chatMessageOwnerStyle);
        }else {
            this.messagePane.setStyle(lightBackgroundStyle);
        }
    }

    public void setMessage(Message message){
        this.message = message;
        setMessageText(message.getMsgText());

        Image messageImage = null;
        if(message.getMsgImage() != null){
            ByteArrayInputStream bis = new ByteArrayInputStream(message.getMsgImage());
            messageImage = new Image(bis);
            try {
                bis.close();
            } catch (IOException e) {
                //e.printStackTrace();
            }
        }
        setMessageImage(messageImage);
        setMessageFullName(message.getUserFullName());
        setMessageDate(message.getMsgDateTime());
        setMessageStatus(message.getMsgStatus());
        setBackgroundStyle(message.isOwner());

    }

}
