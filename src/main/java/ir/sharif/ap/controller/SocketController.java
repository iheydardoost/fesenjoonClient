package ir.sharif.ap.controller;

import ir.sharif.ap.Main;
import ir.sharif.ap.model.Packet;
import ir.sharif.ap.model.PacketType;
import ir.sharif.ap.model.config.ClientSocketConfig;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;

public class SocketController implements Runnable{
    private final Socket socket;
    private InputStream input;
    private OutputStream output;
    private final LoopHandler loopHandler;
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

        clientSocketConfig = Main.getMainController().getConfigLoader().getClientSocketConfig();
        InetSocketAddress socketAddress =
                new InetSocketAddress(clientSocketConfig.getIpAddress(),clientSocketConfig.getPort());
        this.socket = new Socket();
        try {
            socket.connect(socketAddress,5);
            this.input = socket.getInputStream();
            this.output = socket.getOutputStream();
        } catch (IOException e) {
            //e.printStackTrace();
            LogHandler.logger.error("could not connect to Server via address "
                    + clientSocketConfig.getIpAddress().getHostAddress()
                    + " and port " + clientSocketConfig.getPort());
        }

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
        Packet request = new Packet(PacketType.BYE,"",this.authToken,this.authTokenAvailable);
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
}