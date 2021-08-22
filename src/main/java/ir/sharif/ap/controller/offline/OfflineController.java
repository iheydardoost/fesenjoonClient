package ir.sharif.ap.controller.offline;

import ir.sharif.ap.Main;
import ir.sharif.ap.controller.*;
import ir.sharif.ap.model.LastSeenStatus;

import java.util.Base64;

public class OfflineController {
    private final OfflineSocketController offlineSocketController;
    private final ConfigLoader configLoader;
    private final DBCommunicator dbCommunicator;
    private final AuthenticationController authenticationController;
    private final SettingController settingController;
    private final PrivateController privateController;

    public OfflineController() {
        configLoader = new ConfigLoader();
        dbCommunicator = new DBCommunicator();
        authenticationController = new AuthenticationController();
        settingController = new SettingController();
        privateController = new PrivateController();

        offlineSocketController = new OfflineSocketController();
    }

    public OfflineSocketController getOfflineSocketController() {
        return offlineSocketController;
    }

    public DBCommunicator getDbCommunicator() {
        return dbCommunicator;
    }

    public void handleRequest(OfflinePacket rp){
        switch (rp.getPacketType()){
            case LOG_IN_REQ:
                authenticationController.handleLogIn(rp);
                break;
            case SETTING_INFO_REQ:
                settingController.handleSettingInfoReq(rp);
                break;
            case CHANGE_SETTING_REQ:
                settingController.handleChangeSettingReq(rp);
                break;
            case LOG_OUT_REQ:
                settingController.handleLogoutReq(rp);
                break;
            case GET_PRIVATE_INFO_REQ:
                privateController.handleGetPrivateInfo(rp);
                break;
            default:
                break;
        }
    }

    public static void saveUserData(String response){
        String[] args = response.split(",",-1);

        String query = "insert into \"User\""
                + " ("
                + "\"userID\"" + ","
                + "\"firstName\"" + ","
                + "\"lastName\"" + ","
                + "\"userName\"" + ","
                + "\"passwordHash\"" + ","
                + "\"dateOfBirth\"" + ","
                + "\"email\"" + ","
                + "\"phoneNumber\"" + ","
                + "\"bio\"" + ","
                + "\"lastSeenStatus\"" + ","
                + "\"lastSeen\"" + ","
                + "\"accountPrivate\"" + ","
                + "\"accountActive\""
                + ") "
                + "values"
                + " ("
                + args[0] + ","
                + "'" + PacketHandler.getDecodedArg(args[1]) + "'" + ","
                + "'" + PacketHandler.getDecodedArg(args[2]) + "'" + ","
                + "'" + PacketHandler.getDecodedArg(args[3]) + "'" + ","
                + args[4] + ",";
        if(args[5].isEmpty())
            query += ( "null,");
        else
            query += ("'" + args[5] + "'" + ",");
        query += ( "'" + PacketHandler.getDecodedArg(args[6]) + "'" + ","
                + "'" + PacketHandler.getDecodedArg(args[7]) + "'" + ","
                + "'" + PacketHandler.getDecodedArg(args[8]) + "'" + ","
                + LastSeenStatus.valueOf(args[9]).ordinal() + ","
                + "null" + ","
                + args[10] + ","
                + args[11]
                + ")");
        int updatedRowsNum = Main.getOfflineController().getDbCommunicator().executeUpdate(query);

        if(!args[12].isEmpty()) {
            byte[] userImage = Base64.getDecoder().decode(args[12]);
            query = "update \"User\" set \"userImage\" = ? where \"userID\" = " + args[0];
            updatedRowsNum = Main.getOfflineController().getDbCommunicator().executeUpdateBytea(query, userImage);
        }
    }
}
