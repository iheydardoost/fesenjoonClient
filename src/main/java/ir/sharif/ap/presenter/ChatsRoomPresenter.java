package ir.sharif.ap.presenter;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import ir.sharif.ap.controller.PacketHandler;
import ir.sharif.ap.model.ChatItem;
import ir.sharif.ap.presenter.listeners.GetChatroomListEventListener;
import ir.sharif.ap.presenter.listeners.WantUpdateChatroomEventListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import static ir.sharif.ap.Main.mainAppBar;

public class ChatsRoomPresenter implements Initializable {

    @FXML
    private View chatsRoomTab;

    @FXML
    private ListView<ChatItem> chatsRoomListView;
    private GetChatroomListEventListener getChatroomListEventListener;
    private WantUpdateChatroomEventListener wantUpdateChatroomEventListener;

    public void addWantUpdateChatroomEventListener(WantUpdateChatroomEventListener wantUpdateChatroomEventListener) {
        this.wantUpdateChatroomEventListener = wantUpdateChatroomEventListener;
    }

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
            wantUpdateChatroomEventListener.wantUpdateChatroomEventOccurred(nv);
        });
    }

    public void onChatItemReceive(String response){
        String[] args = response.split(",", -1);

        ChatItem chatItem = new ChatItem();
        chatItem.setChatID(Long.parseLong(args[0]));
        chatItem.setChatName(PacketHandler.getDecodedArg(args[1]));
        chatItem.setUnReadMessages(Integer.parseInt(args[2]));
        chatsRoomListView.getItems().add(chatItem);
        chatsRoomListView.getItems().sort(new Comparator<ChatItem>() {
            @Override
            public int compare(ChatItem o1, ChatItem o2) {
                return (o2.getUnReadMessages()-o1.getUnReadMessages());
            }
        });
    }

    public void onRefreshList(String response){
        chatsRoomListView.getItems().clear();
        getChatroomListEventListener.getChatroomListEventOccurred(true);
    }

}
