package controller;

import model.Packet;

public class MainController implements Runnable{
    private LoopHandler loopHandler;
    private SocketController socketController;

    public MainController() {
        this.socketController = new SocketController();
        this.loopHandler = new LoopHandler(180,this);
    }

    @Override
    public void run() {
        Packet response = socketController.getResponse();
        if(response==null) return;
        if(response.isAuthTokenAvailable()) socketController.setAuthToken(response.getAuthToken());

//        switch (response.getPacketType()){
//            case AUTHENTICATION_ERROR:
//                break;
//            case AUTHENTICATION_SUCCESS:
//                break;
//            case INFO_RES:
//                break;
//            case PLAYING_RES:
//                break;
//            case WATCH_RES:
//                break;
//            case SCORE_TABLE_RES:
//                break;
//        }
    }

    public void doClose(){
        this.loopHandler.pause();
        socketController.closeSocket();
    }

}