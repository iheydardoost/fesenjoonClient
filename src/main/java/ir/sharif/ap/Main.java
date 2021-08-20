package ir.sharif.ap;

import com.gluonhq.attach.display.DisplayService;
import com.gluonhq.attach.util.Platform;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.control.Snackbar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import ir.sharif.ap.presenter.*;
import ir.sharif.ap.View.*;
import ir.sharif.ap.controller.JsonHandler;
import ir.sharif.ap.controller.LogHandler;
import ir.sharif.ap.controller.MainController;
import ir.sharif.ap.model.*;
import ir.sharif.ap.presenter.listeners.LogoutListener;
import javafx.geometry.Dimension2D;
import javafx.scene.Scene;

import java.util.ArrayList;

public class Main extends MobileApplication {

    private static MainController mainController;

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
    private static LogoutListener logoutListener = new LogoutListener() {
        @Override
        public void logoutOccurred(boolean sure) {
            mainController.handleLogoutEvent();
        }
    };

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
    private static ArrayList<CollectionItem> messageReceiverList = new ArrayList<CollectionItem>();
    private static long chatID;

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

    @Override
    public void stop(){
        mainController.doClose();
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
            return (View) chatView.getView();
        });

        addViewFactory(CHATS_ROOM_VIEW, () -> {
            final ChatsRoomView chatsRoomView = new ChatsRoomView();
            chatsRoomPresenter = (ChatsRoomPresenter) chatsRoomView.getPresenter();
            chatsRoomPresenter.addGetChatroomListEventListener(sure -> mainController.handleGetChatroomListEvent());
            return (View) chatsRoomView.getView();
        });

        addViewFactory(COLLECTION_EDIT_VIEW, () -> {
            final CollectionEditView collectionEditView = new CollectionEditView();
            collectionEditPresenter = (CollectionEditPresenter) collectionEditView.getPresenter();
            return (View) collectionEditView.getView();
        });

        addViewFactory(MANAGE_COLLECTION_VIEW, () -> {
            final ManageCollectionView manageCollectionView = new ManageCollectionView();
            manageCollectionPresenter=(ManageCollectionPresenter) manageCollectionView.getPresenter();
            manageCollectionPresenter.addGetCollectionListEventListener(e->mainController.handleGetCollectionListEvent(e));
            manageCollectionPresenter.addNewCollectionEventListener(e->mainController.handleNewCollectionEvent(e));
            manageCollectionPresenter.addDeleteCollectionEventListener(e -> mainController.handleDeleteCollectionEvent(e));
            manageCollectionPresenter.addGetEditCollectionListEventListener(e->mainController.handleGetEditCollectionListEvent(e));
            manageCollectionPresenter.addSetEditCollectionListEventListener(e -> mainController.handleSetEditCollectionListEvent(e));
            return (View) manageCollectionView.getView();
        });


//        addViewFactory(HOME_VIEW, () -> {
//            FloatingActionButton fab = new FloatingActionButton(MaterialDesignIcon.SEARCH.text,
//                    e -> System.out.println("Search"));
//
//
//            ImageView imageView = new ImageView(new Image(Main.class.getResourceAsStream("openduke.png")));
//            imageView.setFitHeight(200);
//            imageView.setPreserveRatio(true);
//
//            Label label = new Label("Hello, Gluon Mo22bile!");
//
//            VBox root = new VBox(20, imageView, label);
//            root.setAlignment(Pos.CENTER);
//
//            View view = new View(root) {
//                @Override
//                protected void updateAppBar(AppBar appBar) {
//                    appBar.setTitleText("Gluon Mobile");
//                }
//            };
//
//            fab.showOn(view);
//
//            return view;
//        });
    }

    @Override
    public void postInit(Scene scene) {
//        Swatch.LIGHT_GREEN.assignTo(scene);
//        scene.getStylesheets().add(Main.class.getResource("styles.css").toExternalForm());

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
        mainController.getSocketController().initConnection();

        launch(args);
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
    public static String getNextViewName(){
        lastViewIndex++;
        return "ViewNumber" + String.valueOf(lastViewIndex);
    }

    public static void doLogout(){
        logoutListener.logoutOccurred(true);
    }
    public static void onLogoutResponse(String result){
        if(result.equals("success")){
            MobileApplication.getInstance().switchView(HOME_VIEW);
        }
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
}
