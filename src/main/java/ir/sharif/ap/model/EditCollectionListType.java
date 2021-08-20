package ir.sharif.ap.model;

public enum EditCollectionListType {
    FORWARD_LIST,
    NEW_MESSAGE,
    EDIT_FOLDER,
    EDIT_CHAT;
    @Override
    public String toString() {
        String name = "";
        switch (ordinal()) {
            case 0:
                name = "Forward to ...";
                break;
            case 1:
                name = "New Message";
                break;
            case 2:
                name = "Edit Folder";
                break;
            case 3:
                name = "Edit Group";
                break;
            default:
                name = "";
                break;
        }
        return name;
    }
}
