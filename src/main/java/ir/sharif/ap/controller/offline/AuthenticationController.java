package ir.sharif.ap.controller.offline;

import ir.sharif.ap.Main;
import ir.sharif.ap.controller.LogHandler;
import ir.sharif.ap.model.PacketType;

import java.security.SecureRandom;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class AuthenticationController {
    private final SecureRandom SECURE_RANDOM = new SecureRandom();

    public AuthenticationController() {
    }

    public void handleLogIn(OfflinePacket rp){
//        System.out.println("in handle login");
        String[] bodyArgs = rp.getBody().split(",",-1);
        String userName = OfflinePacketHandler.getDecodedArg(bodyArgs[0]);
        int passwordHash = OfflinePacketHandler.getDecodedArg(bodyArgs[1]).hashCode();
        /**********************************************/
        String query = "select * from \"User\" u where u.\"userName\" = '" + userName + "'";
        ResultSet rs = Main.getOfflineController().getDbCommunicator().executeQuery(query);
        boolean userExisted = false;
        try {
            userExisted = rs.next();
        } catch (SQLException e) {
            //e.printStackTrace();
            LogHandler.logger.error("could not get result from DB");
        }
        /**********************************************/
        OfflineSocketController socketController = Main.getOfflineController().getOfflineSocketController();
        long userID = 0;
        try {
            if(userExisted){
//                System.out.println("user existed");
                userID = rs.getLong("userID");
                if(socketController.isUserOnline(userID)){
                    socketController.getClient(rp.getClientID())
                            .addResponse(
                                    new OfflinePacket(PacketType.LOG_IN_RES,
                                            "error,user is online now",
                                            0,
                                            false,
                                            rp.getClientID(),
                                            rp.getRequestID())
                            );
                    rs.close();
                    return;
                }
            }
            else {
                socketController.getClient(rp.getClientID())
                        .addResponse(
                                new OfflinePacket(PacketType.LOG_IN_RES,
                                        "error,userName does not exist",
                                        0,
                                        false,
                                        rp.getClientID(),
                                        rp.getRequestID())
                        );
                rs.close();
                return;
            }
        } catch (SQLException e) {
            //e.printStackTrace();
            LogHandler.logger.error("could not get result from DB");
        }
        /**********************************************/
        try {
            int dbPasswordHash = rs.getInt("passwordHash");
//            System.out.println(dbPasswordHash);
            if(dbPasswordHash == passwordHash){
                int newAuthToken = SECURE_RANDOM.nextInt(Integer.MAX_VALUE);
                socketController.getClient(rp.getClientID())
                        .setAuthToken(newAuthToken)
                        .setUserID(userID);
                socketController.getClient(rp.getClientID())
                        .addResponse(
                                new OfflinePacket(PacketType.LOG_IN_RES,
                                        "success,logged in offline mode",
                                        newAuthToken,
                                        true,
                                        rp.getClientID(),
                                        rp.getRequestID())
                        );
                LogHandler.logger.info("userID:" + userID + " logged in offline");
            }
            else{
//                System.out.println("pass incorrect");
                socketController.getClient(rp.getClientID())
                        .addResponse(
                                new OfflinePacket(PacketType.LOG_IN_RES,
                                        "error,password is incorrect",
                                        0,
                                        false,
                                        rp.getClientID(),
                                        rp.getRequestID())
                        );
            }
            rs.close();
        } catch (SQLException e) {
            //e.printStackTrace();
            LogHandler.logger.error("could not get result from DB");
        }
    }
}
