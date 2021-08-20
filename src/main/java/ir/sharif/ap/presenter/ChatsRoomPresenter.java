package ir.sharif.ap.presenter;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import ir.sharif.ap.model.ChatItem;
import ir.sharif.ap.presenter.listeners.GetChatroomListEventListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.ResourceBundle;

import static ir.sharif.ap.Main.mainAppBar;

public class ChatsRoomPresenter implements Initializable {

    @FXML
    private View chatsRoomTab;

    @FXML
    private ListView<ChatItem> chatsRoomListView;
    private GetChatroomListEventListener getChatroomListEventListener;


    public void addGetChatroomListEventListener(GetChatroomListEventListener getChatroomListEventListener) {
        this.getChatroomListEventListener = getChatroomListEventListener;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        chatsRoomListView.setCellFactory(chatItemListView -> {
            ChatsRoomListCell chatsRoomListCell = new ChatsRoomListCell();
            return chatsRoomListCell;
        });
        chatsRoomListView.setPlaceholder(new Label("There are no chats"));
        chatsRoomTab.showingProperty().addListener((obs, ov, nv) -> {
            if (nv) {
                final AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setTitleText("Chats room");
                appBar.getActionItems().addAll(mainAppBar.getActionItems());
                chatsRoomListView.getItems().clear();
                getChatroomListEventListener.getChatroomListEventOccurred(true);
            }
        });
    }

    public void onChatItemReceive(String respose){
        String[] args = respose.split(",", -1);

        ChatItem chatItem = new ChatItem();
        chatItem.setChatID(Long.parseLong(args[0]));
        chatItem.setChatName(args[1]);
        chatItem.setUnReadMessages(Integer.parseInt(args[2]));
        chatsRoomListView.getItems().add(chatItem);
    }

}
