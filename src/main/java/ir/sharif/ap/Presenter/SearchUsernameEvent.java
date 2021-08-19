package ir.sharif.ap.Presenter;

public class SearchUsernameEvent {
    private boolean isExplorer;
    private String userName;

    public SearchUsernameEvent(boolean isExplorer, String userName) {
        this.isExplorer = isExplorer;
        this.userName = userName;
    }

    public boolean isExplorer() {
        return isExplorer;
    }

    public void setExplorer(boolean explorer) {
        isExplorer = explorer;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
