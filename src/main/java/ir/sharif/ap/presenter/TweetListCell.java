package ir.sharif.ap.presenter;

import ir.sharif.ap.view.TweetTileView;
import ir.sharif.ap.model.TweetTile;
import ir.sharif.ap.presenter.listeners.ActionTweetEventListener;
import ir.sharif.ap.presenter.listeners.NewTweetListener;
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