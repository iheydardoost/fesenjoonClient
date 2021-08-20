package ir.sharif.ap.controller;

import ir.sharif.ap.Main;
import ir.sharif.ap.presenter.*;
import ir.sharif.ap.model.*;
import javafx.application.Platform;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Base64;

public class MainController implements Runnable{
    private LoopHandler loopHandler;
    private SocketController socketController;
    private ConfigLoader configLoader;
    public static final int MAX_TWEET_LIST_REQUEST_NUMBER = 10;

    public MainController() {
        this.socketController = new SocketController();
        this.configLoader = new ConfigLoader();
        this.loopHandler = new LoopHandler(180,this);
    }

    @Override
    public void run() {
        Packet response = socketController.getResponse();
        if(response==null) return;
        if(response.isAuthTokenAvailable()) socketController.setAuthToken(response.getAuthToken());

        switch (response.getPacketType()){
            case LOG_IN_RES:
                Platform.runLater(() -> Main.getLoginPresenter().onAuthReceive(response.getBody()));
                break;
            case SIGN_IP_RES:
                Platform.runLater(() -> Main.getLoginPresenter().onSignupReceive(response.getBody()));
                break;
            case CHANGE_SETTING_RES:
                Platform.runLater(() -> Main.getSettingPresenter().onChangeResponse(response.getBody()));
                break;
            case DELETE_USER_RES:
                Platform.runLater(() -> Main.getSettingPresenter().onUserDeleteResponse(response.getBody()));
                break;
            case LOG_OUT_RES:
//                Platform.runLater(() -> Main.getSettingPresenter().onLogoutResponse(response.getBody()));
                Platform.runLater(() -> Main.onLogoutResponse(response.getBody()));
                break;
            case SETTING_INFO_RES:
                Platform.runLater(() -> Main.getSettingPresenter().onSettingInfoReceive(response.getBody()));
                break;
            case TIMELINE_TWEET_RES:
                Platform.runLater(() -> Main.getTimeLinePresenter().onTweetReceive(response.getBody()));
                break;
            case EXPLORER_TWEET_RES:
                Platform.runLater(() -> Main.getExplorePresenter().onTweetReceive(response.getBody()));
                break;
            case NEW_TWEET_RES:
                Platform.runLater(() -> Main.getNewTweetPresenter().onNewTweetResultRecive(response.getBody()));
                break;
            case GET_TWEET_LIST_RES:
                Platform.runLater(()->Main.getMyTweetListPresenter().onTweetReceive(response.getBody()));
                break;
            case GET_COMMENTS_RES:
                Platform.runLater(()->Main.getCurrentTweetDetailPresenter().onTweetReceive(false, response.getBody()));
                break;
            case GET_TWEET_RES:
                Platform.runLater(()->Main.getCurrentTweetDetailPresenter().onTweetReceive(true, response.getBody()));
                break;
            case GET_BLACK_LIST_RES:
            case GET_FOLLOWINGS_LIST_RES:
            case GET_FOLLOWERS_LIST_RES:
                Platform.runLater(() -> Main.getRelationListPresenter().onRelationListReceive(response.getBody()));
                break;
            case GET_USER_INFO_RES:
                Platform.runLater(() -> Main.getUserInfoPresenter().onInfoReceive(response.getBody()));
                break;
            case GET_PRIVATE_INFO_RES:
                Platform.runLater(() -> Main.getMyInfoPresenter().onInfoReceive(response.getBody()));

                break;
            case GET_EDIT_INFO_RES:
                Platform.runLater(() -> Main.getEditUserInfoPresenter().onUserInfoReceive(response.getBody()));
                break;

            case FOLLOW_USER_RES:
            case UNFOLLOW_USER_RES:
            case REPORT_USER_RES:
                Platform.runLater(() -> Main.getUserInfoPresenter().onResponseReceive(response.getBody()));
                break;
            case UNMUTE_USER_RES:
            case MUTE_USER_RES:
                Platform.runLater(()->Main.getCurrentTweetDetailPresenter().onMuteResponse(response.getBody()));
                break;
            case SEARCH_USERNAME_EXPLORER_RES:
                Platform.runLater(()->Main.getExplorePresenter().onSearchResultReceive(response.getBody()));
                break;
            case SEARCH_USERNAME_LIST_RES:
                Platform.runLater(()->Main.getRelationListPresenter().onSearchResultReceive(response.getBody()));
                break;
            case GET_NOTIFICATIONS_RES:
            case GET_PENDING_FOLLOW_RES:
                Platform.runLater(()->Main.getNotificationsPresenter().onNotificationReceive(response.getBody()));
                break;
            case GET_CHATROOM_LIST_RES:
                Platform.runLater(()->Main.getChatsRoomPresenter().onChatItemReceive(response.getBody()));
                break;
            case GET_FOLDER_LIST_RES:
            case GET_GROUP_LIST_RES:
                Platform.runLater(()->Main.getManageCollectionPresenter().onCollectionReceive(response.getBody()));
                break;

            case GET_MESSAGES_RES:
                Platform.runLater(()->Main.getChatPresenter().onMessageReceive(response.getBody()));

                break;
            case GET_SELECT_LIST_RES:
                Platform.runLater(()->Main.getCollectionEditPresenter().onSelectCollectionReceive(response.getBody()));
                break;
            case GET_EDIT_GROUP_LIST_RES:
            case GET_EDIT_FOLDER_LIST_RES:
                Platform.runLater(()->Main.getCollectionEditPresenter().onEditCollectionReceive(response.getBody()));

                break;
            case DELETE_MESSAGE_RES:
                Platform.runLater(()->Main.getChatPresenter().onDeleteMessageReceive(response.getBody()));
                break;
            case SET_EDIT_FOLDER_LIST_RES:
            case SET_EDIT_GROUP_LIST_RES:
            case DELETE_FOLDER_RES:
            case DELETE_GROUP_RES:
                Platform.runLater(()->Main.getCollectionEditPresenter().onResponseReceive(response.getBody()));
                break;
            case NEW_MESSAGE_RES:
                Platform.runLater(()-> {
                    if (Main.getNewMessagePresenter() != null);
                        //Main.getNewMessagePresenter().onNewMessageResultReceive(response.getBody());
                        }
                );
                break;
            case EDIT_MESSAGE_RES:
                Platform.runLater(()->Main.getChatPresenter().onEditMessageReceive(response.getBody()));
                break;

//            case REPORT_TWEET_RES:
//                break;
//            case LIKE_TWEET_RES:
//                break;
//            case REPORT_USER_RES:
//                break;
//            case MUTE_USER_RES:
//                break;
//            case BLOCK_USER_RES:
//                break;
            default:
                break;
        }
    }

    public void handleAuthEvent(AuthFormEvent e){
        Packet request = null;
        if(e.isLoginReq()){
           request = new Packet(
                   PacketType.LOG_IN_REQ,
                   e.getUserName() + "," + e.getPassword(),
                   0,
                   false);
        }
        else{
            request = new Packet(
                    PacketType.SIGN_UP_REQ,
                    e.getFirstName() + ","
                            + e.getLastName() + ","
                            + e.getUserName() + ","
                            + e.getPassword() + ","
                            + e.getDateOfBirth() + ","
                            + e.getEmail() + ","
                            + e.getPhoneNumber() + ","
                            + e.getBio(),
                    0,
                    false);
        }
        socketController.addRequest(request);
    }

    public void handleLogoutEvent(){
        socketController.addRequest(
                new Packet(
                        PacketType.LOG_OUT_REQ,
                        "",
                        socketController.getAuthToken(),
                        true));
    }

    public void handleUserDeleteEvent(){
        socketController.addRequest(
                new Packet(
                        PacketType.DELETE_USER_REQ,
                        "",
                        socketController.getAuthToken(),
                        true));
    }

    public void handleSettingInfoEvent(){
        socketController.addRequest(
                new Packet(
                        PacketType.SETTING_INFO_REQ,
                        "",
                        socketController.getAuthToken(),
                        true));

    }

    public void handleSettingChangeEvent(SettingChangeFormEvent e){
        socketController.addRequest(
                new Packet(
                        PacketType.CHANGE_SETTING_REQ,
                        "lastSeenStatus," + e.getLastSeenStatus(),
                        socketController.getAuthToken(),
                        true));
        socketController.addRequest(
                new Packet(
                        PacketType.CHANGE_SETTING_REQ,
                        "accountPrivate," + e.isAccountPrivate(),
                        socketController.getAuthToken(),
                        true));
        socketController.addRequest(
                new Packet(
                        PacketType.CHANGE_SETTING_REQ,
                        "accountActive," + e.isAccountActive(),
                        socketController.getAuthToken(),
                        true));
        if(!e.getPassword().isEmpty()) {
            socketController.addRequest(
                    new Packet(
                            PacketType.CHANGE_SETTING_REQ,
                            "password," + e.getPassword(),
                            socketController.getAuthToken(),
                            true));
        }
    }

    public void handleListTweetEvent(ListTweetEvent e){
        LocalDateTime lastTweetDateTime = e.getLastTweetDateTime();
        String dateTimeStr = "";
        if(lastTweetDateTime!=null)
            dateTimeStr = lastTweetDateTime.toString();

        String body = "";
        PacketType packetType = null;
        if(e.getTweetListType()==TweetListType.TIMELINE) {
            packetType = PacketType.TIMELINE_TWEET_REQ;
            body = e.getMaxNum() + "," + dateTimeStr;
        }else if(e.getTweetListType()==TweetListType.EXPLORER){
            packetType = PacketType.EXPLORER_TWEET_REQ;
            body = e.getMaxNum() + "," + dateTimeStr;
        }else if(e.getTweetListType()==TweetListType.COMMENT){
            packetType = PacketType.GET_COMMENTS_REQ;
            body = e.getMaxNum() + "," + dateTimeStr + "," + e.getParentTweetID();
        }else if(e.getTweetListType()==TweetListType.MY_TWEETS){
            packetType = PacketType.GET_TWEET_LIST_REQ;
            body = e.getMaxNum() + "," + dateTimeStr;
        }
        socketController.addRequest(
                new Packet(
                        packetType,
                        body,
                        socketController.getAuthToken(),
                        true));
    }

    public void handleActionTweetEvent(ActionTweetEvent e){
        if(e.getActionType()== ActionType.LIKE) {
            socketController.addRequest(
                    new Packet(
                            PacketType.LIKE_TWEET_REQ,
                            Long.toString(e.getTweetID()),
                            socketController.getAuthToken(),
                            true));
        }
        else if(e.getActionType()== ActionType.SPAM) {
            socketController.addRequest(
                    new Packet(
                            PacketType.REPORT_TWEET_REQ,
                            Long.toString(e.getTweetID()),
                            socketController.getAuthToken(),
                            true));
        }
    }

    public void handleRelationUserEvent(RelationUserEvent e){
        PacketType packetType = null;
        if(e.getRelationType()== RelationType.REPORT) {
            packetType = PacketType.REPORT_USER_REQ;
        }
        else if(e.getRelationType()== RelationType.MUTE) {
            packetType = PacketType.MUTE_USER_REQ;
        }
        else if(e.getRelationType()== RelationType.UNMUTE) {
            packetType = PacketType.UNMUTE_USER_REQ;
        }
        else if(e.getRelationType()== RelationType.BLOCK) {
            packetType = PacketType.BLOCK_USER_REQ;
        }
        else if(e.getRelationType()== RelationType.UNBLOCK) {
            packetType = PacketType.UNBLOCK_USER_REQ;
        }
        else if(e.getRelationType()== RelationType.FOLLOW) {
            packetType = PacketType.FOLLOW_USER_REQ;
        }
        else if(e.getRelationType()== RelationType.UNFOLLOW) {
            packetType = PacketType.UNFOLLOW_USER_REQ;
        }
        socketController.addRequest(
                new Packet(
                        packetType,
                        Long.toString(e.getObjectID()),
                        socketController.getAuthToken(),
                        true));
    }

    public void handleNewTweetEvent(NewTweetEvent e){
        String imageStr = "";
        byte[] tweetImage = e.getTweetImage();
        if(tweetImage!=null)
            imageStr = Base64.getEncoder().encodeToString(e.getTweetImage());
        String body = e.getTweetText() + ","
                + e.getTweetDateTime() + ","
                + e.getParentTweetID() + ","
                + e.isRetweeted() + ","
                + imageStr;
        socketController.addRequest(
                new Packet(
                        PacketType.NEW_TWEET_REQ,
                        body,
                        socketController.getAuthToken(),
                        true));
    }

    public void handleSearchUsernameEvent(SearchUsernameEvent e){
        socketController.addRequest(
                new Packet(
                        PacketType.SEARCH_USERNAME_REQ,
                        e.getUserName()+","+e.isExplorer(),
                        socketController.getAuthToken(),
                        true));
    }

    public void handleGetTweetEvent(long tweetID){
        socketController.addRequest(
                new Packet(
                        PacketType.GET_TWEET_REQ,
                        Long.toString(tweetID),
                        socketController.getAuthToken(),
                        true));
    }

    public void handleRelationListEvent(RelationListType relationListType){
        PacketType packetType = null;
        switch (relationListType){
            case BLACKLIST:
                packetType = PacketType.GET_BLACK_LIST_REQ;
                break;
            case FOLLOWER_LIST:
                packetType = PacketType.GET_FOLLOWERS_LIST_REQ;
                break;
            case FOLLOWING_LIST:
                packetType = PacketType.GET_FOLLOWINGS_LIST_REQ;
                break;
        }
        socketController.addRequest(
                new Packet(
                        packetType,
                        "",
                        socketController.getAuthToken(),
                        true));
    }

    public void handleGetNotificationsEvent(boolean isSystem){
        String body = "";
        if(isSystem)
            body = "system";
        socketController.addRequest(
                new Packet(
                        PacketType.GET_NOTIFICATIONS_REQ,
                        body,
                        socketController.getAuthToken(),
                        true));
    }

    public void handleGetPendingListEvent(){
        socketController.addRequest(
                new Packet(
                        PacketType.GET_PENDING_FOLLOW_REQ,
                        "",
                        socketController.getAuthToken(),
                        true));
    }

    public void handleFollowResponseEvent(FollowResponseEvent e){
        PacketType packetType = null;
        String body = e.getUserName();
        switch (e.getFollowResponseType()){
            case ACCEPT:
                packetType = PacketType.ACCEPT_FOLLOW_REQ;
                break;
            case REJECT_NOTIFY:
                packetType = PacketType.REJECT_FOLLOW_REQ;
                body += ",yes";
                break;
            case REJECT:
                packetType = PacketType.REJECT_FOLLOW_REQ;
                body += ",no";
                break;
        }
        socketController.addRequest(
                new Packet(
                        packetType,
                        body,
                        socketController.getAuthToken(),
                        true));
    }

    public void handleEditUserInfoEvent(EditUserInfoEvent e){
        String firstName = e.getFirstName();
        if(firstName!=null) {
            socketController.addRequest(
                    new Packet(
                            PacketType.EDIT_USER_INFO_REQ,
                            "firstName," + firstName,
                            socketController.getAuthToken(),
                            true));
        }
        String lastName = e.getLastName();
        if(lastName!=null) {
            socketController.addRequest(
                    new Packet(
                            PacketType.EDIT_USER_INFO_REQ,
                            "lastName," + lastName,
                            socketController.getAuthToken(),
                            true));
        }
        LocalDate dateOfBirth = e.getDateOfBirth();
        if(dateOfBirth!=null) {
            socketController.addRequest(
                    new Packet(
                            PacketType.EDIT_USER_INFO_REQ,
                            "dateOfBirth," + dateOfBirth,
                            socketController.getAuthToken(),
                            true));
        }
        String phoneNumber = e.getPhoneNumber();
        if(phoneNumber!=null) {
            socketController.addRequest(
                    new Packet(
                            PacketType.EDIT_USER_INFO_REQ,
                            "phoneNumber," + phoneNumber,
                            socketController.getAuthToken(),
                            true));
        }
        String bio = e.getBio();
        if(bio!=null) {
            socketController.addRequest(
                    new Packet(
                            PacketType.EDIT_USER_INFO_REQ,
                            "bio," + bio,
                            socketController.getAuthToken(),
                            true));
        }
        byte[] userImage = e.getUserImage();
        String userImageStr = "";
        if(userImage!=null) {
            userImageStr = Base64.getEncoder().encodeToString(userImage);
        }
        socketController.addRequest(
                new Packet(
                        PacketType.EDIT_USER_INFO_REQ,
                        "userImage," + userImageStr,
                        socketController.getAuthToken(),
                        true));
    }

    public void handleGetUserInfoEvent(long userID){
        socketController.addRequest(
                new Packet(
                        PacketType.GET_USER_INFO_REQ,
                        Long.toString(userID),
                        socketController.getAuthToken(),
                        true));
    }

    public void handleGetPrivateInfoEvent(){
        socketController.addRequest(
                new Packet(
                        PacketType.GET_PRIVATE_INFO_REQ,
                        "",
                        socketController.getAuthToken(),
                        true));
    }

    public void handleGetEditInfoEvent(){
        socketController.addRequest(
                new Packet(
                        PacketType.GET_EDIT_INFO_REQ,
                        "",
                        socketController.getAuthToken(),
                        true));
    }

    public void handleGetChatroomListEvent(){
        socketController.addRequest(
                new Packet(
                        PacketType.GET_CHATROOM_LIST_REQ,
                        "",
                        socketController.getAuthToken(),
                        true));
    }

    public void handleGetCollectionListEvent(CollectionListType collectionListType){
        PacketType packetType = null;
        if(collectionListType==CollectionListType.FOLDER)
            packetType = PacketType.GET_FOLDER_LIST_REQ;
        else if(collectionListType==CollectionListType.GROUP)
            packetType = PacketType.GET_GROUP_LIST_REQ;
        socketController.addRequest(
                new Packet(
                        packetType,
                        "",
                        socketController.getAuthToken(),
                        true));
    }

    public void handleNewCollectionEvent(NewCollectionEvent e){
        PacketType packetType = null;
        if(e.getCollectionListType()==CollectionListType.FOLDER)
            packetType = PacketType.NEW_FOLDER_REQ;
        else if(e.getCollectionListType()==CollectionListType.GROUP)
            packetType = PacketType.NEW_GROUP_REQ;
        socketController.addRequest(
                new Packet(
                        packetType,
                        e.getName(),
                        socketController.getAuthToken(),
                        true));
    }

    public void handleDeleteCollectionEvent(DeleteCollectionEvent e){
        PacketType packetType = null;
        if(e.getCollectionListType()==CollectionListType.FOLDER)
            packetType = PacketType.DELETE_FOLDER_REQ;
        else if(e.getCollectionListType()==CollectionListType.GROUP)
            packetType = PacketType.DELETE_GROUP_REQ;
        socketController.addRequest(
                new Packet(
                        packetType,
                        Long.toString(e.getId()),
                        socketController.getAuthToken(),
                        true));
    }

    public void handleGetEditCollectionListEvent(GetEditCollectionListEvent e){
        PacketType packetType = null;
        if(e.getCollectionListType()==CollectionListType.FOLDER)
            packetType = PacketType.GET_EDIT_FOLDER_LIST_REQ;
        else if(e.getCollectionListType()==CollectionListType.GROUP)
            packetType = PacketType.GET_EDIT_GROUP_LIST_REQ;
        socketController.addRequest(
                new Packet(
                        packetType,
                        Long.toString(e.getId()),
                        socketController.getAuthToken(),
                        true));
    }

    public void handleSetEditCollectionListEvent(SetEditCollectionListEvent e){
        PacketType packetType = null;
        if(e.getCollectionListType()==CollectionListType.FOLDER)
            packetType = PacketType.SET_EDIT_FOLDER_LIST_REQ;
        else if(e.getCollectionListType()==CollectionListType.GROUP)
            packetType = PacketType.SET_EDIT_GROUP_LIST_REQ;

        String body = Long.toString(e.getCollectionID());
        for (String userName: e.getUserNames()) {
            body += ( "," + userName);
        }
        socketController.addRequest(
                new Packet(
                        packetType,
                        body,
                        socketController.getAuthToken(),
                        true));
    }

    public void handleGetMessagesEvent(GetMessagesEvent e){
        LocalDateTime lastMessageDateTime = e.getLastMessageDateTime();
        String dateTimeStr = "";
        if(lastMessageDateTime!=null)
            dateTimeStr = lastMessageDateTime.toString();

        String body = e.getMaxNum() + "," + dateTimeStr + "," + e.getChatID();
        socketController.addRequest(
                new Packet(
                        PacketType.GET_MESSAGES_REQ,
                        body,
                        socketController.getAuthToken(),
                        true));
    }

    public void handleDeleteMessageEvent(long msgID){
        socketController.addRequest(
                new Packet(
                        PacketType.DELETE_MESSAGE_REQ,
                        Long.toString(msgID),
                        socketController.getAuthToken(),
                        true));
    }

    public void handleEditMessageEvent(EditMessageEvent e){
        socketController.addRequest(
                new Packet(
                        PacketType.EDIT_MESSAGE_REQ,
                        Long.toString(e.getMsgID()) + "," + e.getMsgText(),
                        socketController.getAuthToken(),
                        true));
    }

    public void handleGetSelectListEvent(){
        socketController.addRequest(
                new Packet(
                        PacketType.GET_SELECT_LIST_REQ,
                        "",
                        socketController.getAuthToken(),
                        true));
    }

    public void handleNewMessageEvent(NewMessageEvent e){
        byte[] msgImage = e.getMsgImage();
        String msgImageStr = "";
        if(msgImage!=null)
            msgImageStr = Base64.getEncoder().encodeToString(msgImage);

        String dateTimeStr = "";
        LocalDateTime lastMessageDateTime = e.getMsgDateTime();
        if(lastMessageDateTime!=null)
            dateTimeStr = lastMessageDateTime.toString();

        String body = e.getMsgText() + ","
                    + msgImageStr + ","
                    + dateTimeStr + ","
                    + e.isForwarded();
        for (int i = 0; i < e.getIDs().size(); i++) {
            body += ("," + e.getCollectionItemTypes().get(i) + "," + e.getIDs().get(i));
        }
        socketController.addRequest(
                new Packet(
                        PacketType.NEW_MESSAGE_REQ,
                        body,
                        socketController.getAuthToken(),
                        true));
    }

    public void handleWantUpdateChatroomEvent(boolean want){
        socketController.addRequest(
                new Packet(
                        PacketType.WANT_UPDATE_CHATROOM_REQ,
                        Boolean.toString(want),
                        socketController.getAuthToken(),
                        true));
    }

    public void handleWantUpdateChatEvent(boolean want){
        socketController.addRequest(
                new Packet(
                        PacketType.WANT_UPDATE_CHAT_REQ,
                        Boolean.toString(want),
                        socketController.getAuthToken(),
                        true));
    }

    public void doClose(){
        this.loopHandler.pause();
        socketController.closeSocket();
    }

    public ConfigLoader getConfigLoader() {
        return configLoader;
    }

    public SocketController getSocketController() {
        return socketController;
    }
}