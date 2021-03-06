package ir.sharif.ap.presenter;

import ir.sharif.ap.view.ChatTileView;
import ir.sharif.ap.model.Message;
import ir.sharif.ap.presenter.listeners.DeleteMessageEventListener;
import ir.sharif.ap.presenter.listeners.EditMessageEventListener;
import javafx.scene.control.ListCell;


public class ChatListCell extends ListCell<Message> {
    private final ChatTileView chatTileView = new ChatTileView();
    private final ChatTilePresenter chatTilePresenter = (ChatTilePresenter)chatTileView.getPresenter();;
    private Message message;

    public void addDeleteMessageEventListener(DeleteMessageEventListener deleteMessageEventListener) {
        chatTilePresenter.addDeleteMessageEventListener(deleteMessageEventListener);
    }

    public void addEditMessageEventListener(EditMessageEventListener editMessageEventListener) {
        chatTilePresenter.addEditMessageEventListener(editMessageEventListener);
    }

    public String getMessageText(){
        return message.getMsgText();
    }

    public long getMessageID(){ return message.getMsgID();}

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