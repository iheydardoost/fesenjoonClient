package ir.sharif.ap.Presenter;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import ir.sharif.ap.model.TweetTile;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class TweetDetailPresenter implements Initializable {
    @FXML
    private Label text;

    @FXML
    private View tweetDetailTab;
    @FXML
    private ListView<TweetTile> tweetDetailList;

    private TweetTile mainTweet;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        tweetDetailList.setCellFactory(p -> new TweetListCell());
        tweetDetailList.setPlaceholder(new Label("There are no Tweets"));
        tweetDetailList.setOnScroll(e -> {
            int rnd = new Random().nextInt();
            if(e.getDeltaY()<0)
                System.out.println("You Reached THE End Baby"+ rnd );
        });

        tweetDetailTab.showingProperty().addListener((obs, ov, nv) -> {
            if (nv) {

                final AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setTitleText("Tweet");
                appBar.getActionItems().addAll(
                        MaterialDesignIcon.ARROW_BACK.button(e->MobileApplication.getInstance().switchToPreviousView()),
                        MaterialDesignIcon.POWER_SETTINGS_NEW.button(e-> Platform.exit()));

            }
        });
    }

    void setMainTweet(TweetTile mainTweet){
        this.mainTweet=mainTweet;//new TweetTile(mainTweet);
        mainTweet.setMainTweet(true);
        tweetDetailList.getItems().clear();
        tweetDetailList.getItems().add(mainTweet);

    }
}
