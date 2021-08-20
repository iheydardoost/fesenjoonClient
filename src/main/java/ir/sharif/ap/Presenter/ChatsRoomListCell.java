package ir.sharif.ap.Presenter;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.ListTile;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import ir.sharif.ap.Main;
import ir.sharif.ap.model.ChatItem;
import ir.sharif.ap.model.CollectionItem;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

import static ir.sharif.ap.Main.CHAT_VIEW;
import static ir.sharif.ap.Presenter.Styles.defaultButtonStyle;

public class ChatsRoomListCell extends ListCell<ChatItem> {

    private final ListTile tile = new ListTile();
    private ChatItem chatItem;
    private Button visitChatButton;
    private StackPane unReadBadge;
    private Label unReadText;

    {
        visitChatButton = MaterialDesignIcon.CHAT.button(actionEvent -> {
            Main.setChatID(chatItem.getChatID());
            MobileApplication.getInstance().switchView(CHAT_VIEW);
        });
        visitChatButton.setStyle(defaultButtonStyle);
        tile.setSecondaryGraphic(new HBox(visitChatButton));
        Circle circle = new Circle();
        circle.setRadius(15);
        circle.setStyle("-fx-fill: -alternate-swatch-400;");
        unReadText=new Label("");
        unReadText.setStyle("-fx-font-size:12;");
        unReadBadge = new StackPane(circle, unReadText);
        tile.setPrimaryGraphic(unReadBadge);

    }

    @Override
    protected void updateItem(ChatItem item, boolean empty) {
        super.updateItem(item, empty);
        chatItem = item;
        if (!empty && item != null) {
            tile.textProperty().setAll(item.getChatName());
            if(item.getUnReadMessages()>0){
                unReadBadge.setManaged(true);
                unReadBadge.setVisible(true);
                unReadText.setText(String.valueOf(item.getUnReadMessages()));
            }else{
                unReadBadge.setManaged(false);
                unReadBadge.setVisible(false);

            }
            setGraphic(tile);
        } else {
            setGraphic(null);
        }
    }
}
