package ir.sharif.ap.presenter.listeners;

import ir.sharif.ap.presenter.events.NewTweetEvent;

public interface NewTweetListener {
    public void newTweetEventOccurred(NewTweetEvent e);
}
