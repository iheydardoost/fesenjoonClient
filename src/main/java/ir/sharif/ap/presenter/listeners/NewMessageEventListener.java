package ir.sharif.ap.presenter.listeners;

import ir.sharif.ap.presenter.events.NewMessageEvent;

public interface NewMessageEventListener {
    public void newMessageEventOccurred(NewMessageEvent e);
}
