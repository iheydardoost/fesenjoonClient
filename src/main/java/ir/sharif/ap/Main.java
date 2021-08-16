package ir.sharif.ap;

import com.gluonhq.attach.display.DisplayService;
import com.gluonhq.attach.util.Platform;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.mvc.View;
import ir.sharif.ap.Presenter.LoginPresenter;
import ir.sharif.ap.View.LoginView;
import ir.sharif.ap.controller.MainController;

import ir.sharif.ap.View.TimeLineView;

import javafx.geometry.Dimension2D;
import javafx.scene.Scene;

public class Main extends MobileApplication {

    private static MainController mainController;
    public final static String TIMELINE_VIEW="TimeLineView";

    @Override
    public void init() {
        addViewFactory(HOME_VIEW, () -> {
            final LoginView loginView = new LoginView();
            LoginPresenter loginPresenter = (LoginPresenter) loginView.getPresenter();
            loginPresenter.addAuthFormListener(e -> {
                mainController.handleAuthEvent(e);
            });
            return (View) loginView.getView();
        });

        addViewFactory(TIMELINE_VIEW, () -> {
            final TimeLineView timeLineView = new TimeLineView();
            return (View) timeLineView.getView();
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
        mainController = new MainController();

        launch(args);
    }

    public static MainController getMainController() {
        return mainController;
    }
}
