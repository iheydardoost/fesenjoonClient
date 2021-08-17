package ir.sharif.ap.Presenter;

import com.gluonhq.charm.glisten.control.Avatar;
import com.gluonhq.charm.glisten.control.ListTile;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import ir.sharif.ap.Main;
import ir.sharif.ap.View.TweetTileView;
import ir.sharif.ap.model.TweetTile;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;


public class TweetListCell extends ListCell<TweetTile> {

    private final TweetTileView tweetTileView = new TweetTileView();
    private final TweetTilePresenter tweetTilePresenter = (TweetTilePresenter)tweetTileView.getPresenter();;
    private TweetTile tweet;


    @Override
    protected void updateItem(TweetTile item, boolean empty) {
        super.updateItem(item, empty);
        tweet = item;
        if (!empty && item != null) {
            tweetTilePresenter.setTweet(tweet);
            setGraphic(tweetTileView.getView());

        } else {
            setGraphic(null);
        }
    }
}