package ir.sharif.ap.controller.offline;

import ir.sharif.ap.Main;
import ir.sharif.ap.controller.LogHandler;
import ir.sharif.ap.model.ChatType;
import ir.sharif.ap.model.LastSeenStatus;
import ir.sharif.ap.model.PacketType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class SettingController {

    public SettingController() {
    }

    public void handleSettingInfoReq(OfflinePacket rp){
        OfflineSocketController socketController = Main.getOfflineController().getOfflineSocketController();
        long userID = socketController.getClient(rp.getClientID()).getUserID();

        String query = "select * from \"User\" u where u.\"userID\" = " + userID;
        ResultSet rs = Main.getOfflineController().getDbCommunicator().executeQuery(query);

        LastSeenStatus lastSeenStatus;
        boolean accountPrivate;
        boolean accountActive;
        try {
            rs.next();
            lastSeenStatus = LastSeenStatus.values()[rs.getInt("lastSeenStatus")];
            accountPrivate = rs.getBoolean("accountPrivate");
            accountActive = rs.getBoolean("accountActive");
            socketController.getClient(rp.getClientID())
                    .addResponse(
                            new OfflinePacket(PacketType.SETTING_INFO_RES,
                                    "lastSeenStatus," + lastSeenStatus + ","
                                    + "accountPrivate," + accountPrivate + ","
                                    + "accountActive," + accountActive,
                                    rp.getAuthToken(),
                                    true,
                                    rp.getClientID(),
                                    rp.getRequestID())
                    );
        } catch (SQLException e) {
            //e.printStackTrace();
            LogHandler.logger.error("could not get result from DB");
        }
    }

    public void handleChangeSettingReq(OfflinePacket rp){
        String[] bodyArgs = rp.getBody().split(",",-1);
        String variable = bodyArgs[0];
        String value = OfflinePacketHandler.getDecodedArg(bodyArgs[1]);

        int updatedRowsNum = 0;
        OfflineSocketController socketController = Main.getOfflineController().getOfflineSocketController();
        long userID = socketController.getClient(rp.getClientID()).getUserID();
        String query = "";
        if(variable.equals("accountPrivate")) {
            query = "update \"User\" set \"accountPrivate\" = "
                    + value;
        }
        else if(variable.equals("accountActive")){
            query = "update \"User\" set \"accountActive\" = "
                    + value;
        }
        else if(variable.equals("lastSeenStatus")){
            query = "update \"User\" set \"lastSeenStatus\" = "
                    + LastSeenStatus.valueOf(value).ordinal();
        }
        else if(variable.equals("password")){
            socketController.getClient(rp.getClientID())
                    .addResponse(
                            new OfflinePacket(PacketType.CHANGE_SETTING_RES,
                                    "error,password",
                                    rp.getAuthToken(),
                                    true,
                                    rp.getClientID(),
                                    rp.getRequestID())
                    );
            return;
        }
        query += " where \"userID\" = " + userID;
        updatedRowsNum = Main.getOfflineController().getDbCommunicator().executeUpdate(query);

        String body = "";
        if(updatedRowsNum==1){
            body = "success," + variable;
            LogHandler.logger.info(variable + " changed to " + value + " (userID: " + userID + ")");
        }
        else if(updatedRowsNum==0){
            body = "error," + variable;
        }
        socketController.getClient(rp.getClientID())
                .addResponse(
                        new OfflinePacket(PacketType.CHANGE_SETTING_RES,
                                body,
                                rp.getAuthToken(),
                                true,
                                rp.getClientID(),
                                rp.getRequestID())
                );
    }

    public void handleLogoutReq(OfflinePacket rp){
        OfflineSocketController socketController = Main.getOfflineController().getOfflineSocketController();
        socketController.getClient(rp.getClientID())
                .setAuthToken(-1)
                .setUserID(0);

        socketController.getClient(rp.getClientID())
                .addResponse(
                        new OfflinePacket(PacketType.LOG_OUT_RES,
                                "success",
                                rp.getAuthToken(),
                                true,
                                rp.getClientID(),
                                rp.getRequestID())
                );
        LogHandler.logger.info(
                "userID:"
                + socketController.getClient(rp.getClientID()).getUserID()
                        + " logged out");
    }

    public static long findUserID(String userName){
        String query = "select \"userID\" from \"User\""
                + " where \"userName\" = '" + userName + "'";
        ResultSet rs = Main.getOfflineController().getDbCommunicator().executeQuery(query);
        try {
            if(rs.next())
                return rs.getLong("userID");
        } catch (SQLException e) {
            //e.printStackTrace();
        }
        return 0;
    }
}
