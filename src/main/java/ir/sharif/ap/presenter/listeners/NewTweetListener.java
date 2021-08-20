package ir.sharif.ap.presenter.listeners;

import ir.sharif.ap.presenter.NewTweetEvent;

public interface NewTweetListener {
    public void newTweetEventOccurred(NewTweetEvent e);
}
