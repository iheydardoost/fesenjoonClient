package ir.sharif.ap.controller;

import ir.sharif.ap.Main;
import ir.sharif.ap.model.Packet;
import ir.sharif.ap.model.PacketType;
import ir.sharif.ap.model.config.ClientSocketConfig;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;

public class SocketController implements Runnable{
    private Socket socket;
    private InputStream input;
    private OutputStream output;
    private LoopHandler loopHandler;
    private ClientSocketConfig clientSocketConfig;
    private final LinkedList<Packet> requests;
    private final LinkedList<Packet> responses;
    private int authToken;
    private boolean authTokenAvailable;
    private PacketHandler packetHandler;

    public SocketController() {
        requests = new LinkedList<>();
        responses = new LinkedList<>();
        authToken = 0;
        authTokenAvailable = false;
        packetHandler = new PacketHandler();
    }

    public void initConnection(boolean online) throws IOException {
        if(online) {
            clientSocketConfig = Main.getMainController().getConfigLoader().getClientSocketConfig();
        }else {
            try {
                clientSocketConfig = new ClientSocketConfig(InetAddress.getByName("localhost"),8000);
                LogHandler.logger.info("localhost / port:8000 selected");
            } catch (UnknownHostException e) {
                //e.printStackTrace();
                LogHandler.logger.error("could not connect offline via localhost and port 8000");
            }
        }
        InetSocketAddress socketAddress =
                new InetSocketAddress(clientSocketConfig.getIpAddress(), clientSocketConfig.getPort());

        if(this.socket!=null)
            socket.close();
        if(input!=null)
            input.close();
        if(output!=null)
            output.close();

        this.socket = new Socket();

        socket.connect(socketAddress,50);
        this.input = socket.getInputStream();
        this.output = socket.getOutputStream();

        loopHandler = new LoopHandler(600, this);
        //loopHandler.setNonStop(true);
    }

    @Override
    public void run() {
        try {
            synchronized (requests) {
                if (!requests.isEmpty()) {
                    Packet request = requests.removeFirst();
                    output.write(packetHandler.makePacketStr(request).getBytes(StandardCharsets.UTF_8));
                }
            }
        } catch (IOException e) {
            //e.printStackTrace();
            LogHandler.logger.error("network output stream could not work");
        }

        try {
            synchronized (responses) {
                if(input != null)
                if (input.available() > 0) {
                    String inStr = "";
                    char ch = '0';
                    while (ch != '$') {
                        ch = (char) input.read();
                        inStr += ch;
                    }
                    LogHandler.logger.info("inStrClient: " + inStr);
                    Packet response = packetHandler.parsePacket(inStr);
                    responses.add(response);
                }
            }
        } catch (IOException e){
            //e.printStackTrace();
            LogHandler.logger.error("network input stream could not work");
        }
    }

    public void addRequest(Packet rp){
        synchronized (requests) {
            if(rp != null)
                requests.add(rp);
        }
    }

    public Packet getResponse(){
        synchronized (responses) {
            if (responses.isEmpty())
                return null;
            else
                return responses.removeFirst();
        }
    }

    public void setAuthToken(int authToken){
        authTokenAvailable = true;
        this.authToken = authToken;
    }

    public int getAuthToken() {
        return authToken;
    }

    public boolean isAuthTokenAvailable() {
        return authTokenAvailable;
    }

    public void closeSocket(){
        Packet request = new Packet(
                PacketType.BYE,
                "",
                this.authToken,
                this.authTokenAvailable);
        try {
            output.write(packetHandler.makePacketStr(request).getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            //e.printStackTrace();
            LogHandler.logger.error("network output stream could not work");
        }
        this.loopHandler.pause();
        try {
            socket.close();
        } catch (IOException e) {
            //e.printStackTrace();
            LogHandler.logger.error("could not close the socket");
        }
    }

    public LoopHandler getLoopHandler() {
        return loopHandler;
    }
}