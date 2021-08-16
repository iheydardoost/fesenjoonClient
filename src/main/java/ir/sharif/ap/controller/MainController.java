package ir.sharif.ap.controller;

import ir.sharif.ap.Presenter.AuthFormEvent;
import ir.sharif.ap.model.Packet;
import ir.sharif.ap.model.PacketType;

public class MainController implements Runnable{
    private LoopHandler loopHandler;
    private SocketController socketController;
    private ConfigLoader configLoader;

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
                System.out.println("login response body: " + response.getBody());
                break;
            case SIGN_IP_RES:
                System.out.println("signup response body: " + response.getBody());
                break;
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

    public void doClose(){
        this.loopHandler.pause();
        socketController.closeSocket();
    }

    public ConfigLoader getConfigLoader() {
        return configLoader;
    }
}