package ir.sharif.ap;

import com.gluonhq.attach.display.DisplayService;
import com.gluonhq.attach.util.Platform;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.mvc.View;
import ir.sharif.ap.Presenter.LoginPresenter;
import ir.sharif.ap.View.*;
import ir.sharif.ap.controller.JsonHandler;
import ir.sharif.ap.controller.LogHandler;
import ir.sharif.ap.controller.MainController;

import javafx.geometry.Dimension2D;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.logging.Logger;

public class Main extends MobileApplication {

    private static MainController mainController;

    public final static String TIMELINE_VIEW="TimeLineView";
    public final static String PRIVATE_VIEW="PrivateView";
    public final static String EXPLORE_VIEW="ExploreView";
    public final static String MESSAGING_VIEW="MessagingView";
    public final static String SETTING_VIEW="SettingView";


    private static LoginPresenter loginPresenter;
    @Override
    public void stop(){

        System.out.println("Closing Application...");
        mainController.doClose();
    }


    @Override
    public void init() {
        addViewFactory(HOME_VIEW, () -> {
            final LoginView loginView = new LoginView();
            loginPresenter = (LoginPresenter) loginView.getPresenter();
            loginPresenter.addAuthFormListener(e -> {
                mainController.handleAuthEvent(e);
            });
            return (View) loginView.getView();
        });

        addViewFactory(TIMELINE_VIEW, () -> {
            final TimeLineView timeLineView = new TimeLineView();
            return (View) timeLineView.getView();
        });

        addViewFactory(PRIVATE_VIEW, () -> {
            final PrivateView privateView = new PrivateView();
            return (View) privateView.getView();
        });
        addViewFactory(EXPLORE_VIEW, () -> {
            final ExploreView exploreView = new ExploreView();
            return (View) exploreView.getView();
        });
        addViewFactory(MESSAGING_VIEW, () -> {
            final MessagingView messagingView = new MessagingView();
            return (View) messagingView.getView();
        });

        addViewFactory(SETTING_VIEW, () -> {
            final SettingView settingView = new SettingView();
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
}
