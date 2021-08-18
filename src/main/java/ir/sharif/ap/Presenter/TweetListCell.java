package ir.sharif.ap.Presenter;

import ir.sharif.ap.View.TweetTileView;
import ir.sharif.ap.model.TweetTile;
import javafx.scene.control.ListCell;


public class TweetListCell extends ListCell<TweetTile> {

    private final TweetTileView tweetTileView = new TweetTileView();
    private final TweetTilePresenter tweetTilePresenter = (TweetTilePresenter)tweetTileView.getPresenter();;
    private TweetTile tweet;

    public void addNewTweetListener(NewTweetListener newTweetListener) {
        tweetTilePresenter.addNewTweetListener(newTweetListener);
    }

    public void addActionTweetEventListener(ActionTweetEventListener actionTweetEventListener) {
        tweetTilePresenter.addActionTweetEventListener(actionTweetEventListener);
    }

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