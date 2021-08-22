package ir.sharif.ap;

import com.gluonhq.attach.display.DisplayService;
import com.gluonhq.attach.util.Platform;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.control.Snackbar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import ir.sharif.ap.controller.offline.OfflineController;
import ir.sharif.ap.presenter.*;
import ir.sharif.ap.view.*;
import ir.sharif.ap.controller.JsonHandler;
import ir.sharif.ap.controller.LogHandler;
import ir.sharif.ap.controller.MainController;
import ir.sharif.ap.model.*;
import ir.sharif.ap.presenter.listeners.LogoutListener;
import javafx.geometry.Dimension2D;
import javafx.scene.Scene;

import java.io.IOException;
import java.util.ArrayList;

public class Main extends MobileApplication {

    private static MainController mainController;
    private static OfflineController offlineController;

    public final static String TIMELINE_VIEW="TimeLineView";
    public final static String PRIVATE_VIEW="PrivateView";
    public final static String EXPLORE_VIEW="ExploreView";
    public final static String MESSAGING_VIEW="MessagingView";
    public final static String SETTING_VIEW="SettingView";
    public final static String NEW_TWEET_VIEW="NewTweetView";
    public final static String MY_TWEET_LIST_VIEW="MyTweetListView";
    public final static String EDIT_USER_INFO_VIEW="EditUserInfoView";
    public final static String RELATION_LIST_VIEW="RelationListView";
    public final static String MY_INFO_VIEW="MyInfoView";
    public final static String NOTIFICATIONS_VIEW="NotificationsView";
    public final static String USERINFO_VIEW="UserInfoView";
    public final static String CHAT_VIEW="ChatView";
    public final static String CHATS_ROOM_VIEW="ChatsRoomView";
    public final static String COLLECTION_EDIT_VIEW="CollectionEditView";
    public final static String MANAGE_COLLECTION_VIEW="ManageCollectionView";
    public final static String NEW_MESSAGE_VIEW="NewMessageView";

    private static int lastViewIndex= 0;

    private Snackbar snackbar;

    private static LoginPresenter loginPresenter;
    private static SettingPresenter settingPresenter;
    private static TimeLinePresenter timeLinePresenter;
    private static ExplorePresenter explorePresenter;
    private static NewTweetPresenter newTweetPresenter;
    private static PrivatePresenter privatePresenter;
    private static MyTweetListPresenter myTweetListPresenter;
    private static EditUserInfoPresenter editUserInfoPresenter;
    private static RelationListPresenter relationListPresenter;
    private static MyInfoPresenter myInfoPresenter;
    private static NotificationsPresenter notificationsPresenter;
    private static UserInfoPresenter userInfoPresenter;
    private static MessagingPresenter messagingPresenter;
    private static ChatPresenter chatPresenter;
    private static ChatsRoomPresenter chatsRoomPresenter;
    private static CollectionEditPresenter collectionEditPresenter;
    private static ManageCollectionPresenter manageCollectionPresenter;
    private static NewMessagePresenter newMessagePresenter;

    private static String userName;
    public static AppBar mainAppBar;
    private static TweetDetailPresenter currentTweetDetailPresenter;

    private static boolean refreshTimeline=false, refreshExplore=false;
    private static RelationListType relationListType;
    private static NotificationListType notificationListType;
    private static CollectionListType collectionListType;
    private static EditCollectionListType editCollectionListType;
    private static long targetCollectionID;
    private static ArrayList<CollectionItem> messageReceiverList = new ArrayList<>();
    private static long chatID;
    private static boolean wantUpdateChatroom=false, wantUpdateChat=false;
    private static String forwardMessageText;
    private static byte[] forwardMessageImage;
    private static OnlineStatus onlineStatus;

    public static String getForwardMessageText() {
        return forwardMessageText;
    }

    public static void setForwardMessageText(String forwardMessageText) {
        Main.forwardMessageText = forwardMessageText;
    }

    public static byte[] getForwardMessageImage() {
        return forwardMessageImage;
    }

    public static void setForwardMessageImage(byte[] forwardMessageImage) {
        Main.forwardMessageImage = forwardMessageImage;
    }

    @Override
    public void stop(){
        mainController.doClose();
        if(offlineController.getDbCommunicator().isConnectionValid())
            doCloseOffline();
        System.exit(0);
    }

    @Override
    public void init() {
        mainAppBar = new AppBar();
        mainAppBar.getActionItems().addAll(
                MaterialDesignIcon.ARROW_BACK.button(e->MobileApplication.getInstance().switchToPreviousView()),
                MaterialDesignIcon.POWER_SETTINGS_NEW.button(e-> Main.doLogout()));

        addViewFactory(HOME_VIEW, () -> {
            final LoginView loginView = new LoginView();
            loginPresenter = (LoginPresenter) loginView.getPresenter();
            loginPresenter.addAuthFormListener(e -> {
                mainController.handleAuthEvent(e);
            });
            return (View) loginView.getView();
        });

        addViewFactory(NEW_MESSAGE_VIEW, () -> {
            final NewMessageView newMessageView = new NewMessageView();
            newMessagePresenter = (NewMessagePresenter) newMessageView.getPresenter();
            newMessagePresenter.addNewMessageEventListener(e -> mainController.handleNewMessageEvent(e));
            return (View) newMessageView.getView();
        });

        addViewFactory(NEW_TWEET_VIEW, () -> {
            final NewTweetView newTweetView = new NewTweetView();
            newTweetPresenter = (NewTweetPresenter) newTweetView.getPresenter();
            newTweetPresenter.addNewTweetListener(e -> mainController.handleNewTweetEvent(e));
            return (View) newTweetView.getView();
        });

        addViewFactory(TIMELINE_VIEW, () -> {
            final TimeLineView timeLineView = new TimeLineView();
            timeLinePresenter = (TimeLinePresenter) timeLineView.getPresenter();
            timeLinePresenter.addListTweetEventListener(e -> mainController.handleListTweetEvent(e));
            return (View) timeLineView.getView();
        });

        addViewFactory(PRIVATE_VIEW, () -> {
            final PrivateView privateView = new PrivateView();
            privatePresenter = (PrivatePresenter) privateView.getPresenter();

            return (View) privateView.getView();
        });

        addViewFactory(MY_TWEET_LIST_VIEW,() -> {
            final MyTweetListView myTweetListView = new MyTweetListView();
            myTweetListPresenter = (MyTweetListPresenter) myTweetListView.getPresenter();
            myTweetListPresenter.addListTweetEventListener(e -> mainController.handleListTweetEvent(e));
            return (View) myTweetListView.getView();
        });

        addViewFactory(EDIT_USER_INFO_VIEW,() -> {
            final EditUserInfoView editUserInfoView = new EditUserInfoView();
            editUserInfoPresenter = (EditUserInfoPresenter) editUserInfoView.getPresenter();
            editUserInfoPresenter.addEditUserInfoEventListener(e -> mainController.handleEditUserInfoEvent(e));
            editUserInfoPresenter.addGetPrivateInfoEventListener(sure -> mainController.handleGetEditInfoEvent());
            return (View) editUserInfoView.getView();
        });

        addViewFactory(RELATION_LIST_VIEW,() -> {
            final RelationListView relationListView = new RelationListView();
            relationListPresenter = (RelationListPresenter) relationListView.getPresenter();
            relationListPresenter.addRelationListEventListener(e-> mainController.handleRelationListEvent(e));
            relationListPresenter.addRelationUserEventListener(e -> mainController.handleRelationUserEvent(e));
            relationListPresenter.addSearchUsernameEventListener(e -> mainController.handleSearchUsernameEvent(e));

            return (View) relationListView.getView();
        });

        addViewFactory(MY_INFO_VIEW,() -> {
            final MyInfoView myInfoView = new MyInfoView();
            myInfoPresenter = (MyInfoPresenter) myInfoView.getPresenter();
            myInfoPresenter.addGetPrivateInfoEventListener(sure -> mainController.handleGetPrivateInfoEvent());
            return (View) myInfoView.getView();
        });

        addViewFactory(NOTIFICATIONS_VIEW,() -> {
           final NotificationsView notificationsView = new NotificationsView();
           notificationsPresenter = (NotificationsPresenter) notificationsView.getPresenter();
           notificationsPresenter.addGetPendingListEventListener(sure -> mainController.handleGetPendingListEvent());
           notificationsPresenter.addFollowResponseEventListener(e -> mainController.handleFollowResponseEvent(e));
           notificationsPresenter.addGetNotificationsEventListener(isSystem -> mainController.handleGetNotificationsEvent(isSystem));
           return (View) notificationsView.getView();
        });

        addViewFactory(EXPLORE_VIEW, () -> {
            final ExploreView exploreView = new ExploreView();
            explorePresenter = (ExplorePresenter) exploreView.getPresenter();
            explorePresenter.addListTweetEventListener(e -> mainController.handleListTweetEvent(e));
            explorePresenter.addSearchUsernameEventListener(e -> mainController.handleSearchUsernameEvent(e));
            return (View) exploreView.getView();
        });

        addViewFactory(USERINFO_VIEW, () -> {
            final UserInfoView userInfoView = new UserInfoView();
            userInfoPresenter = (UserInfoPresenter) userInfoView.getPresenter();
            userInfoPresenter.addGetUserInfoEventListener(userID -> mainController.handleGetUserInfoEvent(userID));
            userInfoPresenter.addRelationUserEventListener(e -> mainController.handleRelationUserEvent(e));
            userInfoPresenter.addGetChatIDByUserIDEventListener(e -> mainController.handleGetChatIDEvent(e));
            return (View) userInfoView.getView();
        });

        addViewFactory(SETTING_VIEW, () -> {
            final SettingView settingView = new SettingView();
            settingPresenter = (SettingPresenter) settingView.getPresenter();
            settingPresenter.addSettingInfoListener(e->mainController.handleSettingInfoEvent());
            settingPresenter.addUserDeleteListener(e->mainController.handleUserDeleteEvent());
            settingPresenter.addLogoutListener(e->mainController.handleLogoutEvent());
            settingPresenter.addSettingChangeFormListener(e->mainController.handleSettingChangeEvent(e));
            return (View) settingView.getView();
        });

        addViewFactory(MESSAGING_VIEW, () -> {
            final MessagingView messagingView = new MessagingView();
            messagingPresenter = (MessagingPresenter) messagingView.getPresenter();
            return (View) messagingView.getView();
        });

        addViewFactory(CHAT_VIEW, () -> {
            final ChatView chatView = new ChatView();
            chatPresenter = (ChatPresenter) chatView.getPresenter();
            chatPresenter.addGetMessagesEventListener(e -> mainController.handleGetMessagesEvent(e));
            chatPresenter.addNewMessageEventListener(e -> mainController.handleNewMessageEvent(e));
            chatPresenter.addEditMessageEventListener(e -> mainController.handleEditMessageEvent(e));
            chatPresenter.addWantUpdateChatEventListener(e -> mainController.handleWantUpdateChatEvent(e));
            return (View) chatView.getView();
        });

        addViewFactory(CHATS_ROOM_VIEW, () -> {
            final ChatsRoomView chatsRoomView = new ChatsRoomView();
            chatsRoomPresenter = (ChatsRoomPresenter) chatsRoomView.getPresenter();
            chatsRoomPresenter.addGetChatroomListEventListener(sure -> mainController.handleGetChatroomListEvent());
            chatsRoomPresenter.addWantUpdateChatroomEventListener(e -> mainController.handleWantUpdateChatroomEvent(e));
            return (View) chatsRoomView.getView();
        });

        addViewFactory(COLLECTION_EDIT_VIEW, () -> {
            final CollectionEditView collectionEditView = new CollectionEditView();
            collectionEditPresenter = (CollectionEditPresenter) collectionEditView.getPresenter();
            collectionEditPresenter.addGetSelectListEventListener(sure -> mainController.handleGetSelectListEvent());
            collectionEditPresenter.addGetEditCollectionListEventListener(e -> mainController.handleGetEditCollectionListEvent(e));
            collectionEditPresenter.addSetEditCollectionListEventListener(e -> mainController.handleSetEditCollectionListEvent(e));
            collectionEditPresenter.addNewMessageEventListener(e -> mainController.handleNewMessageEvent(e));
            return (View) collectionEditView.getView();
        });

        addViewFactory(MANAGE_COLLECTION_VIEW, () -> {
            final ManageCollectionView manageCollectionView = new ManageCollectionView();
            manageCollectionPresenter=(ManageCollectionPresenter) manageCollectionView.getPresenter();
            manageCollectionPresenter.addGetCollectionListEventListener(e->mainController.handleGetCollectionListEvent(e));
            manageCollectionPresenter.addNewCollectionEventListener(e->mainController.handleNewCollectionEvent(e));
            manageCollectionPresenter.addDeleteCollectionEventListener(e -> mainController.handleDeleteCollectionEvent(e));
            return (View) manageCollectionView.getView();
        });
    }

    @Override
    public void postInit(Scene scene) {
        if (Platform.isDesktop()) {
            Dimension2D dimension2D = DisplayService.create()
                    .map(DisplayService::getScreenResolution)
                    .orElse(new Dimension2D(640, 480));
            scene.getWindow().setWidth(640);
            scene.getWindow().setHeight(640);

//            scene.getWindow().setWidth(dimension2D.getWidth());
//            scene.getWindow().setHeight(dimension2D.getHeight());
        }
    }

    public static void main(String[] args) {
        JsonHandler.initMapper();
        LogHandler.initLogger(false);
        mainController = new MainController();
        offlineController = new OfflineController();
        offlineController.getOfflineSocketController().initConnection();
        onlineStatus = OnlineStatus.LOG_IN;

        launch(args);
    }

    public static boolean connectToServer(){
        try {
            if(onlineStatus==OnlineStatus.LOG_IN) {
                mainController.getSocketController().initConnection(true);
                onlineStatus = OnlineStatus.ONLINE;
                return true;
            }
        } catch (IOException e) {
            //e.printStackTrace();
        }
        if(onlineStatus==OnlineStatus.LOG_IN) {
            LogHandler.logger.error("could not connect to Server");
            loginPresenter.onAuthReceive("error,server is not accessible");
        }
        return false;
    }

    public static boolean connectOffline(){
        try {
            if(onlineStatus==OnlineStatus.LOG_IN)
                if(offlineController.getDbCommunicator().connectDB()) {
                    mainController.getSocketController().initConnection(false);
                    onlineStatus = OnlineStatus.OFFLINE;
                    return true;
                }
        } catch (IOException e) {
            //e.printStackTrace();
        }
        if(onlineStatus==OnlineStatus.LOG_IN) {
            LogHandler.logger.error("could not connect offline");
            loginPresenter.onAuthReceive("error,offline mode is not accessible");
        }
        return false;
    }

    public static void doCloseOffline(){
        mainController.getSocketController().getLoopHandler().pause();
    }

    private static LogoutListener logoutListener = new LogoutListener() {
        @Override
        public void logoutOccurred(boolean sure) {
            mainController.handleLogoutEvent();
        }
    };

    public static void doLogout(){
        logoutListener.logoutOccurred(true);
    }

    public static void onLogoutResponse(String result){
        if(result.equals("success")){
            MobileApplication.getInstance().switchView(HOME_VIEW);
            onlineStatus = OnlineStatus.LOG_IN;
            doCloseOffline();
            mainController.getSocketController().closeSocket();
        }
    }

    public static OnlineStatus getOnlineStatus() {
        return onlineStatus;
    }

    public static void setOnlineStatus(OnlineStatus onlineStatus) {
        Main.onlineStatus = onlineStatus;
    }

    public static String getNextViewName(){
        lastViewIndex++;
        return "ViewNumber" + String.valueOf(lastViewIndex);
    }

    public static OfflineController getOfflineController() {
        return offlineController;
    }

    public static long getChatID() {
        return chatID;
    }

    public static void setChatID(long chatID) {
        Main.chatID = chatID;
    }

    public static ArrayList<CollectionItem> getMessageReceiverList() {
        return messageReceiverList;
    }

    public static void setMessageReceiverList(ArrayList<CollectionItem> messageReceiverList) {
        Main.messageReceiverList.clear();
        Main.messageReceiverList.addAll(messageReceiverList);
    }

    public static MainController getMainController() {
        return mainController;
    }

    public static LoginPresenter getLoginPresenter() {
        return loginPresenter;
    }

    public static SettingPresenter getSettingPresenter() {
        return settingPresenter;
    }

    public static TimeLinePresenter getTimeLinePresenter() {
        return timeLinePresenter;
    }

    public static ExplorePresenter getExplorePresenter() {
        return explorePresenter;
    }

    public static NewTweetPresenter getNewTweetPresenter() {
        return newTweetPresenter;
    }

    public static MyTweetListPresenter getMyTweetListPresenter() {
        return myTweetListPresenter;
    }

    public static EditUserInfoPresenter getEditUserInfoPresenter() {
        return editUserInfoPresenter;
    }

    public static RelationListPresenter getRelationListPresenter() {
        return relationListPresenter;
    }

    public static MyInfoPresenter getMyInfoPresenter() {
        return myInfoPresenter;
    }

    public static NotificationsPresenter getNotificationsPresenter() {
        return notificationsPresenter;
    }

    public static PrivatePresenter getPrivatePresenter() {
        return privatePresenter;
    }

    public static UserInfoPresenter getUserInfoPresenter() {
        return userInfoPresenter;
    }

    public static MessagingPresenter getMessagingPresenter() {
        return messagingPresenter;
    }

    public static ChatPresenter getChatPresenter() {
        return chatPresenter;
    }

    public static ChatsRoomPresenter getChatsRoomPresenter() {
        return chatsRoomPresenter;
    }

    public static CollectionEditPresenter getCollectionEditPresenter() {
        return collectionEditPresenter;
    }

    public static ManageCollectionPresenter getManageCollectionPresenter() {
        return manageCollectionPresenter;
    }

    public static NewMessagePresenter getNewMessagePresenter() {
        return newMessagePresenter;
    }

    public static long getTargetCollectionID() {
        return targetCollectionID;
    }

    public static void clearMessageReceiverList(){
        Main.messageReceiverList.clear();
    }

    public static void setTargetCollectionID(long targetCollectionID) {
        Main.targetCollectionID = targetCollectionID;
    }

    public static EditCollectionListType getEditCollectionListType() {
        return editCollectionListType;
    }

    public static void setEditCollectionListType(EditCollectionListType editCollectionListType) {
        Main.editCollectionListType = editCollectionListType;
    }

    public static CollectionListType getCollectionType() {
        return collectionListType;
    }

    public static void setCollectionType(CollectionListType collectionListType) {
        Main.collectionListType = collectionListType;
    }

    public static void setUserName(String userName) {
        Main.userName = userName;
    }

    public static String getUserName() {
        return userName;
    }

    public static TweetDetailPresenter getCurrentTweetDetailPresenter() {
        return currentTweetDetailPresenter;
    }

    public static void setCurrentTweetDetailPresenter(TweetDetailPresenter currentTweetDetailPresenter) {
        Main.currentTweetDetailPresenter = currentTweetDetailPresenter;
    }

    public static NotificationListType getNotificationListType() {
        return notificationListType;
    }

    public static void setNotificationListType(NotificationListType notificationListType) {
        Main.notificationListType = notificationListType;
    }

    public static RelationListType getRelationListType() {
        return relationListType;
    }

    public static void setRelationListType(RelationListType relationType) {
        relationListType = relationType;
    }

    public static boolean refreshTimeline() {
        return refreshTimeline;
    }

    public static void setRefreshTimeline(boolean refresh) {
        refreshTimeline = refresh;
    }

    public static boolean refreshExplore() {
        return refreshExplore;
    }

    public static void setRefreshExplore(boolean refresh) {
        refreshExplore = refresh;
    }

}
