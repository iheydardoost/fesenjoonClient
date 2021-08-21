package ir.sharif.ap.presenter;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.control.BottomNavigationButton;
import com.gluonhq.charm.glisten.control.Snackbar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import ir.sharif.ap.Main;
import ir.sharif.ap.controller.MainController;
import ir.sharif.ap.model.TweetListType;
import ir.sharif.ap.model.TweetTile;
import ir.sharif.ap.presenter.events.ListTweetEvent;
import ir.sharif.ap.presenter.events.SearchUsernameEvent;
import ir.sharif.ap.presenter.listeners.ListTweetEventListener;
import ir.sharif.ap.presenter.listeners.SearchUsernameEventListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

import static ir.sharif.ap.Main.*;
import static ir.sharif.ap.presenter.Styles.DEFAULT_BUTTON_STYLE;

public class ExplorePresenter implements Initializable {
    @FXML
    private View exploreTab;
    @FXML
    private HBox searchBar;

    private Button searchButton;
    @FXML
    private BottomNavigationButton navigationTimeline, navigationPrivate, navigationExplore, navigationMessaging, navigationSetting;

    @FXML
    private TextField searchText;

    @FXML
    private ListView<TweetTile> exploreList;
    private Snackbar snackbar;


    private final String TAB_NAME ="Explore";
    private ListTweetEventListener listTweetEventListener;
    private SearchUsernameEventListener searchUsernameEventListener;
    private static LocalDateTime previousLastTweetTime = null;

    public void addSearchUsernameEventListener(SearchUsernameEventListener searchUsernameEventListener) {
        this.searchUsernameEventListener = searchUsernameEventListener;
    }

    public void addListTweetEventListener(ListTweetEventListener listTweetEventListener) {
        this.listTweetEventListener = listTweetEventListener;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        snackbar = new Snackbar("");
        searchButton = MaterialDesignIcon.SEARCH.button(e -> {
            searchUsernameEventListener.searchUsernameEventOccurred(
                    new SearchUsernameEvent(true,searchText.getText()));
        });
        searchButton.setStyle(DEFAULT_BUTTON_STYLE);
        searchBar.getChildren().add(searchButton);


        exploreList.setCellFactory(p -> {
            TweetListCell tweetListCell = new TweetListCell();
            tweetListCell.addActionTweetEventListener(e -> Main.getMainController().handleActionTweetEvent(e));
            tweetListCell.addNewTweetListener(e -> Main.getMainController().handleNewTweetEvent(e));
            return tweetListCell;
        });
        exploreList.setPlaceholder(new Label("There are no Tweets"));
        exploreList.setOnScroll(e -> {
            if(e.getDeltaY()<0){

                LocalDateTime lastTweetTime = null;
                if(exploreList.getItems().size()>0){
                    lastTweetTime = exploreList.getItems().get(exploreList.getItems().size()-1).getTweetDateTime();
                }
                if(previousLastTweetTime != null)
                    if(previousLastTweetTime.equals(lastTweetTime)){
                        return;
                    }
                previousLastTweetTime = lastTweetTime;
                ListTweetEvent event = new ListTweetEvent(
                        MainController.MAX_TWEET_LIST_REQUEST_NUMBER,
                        lastTweetTime,
                        TweetListType.EXPLORER,
                        0);
                listTweetEventListener.listTweetEventOccurred(event);
            }
        });

        exploreTab.showingProperty().addListener((obs, ov, nv) -> {
            if (nv) {
                navigationExplore.setSelected(true);

                if(Main.refreshExplore()){
                    Main.setRefreshExplore(false);
                    exploreList.getItems().clear();
                    ListTweetEvent event = new ListTweetEvent(
                            MainController.MAX_TWEET_LIST_REQUEST_NUMBER,
                            null,
                            TweetListType.EXPLORER,
                            0);
                    listTweetEventListener.listTweetEventOccurred(event);
                }

                final AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setTitleText(TAB_NAME);
                appBar.getActionItems().addAll(mainAppBar.getActionItems());
                searchText.setText("");
            }
        });
    }
    public void onTweetReceive(String response){
        TweetTile tweet = Utility.createTweetFromResponse(response);
        exploreList.getItems().add(tweet);
    }
    public void onNavigationBarChange(ActionEvent e){
        if(e.getSource() == navigationTimeline){
            Main.setRefreshTimeline(true);
            MobileApplication.getInstance().switchView(TIMELINE_VIEW);
        }else if(e.getSource() == navigationPrivate){
            MobileApplication.getInstance().switchView(PRIVATE_VIEW);
        }else if(e.getSource() == navigationExplore){
            Main.setRefreshExplore(true);
            MobileApplication.getInstance().switchView(EXPLORE_VIEW);
        }else if(e.getSource() == navigationMessaging){
            MobileApplication.getInstance().switchView(MESSAGING_VIEW);
        }else if(e.getSource() == navigationSetting){
            MobileApplication.getInstance().switchView(SETTING_VIEW);
        }
    }

    public void onSearchResultReceive(String searchResult){
        String[] args = searchResult.split(",",-1);
        snackbar.setMessage(args[0]);
        snackbar.show();
        if(args[0].equals("found")){
            if(Main.getUserName().equals(searchText.getText())){
                MobileApplication.getInstance().switchView(MY_INFO_VIEW);
            }else{
                MobileApplication.getInstance().switchView(USERINFO_VIEW);
                Main.getUserInfoPresenter().setUserID(Long.parseLong(args[1]));
                Main.getUserInfoPresenter().update();
            }

        }else{
            snackbar.setMessage("User not found");
            snackbar.show();
        }
    }


}
