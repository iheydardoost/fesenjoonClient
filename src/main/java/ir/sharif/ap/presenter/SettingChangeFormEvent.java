package ir.sharif.ap.presenter;

import ir.sharif.ap.model.LastSeenStatus;

public class SettingChangeFormEvent {
    private LastSeenStatus lastSeenStatus;
    private boolean accountPrivate;
    private boolean accountActive;
    private String password;

    public SettingChangeFormEvent() {
        this.lastSeenStatus = LastSeenStatus.EVERYONE;
        this.accountPrivate = false;
        this.accountActive = true;
        this.password = null;
    }

    public LastSeenStatus getLastSeenStatus() {
        return lastSeenStatus;
    }

    public SettingChangeFormEvent setLastSeenStatus(LastSeenStatus lastSeenStatus) {
        this.lastSeenStatus = lastSeenStatus;
        return this;
    }

    public boolean isAccountPrivate() {
        return accountPrivate;
    }

    public SettingChangeFormEvent setAccountPrivate(boolean accountPrivate) {
        this.accountPrivate = accountPrivate;
        return this;
    }

    public boolean isAccountActive() {
        return accountActive;
    }

    public SettingChangeFormEvent setAccountActive(boolean accountActive) {
        this.accountActive = accountActive;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public SettingChangeFormEvent setPassword(String password) {
        this.password = password;
        return this;
    }
}
