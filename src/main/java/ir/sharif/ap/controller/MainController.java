package ir.sharif.ap.controller;

import com.gluonhq.charm.glisten.application.MobileApplication;
import ir.sharif.ap.Main;
import ir.sharif.ap.Presenter.*;
import ir.sharif.ap.model.*;
import javafx.application.Platform;

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


    public void handleGetTweetEvent(long tweetID){

    }
    public void handleListTweetEvent(ListTweetEvent e){
        LocalDateTime lastTweetDateTime = e.getLastTweetDateTime();
        String dateTimeStr = "";
        if(lastTweetDateTime!=null)
            dateTimeStr = lastTweetDateTime.toString();
        if(e.getTweetListType()==TweetListType.TIMELINE) {
            socketController.addRequest(
                    new Packet(
                            PacketType.TIMELINE_TWEET_REQ,
                            e.getMaxNum() + "," + dateTimeStr,
                            socketController.getAuthToken(),
                            true));
        }
        else if(e.getTweetListType()==TweetListType.EXPLORE){
            socketController.addRequest(
                    new Packet(
                            PacketType.EXPLORER_TWEET_REQ,
                            e.getMaxNum() + "," + dateTimeStr,
                            socketController.getAuthToken(),
                            true));
        }else if(e.getTweetListType()==TweetListType.COMMENT){

        }else if(e.getTweetListType()==TweetListType.MY_TWEETS){

        }

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
        if(e.getRelationType()== RelationType.REPORT) {
            socketController.addRequest(
                    new Packet(
                            PacketType.REPORT_USER_REQ,
                            Long.toString(e.getObjectID()),
                            socketController.getAuthToken(),
                            true));
        }
        else if(e.getRelationType()== RelationType.MUTE) {
            socketController.addRequest(
                    new Packet(
                            PacketType.MUTE_USER_REQ,
                            Long.toString(e.getObjectID()),
                            socketController.getAuthToken(),
                            true));
        }
        else if(e.getRelationType()== RelationType.BLOCK) {
            socketController.addRequest(
                    new Packet(
                            PacketType.BLOCK_USER_REQ,
                            Long.toString(e.getObjectID()),
                            socketController.getAuthToken(),
                            true));
        }
    }

    public void handleNewTweetEvent(NewTweetEvent e){
        String imageStr = "";
        byte[] tweetImage = e.getTweetImage();
        if(tweetImage!=null)
            imageStr = Base64.getEncoder().encodeToString(e.getTweetImage());
        String body = e.getTweetText() + ","
                + e.getTweetDateTime() + ","
                + e.getUserID() + ","
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