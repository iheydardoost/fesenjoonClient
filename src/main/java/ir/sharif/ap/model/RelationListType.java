package ir.sharif.ap.model;

public enum RelationListType {
    BLACKLIST,
    FOLLOWER_LIST,
    FOLLOWING_LIST;
    @Override
    public String toString() {
        String name = "";
        switch (ordinal()) {
            case 0:
                name = "Black List";
                break;
            case 1:
                name = "Follower List";
                break;
            case 2:
                name = "Following List";
                break;
            default:
                name = "";
                break;
        }
        return name;
    }
}
