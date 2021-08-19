package ir.sharif.ap;

import com.gluonhq.attach.display.DisplayService;
import com.gluonhq.attach.util.Platform;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.control.Snackbar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import ir.sharif.ap.Presenter.*;
import ir.sharif.ap.View.*;
import ir.sharif.ap.controller.JsonHandler;
import ir.sharif.ap.controller.LogHandler;
import ir.sharif.ap.controller.MainController;
import javafx.geometry.Dimension2D;
import javafx.scene.Scene;

import javax.management.Notification;

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

    public static AppBar mainAppBar;

    private static boolean refreshTimeline=false, refreshExplore=false;

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
            return (View) editUserInfoView.getView();
        });
        addViewFactory(RELATION_LIST_VIEW,() -> {
            final RelationListView relationListView = new RelationListView();
            relationListPresenter = (RelationListPresenter) relationListView.getPresenter();
            return (View) relationListView.getView();
        });
        addViewFactory(MY_INFO_VIEW,() -> {
            final MyInfoView myInfoView = new MyInfoView();
            myInfoPresenter = (MyInfoPresenter) myInfoView.getPresenter();
            return (View) myInfoView.getView();
        });
       addViewFactory(NOTIFICATIONS_VIEW,() -> {
           final NotificationsView notificationsView = new NotificationsView();
           notificationsPresenter = (NotificationsPresenter) notificationsView.getPresenter();
           return (View) notificationsView.getView();
       });

        addViewFactory(EXPLORE_VIEW, () -> {
            final ExploreView exploreView = new ExploreView();
            explorePresenter = (ExplorePresenter) exploreView.getPresenter();
            explorePresenter.addListTweetEventListener(e -> mainController.handleListTweetEvent(e));
            return (View) exploreView.getView();
        });
        addViewFactory(USERINFO_VIEW, () -> {
            final UserInfoView userInfoView = new UserInfoView();
            userInfoPresenter = (UserInfoPresenter) userInfoView.getPresenter();
            return (View) userInfoView.getView();
        });

        addViewFactory(MESSAGING_VIEW, () -> {
            final MessagingView messagingView = new MessagingView();
            return (View) messagingView.getView();
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
}
