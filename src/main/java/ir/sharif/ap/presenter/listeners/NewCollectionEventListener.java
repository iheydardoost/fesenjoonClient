package ir.sharif.ap.presenter.listeners;

import ir.sharif.ap.presenter.events.NewCollectionEvent;

public interface NewCollectionEventListener {
    public void newCollectionEventOccurred(NewCollectionEvent e);
}
