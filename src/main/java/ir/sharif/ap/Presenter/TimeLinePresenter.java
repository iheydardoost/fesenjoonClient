package ir.sharif.ap.Presenter;

import com.gluonhq.charm.glisten.afterburner.GluonPresenter;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.control.BottomNavigationButton;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import ir.sharif.ap.Main;
import ir.sharif.ap.model.Tweet;
import ir.sharif.ap.model.TweetTile;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.input.ScrollEvent;


import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.ResourceBundle;

import static ir.sharif.ap.Main.*;

public class TimeLinePresenter extends GluonPresenter<Main> implements Initializable {
    @FXML
    private View timelineTab;

    @FXML
    private BottomNavigationButton navigationTimeline, navigationPrivate, navigationExplore, navigationMessaging, navigationSetting;

    @FXML
    private ListView<TweetTile> timelineListView;

    final static String tabName="TimeLine";
    static EventTarget lastScrollEvent;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Label b = new Label("Hello Dear this is "+tabName+" PAge");

//        timelineTab.setCenter(b);

        timelineListView.setCellFactory(p -> new TweetListCell());
        timelineListView.setPlaceholder(new Label("There are no Tweets"));
        timelineListView.setOnScroll(e -> {
            int rnd = new Random().nextInt();
            if(e.getDeltaY()<0)
                System.out.println("You Reached THE End Baby"+ rnd );
        });


        TweetTile testTweet = null, testTweet2= null, testTweet3= null, testTweet4= null, testTweet5= null;
        try {
            testTweet = new TweetTile("String 1", "Tweet1 Text", "2334",
                    "12313", null,
                    Main.class.getResourceAsStream("/ir/sharif/ap/images/Apple.jpg").readAllBytes(),
                    Main.class.getResourceAsStream("/ir/sharif/ap/images/avatar.png").readAllBytes());
            testTweet2 = new TweetTile("String 2", "Tweet2 Text", "2334",
                    "12313", null,
                    null,
                    Main.class.getResourceAsStream("/ir/sharif/ap/images/avatar.png").readAllBytes());
            testTweet3 = new TweetTile("String 3", "Tweet 3 Text", "2334",
                    "12313", null,
                    Main.class.getResourceAsStream("/ir/sharif/ap/images/Apple.jpg").readAllBytes(),
                    Main.class.getResourceAsStream("/ir/sharif/ap/images/avatar.png").readAllBytes());
            testTweet4 = new TweetTile("String 4", "Tweet 4 Text", "2334",
                    "12313", null,
                    null,
                    Main.class.getResourceAsStream("/ir/sharif/ap/images/avatar.png").readAllBytes());
            testTweet5 = new TweetTile("String tweetText", "Tweet 5 Text", "2334",
                    "12313", null,
                    Main.class.getResourceAsStream("/ir/sharif/ap/images/Apple.jpg").readAllBytes(),
                    Main.class.getResourceAsStream("/ir/sharif/ap/images/avatar.png").readAllBytes());

        } catch (IOException e) {
            e.printStackTrace();
        }
        timelineListView.getItems().clear();
        for (int i = 0; i < 2; i++) {
            timelineListView.getItems().add(testTweet);
            timelineListView.getItems().add(testTweet2);
            timelineListView.getItems().add(testTweet3);
            timelineListView.getItems().add(testTweet4);
            timelineListView.getItems().add(testTweet5);

            timelineListView.getItems().add(testTweet);
            timelineListView.getItems().add(testTweet2);
            timelineListView.getItems().add(testTweet3);
            timelineListView.getItems().add(testTweet4);
            timelineListView.getItems().add(testTweet5);
        }

        timelineTab.showingProperty().addListener((obs, ov, nv) -> {
            if (nv) {
                navigationTimeline.setSelected(true);

                final AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setTitleText(tabName);
                appBar.getActionItems().addAll(
                        MaterialDesignIcon.ARROW_BACK.button(e->MobileApplication.getInstance().switchToPreviousView()),
                        MaterialDesignIcon.POWER_SETTINGS_NEW.button(e-> Platform.exit()));

            }
        });
    }

    public void onNavigationBarChange(ActionEvent e){

        if(e.getSource() == navigationTimeline){
            MobileApplication.getInstance().switchView(TIMELINE_VIEW);

            System.out.println("This Action navigationTimeline");
        }else if(e.getSource() == navigationPrivate){
            MobileApplication.getInstance().switchView(PRIVATE_VIEW);

            System.out.println("This Action navigationPrivate");

        }else if(e.getSource() == navigationExplore){
            MobileApplication.getInstance().switchView(EXPLORE_VIEW);

            System.out.println("This Action navigationExplore");

        }else if(e.getSource() == navigationMessaging){
            MobileApplication.getInstance().switchView(MESSAGING_VIEW);

            System.out.println("This Action navigationMessaging");

        }else if(e.getSource() == navigationSetting){
            MobileApplication.getInstance().switchView(SETTING_VIEW);
            System.out.println("This Action NavigationSetting");
        }
    }


}
