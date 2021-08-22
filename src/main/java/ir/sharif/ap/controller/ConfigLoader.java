package ir.sharif.ap.controller;

import ir.sharif.ap.model.config.ClientSocketConfig;
import ir.sharif.ap.model.config.ConfigPaths;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class ConfigLoader {
    private ConfigPaths configPaths;
    private final String CONFIG_PATHS = "./src/main/config_files/config_paths.json";

    public ConfigLoader() {
        File file = new File(CONFIG_PATHS);
        if(file.isFile()){
            try {
                configPaths = JsonHandler.mapper.readValue(file,ConfigPaths.class);
            } catch (IOException e) {
                //e.printStackTrace();
                LogHandler.logger.fatal("could not read config_paths.json");
            }
        }
        else{
            LogHandler.logger.fatal("config_paths.json does not exist");
        }
    }

    public ClientSocketConfig getClientSocketConfig(){
        ClientSocketConfig clientSocketConfig;
        String path = configPaths.getClientSocketConfigPath();
        File file = new File(path);
        if(file.isFile()) {
            try {
                clientSocketConfig = JsonHandler.mapper.readValue(file,ClientSocketConfig.class);
                LogHandler.logger.info(clientSocketConfig.getIpAddress().getHostAddress()
                        + " / port:" + clientSocketConfig.getPort() + " selected");
                return clientSocketConfig;
            }catch (IOException e) {
                //e.printStackTrace();
                LogHandler.logger.error("could not read client_socket_config.json");
            }
        }
        else {
            LogHandler.logger.error("client_socket_config.json does not exist");
        }

        return null;
    }
}
