package config;

import controller.JsonHandler;
import controller.LogHandler;

import java.io.File;
import java.io.IOException;

public class ConfigManager {
    private ConfigPathLoader configPathLoader;
    private FrameConfig frameConfig;

    public ConfigManager() {
        configPathLoader = new ConfigPathLoader();
        configPathLoader.loadConfigPath();
        frameConfig = new FrameConfig(configPathLoader.frameConfigPath);
    }

    public FrameConfig getFrameConfig() {
        return frameConfig;
    }
}

class ConfigPathLoader {
    public String frameConfigPath;

    public ConfigPathLoader() {
    }

    public void loadConfigPath(){
        File file = new File("src/main/config_files/config_paths.json");
        if(file.isFile()){
            try {
                ConfigPathLoader configPathLoader = JsonHandler.mapper.readValue(file,ConfigPathLoader.class);
                this.frameConfigPath = configPathLoader.frameConfigPath;
            } catch (IOException e) {
                e.printStackTrace();
                LogHandler.logger.error("ConfigPaths could not be read");
            }
        }
        else{
            LogHandler.logger.error("ConfigPaths is not a file");
        }
    }

    public String getFrameConfigPath() {
        return frameConfigPath;
    }
}
