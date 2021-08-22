package ir.sharif.ap.controller.offline;

import ir.sharif.ap.Main;
import ir.sharif.ap.controller.LogHandler;
import ir.sharif.ap.model.PacketType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Base64;

public class PrivateController {

    public void handleGetPrivateInfo(OfflinePacket rp){
        OfflineSocketController socketController = Main.getOfflineController().getOfflineSocketController();
        long userID = socketController.getClient(rp.getClientID()).getUserID();

        String query = "select * from \"User\" u"
                + " where u.\"userID\" = " + userID;
        ResultSet rs = Main.getOfflineController().getDbCommunicator().executeQuery(query);
        /************************************************************/
        ClientHandler clt = socketController.getClient(rp.getClientID());
        try {
            if(rs.next()) {
                String userImageStr = "";
                byte[] userImage = rs.getBytes("userImage");
                if(userImage!=null)
                    userImageStr = Base64.getEncoder().encodeToString(userImage);
                String dateOfBirth = "";
                if(rs.getDate("dateOfBirth")!=null)
                    dateOfBirth = rs.getDate("dateOfBirth").toString();
                String body = OfflinePacketHandler.makeEncodedArg(rs.getString("userName")) + ","
                        + OfflinePacketHandler.makeEncodedArg(rs.getString("firstName")) + ","
                        + OfflinePacketHandler.makeEncodedArg(rs.getString("lastName")) + ","
                        + dateOfBirth + ","
                        + OfflinePacketHandler.makeEncodedArg(rs.getString("email")) + ","
                        + OfflinePacketHandler.makeEncodedArg(rs.getString("phoneNumber")) + ","
                        + userImageStr + ","
                        + OfflinePacketHandler.makeEncodedArg(rs.getString("bio"));
                clt.addResponse(
                        new OfflinePacket(PacketType.GET_PRIVATE_INFO_RES,
                                body,
                                clt.getAuthToken(),
                                true,
                                rp.getClientID(),
                                rp.getRequestID())
                );
            }
        } catch (SQLException e) {
            //e.printStackTrace();
            LogHandler.logger.error("could not get result from DB");
        }
    }
}
