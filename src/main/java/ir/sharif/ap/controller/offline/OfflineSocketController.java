package ir.sharif.ap.controller.offline;

import ir.sharif.ap.controller.LogHandler;
import ir.sharif.ap.controller.LoopHandler;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Random;

public class OfflineSocketController implements Runnable{
    private ServerSocket serverSocket;
    private LoopHandler loopHandler;
    private final LinkedList<ClientHandler> clients;
    private static final Random random = new Random();

    public OfflineSocketController() {
        clients = new LinkedList<>();
    }

    public void initConnection() {
        try {
            this.serverSocket =
                    new ServerSocket(8000,50, InetAddress.getByName("localhost"));
            LogHandler.logger.info("offline socket started on localhost:8000");
        } catch (IOException e) {
            //e.printStackTrace();
        }
        loopHandler = new LoopHandler(600, this);
    }

    @Override
    public void run() {
        Socket socket = null;
        try {
            socket = serverSocket.accept();
            ClientHandler clt = new ClientHandler(socket, random.nextInt());
            synchronized (clients) {
                clients.add(clt);
            }
            LogHandler.logger.info("accepted client connection request, clientID:"
                    + clt.getClientID()
                    + ", userID:"
                    + clt.getUserID());
        } catch (IOException e) {
            //e.printStackTrace();
            LogHandler.logger.error("could not accept the client connection request");
        }
    }

    public ClientHandler getClient(int clientID){
        synchronized (clients) {
            for (ClientHandler client : clients) {
                if (client.getClientID() == clientID)
                    return client;
            }
            return null;
        }
    }

    public ClientHandler getClientByAuth(int authToken){
        synchronized (clients) {
            for (ClientHandler client : clients) {
                if (client.getAuthToken() == authToken)
                    return client;
            }
            return null;
        }
    }

    public ClientHandler getClientByID(long userID){
        synchronized (clients) {
            for (ClientHandler client : clients) {
                if (client.getUserID() == userID)
                    return client;
            }
            return null;
        }
    }

    public void removeClient(ClientHandler clt){
        clients.remove(clt);
    }

    public boolean isUserOnline(long userID){
        synchronized (clients) {
            for (ClientHandler ch : clients) {
                if (ch.getUserID() == userID)
                    return true;
            }
        }
        return false;
    }

    public void handleWantUpdateChatReq(OfflinePacket rp){
        ClientHandler clt = this.getClient(rp.getClientID());
        clt.setWantToUpdateChat(Boolean.parseBoolean(rp.getBody()));
    }

    public void handleWantUpdateChatroomReq(OfflinePacket rp){
        ClientHandler clt = this.getClient(rp.getClientID());
        clt.setWantToUpdateChatroom(Boolean.parseBoolean(rp.getBody()));
    }

    public void doClose(){
        for (ClientHandler ch: clients) {
            ch.getLoopHandler().pause();
        }
        this.loopHandler.pause();
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }
}
