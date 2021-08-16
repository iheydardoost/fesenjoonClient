package model;

public class Packet {
    private static int lastRequestID = 0;
    private int requestID;
    private int authToken;
    private boolean authTokenAvailable;
    private PacketType packetType;
    private String body;

    public Packet(PacketType packetType, String body, int authToken, boolean authTokenAvailable) {
        this.packetType = packetType;
        this.body = body;
        this.authToken = authToken;
        this.authTokenAvailable = authTokenAvailable;
        lastRequestID++;
        this.requestID = lastRequestID;
    }

    public Packet() {
    }

    public int getAuthToken() {
        return authToken;
    }

    public boolean isAuthTokenAvailable() {
        return authTokenAvailable;
    }

    public PacketType getPacketType() {
        return packetType;
    }

    public String getBody() {
        return body;
    }

    public int getRequestID() {
        return requestID;
    }
}