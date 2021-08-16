package ir.sharif.ap.model.config;

public class ConfigPaths {
    private String clientSocketConfigPath;

    public ConfigPaths() {
    }

    public ConfigPaths(String clientSocketConfigPath) {
        this.clientSocketConfigPath = clientSocketConfigPath;
    }

    public String getClientSocketConfigPath() {
        return clientSocketConfigPath;
    }

    public void setClientSocketConfigPath(String clientSocketConfigPath) {
        this.clientSocketConfigPath = clientSocketConfigPath;
    }
}
