package ir.sharif.ap.model;

public enum CollectionListType {
    FOLDER,
    GROUP;
    @Override
    public String toString() {
        String name = "";
        switch (ordinal()) {
            case 0:
                name = "Folder List";
                break;
            case 1:
                name = "Group List";
                break;
            default:
                name = "";
                break;
        }
        return name;
    }
}
