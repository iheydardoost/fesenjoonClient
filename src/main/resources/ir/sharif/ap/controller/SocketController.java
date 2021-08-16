package controller;

import model.Packet;
import model.PacketType;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.Scanner;

public class SocketController implements Runnable{
    private final Socket socket;
    private InputStream input;
    private OutputStream output;
    private final LoopHandler loopHandler;
    private InetAddress ipAddress;
    private int port;
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

        getSocketConfig();
        InetSocketAddress socketAddress = new InetSocketAddress(ipAddress,port);
        this.socket = new Socket();
        try {
            socket.connect(socketAddress,5);
            this.input = socket.getInputStream();
            this.output = socket.getOutputStream();
        } catch (IOException e) {
            //e.printStackTrace();
            LogHandler.logger.error("could not connect to Server via address " + ipAddress.getHostAddress() + " and port " + port);
        }
        //Main.getMainFrame().showConnected(socket.isConnected());

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

    private void getSocketConfig(){
        String path = "src/main/config_files/client_socket_config.txt";
        File file = new File(path);
        if(file.isFile()) {
            try {
                Scanner scanner = new Scanner(file);
                String[] strings = scanner.nextLine().split(",");
                ipAddress = InetAddress.getByName(strings[0]);
                port = Integer.parseInt(strings[1]);
                LogHandler.logger.info(ipAddress.getHostAddress() + " / port:" + port + " selected.");
                return;
            } catch (FileNotFoundException | UnknownHostException e) {
                //e.printStackTrace();
                LogHandler.logger.error("client socket config file could not be used");
            }
        }
        try {
            ipAddress = InetAddress.getByName("localhost");
            port = 8000;
            LogHandler.logger.info("localhost / port:8000 selected.");
        } catch (UnknownHostException e) {
            //e.printStackTrace();
            LogHandler.logger.error("could not connect to Server via localhost " + " and port " + port);
        }
    }

    public void addRequest(Packet rp){
        synchronized (requests) {
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