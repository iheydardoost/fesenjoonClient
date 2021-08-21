package ir.sharif.ap.presenter;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.control.Snackbar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import ir.sharif.ap.Main;
import ir.sharif.ap.controller.MainController;
import ir.sharif.ap.controller.PacketHandler;
import ir.sharif.ap.model.*;
import ir.sharif.ap.presenter.events.EditMessageEvent;
import ir.sharif.ap.presenter.events.GetMessagesEvent;
import ir.sharif.ap.presenter.events.NewMessageEvent;
import ir.sharif.ap.presenter.listeners.EditMessageEventListener;
import ir.sharif.ap.presenter.listeners.GetMessagesEventListener;
import ir.sharif.ap.presenter.listeners.NewMessageEventListener;
import ir.sharif.ap.presenter.listeners.WantUpdateChatEventListener;
import javafx.collections.ObservableList;
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
import java.time.chrono.ChronoLocalDateTime;
import java.util.*;

import static ir.sharif.ap.Main.mainAppBar;
import static ir.sharif.ap.presenter.Styles.DEFAULT_BUTTON_STYLE;
import static ir.sharif.ap.presenter.Styles.RED_BUTTON_STYLE;

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
    private GetMessagesEventListener getMessagesEventListener;
    private NewMessageEventListener newMessageEventListener;
    private EditMessageEventListener editMessageEventListener;
    private WantUpdateChatEventListener wantUpdateChatEventListener;
    private boolean isEditing;
    private long editingMessageID;
    private Snackbar snackBar;

    public void addWantUpdateChatEventListener(WantUpdateChatEventListener wantUpdateChatEventListener) {
        this.wantUpdateChatEventListener = wantUpdateChatEventListener;
    }

    public void setEditingMessageID(long editingMessageID) {
        this.editingMessageID = editingMessageID;
    }

    public boolean isEditing() {
        return isEditing;
    }

    public void setEditing(boolean editing) {
        isEditing = editing;
        updateSendButton();

    }
    public void updateSendButton(){
        if (isEditing)
            sendButton.setStyle(RED_BUTTON_STYLE);
        else
            sendButton.setStyle(DEFAULT_BUTTON_STYLE);
    }

    public void addEditMessageEventListener(EditMessageEventListener editMessageEventListener) {
        this.editMessageEventListener = editMessageEventListener;
    }

    public void addNewMessageEventListener(NewMessageEventListener newMessageEventListener) {
        this.newMessageEventListener = newMessageEventListener;
    }

    public void addGetMessagesEventListener(GetMessagesEventListener getMessagesEventListener) {
        this.getMessagesEventListener = getMessagesEventListener;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        sendButton = MaterialDesignIcon.SEND.button(e -> {
            if (isEditing){
                EditMessageEvent event = new EditMessageEvent(editingMessageID,messageText.getText());
                editMessageEventListener.editMessageEventOccurred(event);
                setEditing(false);
            }else {
                NewMessageEvent newMessageEvent =new NewMessageEvent();
                newMessageEvent.setForwarded(false)
                        .setMsgText(messageText.getText())
                        .setMsgImage(messageImage)
                        .setMsgDateTime(LocalDateTime.now())
                        .addID(chatID)
                        .addCollectionItemType(CollectionItemType.CHAT);
                newMessageEventListener.newMessageEventOccurred(newMessageEvent);
            }

            resetImage();
            messageText.setText("");

        });

        attachButton = MaterialDesignIcon.ATTACH_FILE.button(e -> {
            onAddImageClick(e);
        });
        sendButton.setStyle(DEFAULT_BUTTON_STYLE);
        attachButton.setStyle(DEFAULT_BUTTON_STYLE);
        bottomBar.getChildren().addAll(sendButton, attachButton);

        messageListView.setCellFactory(e ->{
            ChatListCell chatListCell = new ChatListCell();
            chatListCell.addEditMessageEventListener(e2 ->{
                setEditing(true);
                setEditingMessageID(chatListCell.getMessageID());
                messageText.setText(chatListCell.getMessageText());
            });
            chatListCell.addDeleteMessageEventListener(e1 -> Main.getMainController().handleDeleteMessageEvent(e1));
            return chatListCell;
        });
        messageListView.setPlaceholder(new Label("There are no messages"));
        messageListView.setOnScroll(e -> {
            if(e.getDeltaY()<0){

                LocalDateTime lastMessageTime = null;
                if(messageListView.getItems().size()>0){
                    lastMessageTime = messageListView.getItems().get(messageListView.getItems().size()-1).getMsgDateTime();
                }
                if(previousLastMessageTime != null)
                    if(previousLastMessageTime.equals(lastMessageTime)){
                        return;
                    }
                previousLastMessageTime = lastMessageTime;
                getMessagesEventListener.getMessagesEventOccurred(new GetMessagesEvent(MainController.MAX_TWEET_LIST_REQUEST_NUMBER,
                        lastMessageTime, chatID));

            }

        });

        chatTab.showingProperty().addListener((obs, ov, nv) -> {
            if (nv) {
                final AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setTitleText("Chat");
                appBar.getActionItems().addAll(mainAppBar.getActionItems());
                chatID = Main.getChatID();
                messageListView.getItems().clear();
                getMessagesEventListener.getMessagesEventOccurred(new GetMessagesEvent(MainController.MAX_TWEET_LIST_REQUEST_NUMBER,
                         null, chatID));
            }
            wantUpdateChatEventListener.wantUpdateChatEventOccurred(nv);
        });

        snackBar = new Snackbar("");
    }

    public void onDeleteMessageReceive(String response){
        long msgID=Long.parseLong(response);
        ArrayList<Message> messageArrayList= new ArrayList<>(messageListView.getItems());
        for (Message m: messageArrayList) {
            if(m.getMsgID() == msgID){
                messageListView.getItems().remove(m);
                break;
            }
        }
    }

    public void onEditMessageReceive(String response){
        String[] args = response.split(",",-1);
        if(args[0].contains("success")) {
            long msgID=Long.parseLong(args[1]);
            ArrayList<Message> messageArrayList = new ArrayList<>(messageListView.getItems());
            for (Message m : messageArrayList) {
                if (m.getMsgID() == msgID) {
                    m.setMsgText(PacketHandler.getDecodedArg(args[2]));
                    messageListView.getItems().set(messageArrayList.indexOf(m), m);
                    break;
                }
            }
        }
        snackBar.setMessage(args[0]);
        snackBar.show();
    }

    public void onChangeMessageStatus(String response){
            String[] args = response.split(",", -1);
            long msgID = Long.parseLong(args[0]);
            ArrayList<Message> messageArrayList = new ArrayList<>(messageListView.getItems());
            for (Message m : messageArrayList) {
                if (m.getMsgID() == msgID) {
                    m.setMsgStatus(MessageStatus.valueOf(args[1]));
                    messageListView.getItems().set(messageArrayList.indexOf(m), m);
                    break;
                }
            }
    }

    public void onMessageReceive(String response){
            String[] args = response.split(",", -1);
            byte[] messageImage = null;
            if (!args[2].isEmpty())
                messageImage = Base64.getDecoder().decode(args[2]);
            Message message = new Message(PacketHandler.getDecodedArg(args[1]), LocalDateTime.parse(args[3]),
                    chatID, Long.parseLong(args[0]),
                    Boolean.parseBoolean(args[4]),
                    MessageStatus.valueOf(args[5]),
                    messageImage,
                    PacketHandler.getDecodedArg(args[7]) + " " + PacketHandler.getDecodedArg(args[8]),
                    Boolean.parseBoolean(args[6]));

            messageListView.getItems().add(message);
            messageListView.getItems().sort(new Comparator<Message>() {
                @Override
                public int compare(Message o1, Message o2) {
                    return o2.getMsgDateTime().compareTo(o1.getMsgDateTime());
                }
            });
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
