package ir.sharif.ap.Presenter;

import ir.sharif.ap.View.ChatTileView;
import ir.sharif.ap.View.TweetTileView;
import ir.sharif.ap.model.Message;
import ir.sharif.ap.model.TweetTile;
import javafx.scene.control.ListCell;


public class ChatListCell extends ListCell<Message> {

    private final ChatTileView chatTileView = new ChatTileView();
    private final ChatTilePresenter chatTilePresenter = (ChatTilePresenter)chatTileView.getPresenter();;
    private Message message;

    @Override
    protected void updateItem(Message item, boolean empty) {
        super.updateItem(item, empty);
        message = item;
        if (!empty && item != null) {
            chatTilePresenter.setMessage(message);
            setGraphic(chatTileView.getView());

        } else {
            setGraphic(null);
        }
    }
}