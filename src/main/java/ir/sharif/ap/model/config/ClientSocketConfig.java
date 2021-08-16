package ir.sharif.ap.model.config;

import java.net.InetAddress;

public class ClientSocketConfig {
    private InetAddress ipAddress;
    private int port;

    public ClientSocketConfig() {
    }

    public ClientSocketConfig(InetAddress ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public InetAddress getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(InetAddress ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
