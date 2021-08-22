package ir.sharif.ap.controller.offline;

import ir.sharif.ap.controller.LogHandler;

import java.io.ByteArrayInputStream;
import java.sql.*;

public class DBCommunicator {
    private Connection connection;
    private final Object CONNECTION_LOCK = new Object();
    private boolean connected;
    private String url = "jdbc:postgresql://localhost:5432/test2";
    private String user = "postgres";
    private String password = "admin";

    public DBCommunicator() {
    }

    public boolean connectDB() {
        synchronized (CONNECTION_LOCK) {
            try {
                this.connection =
                        DriverManager.getConnection(
                                url,
                                user,
                                password);
                connection.setAutoCommit(true);
                this.connected = true;
                LogHandler.logger.info("Connected to the postgreSQL database successfully");
            } catch (SQLException e) {
                //e.printStackTrace();
                this.connected = false;
                LogHandler.logger.error("could not connect to postgreSQL database");
            }
        }
        return connected;
    }

    public ResultSet executeQuery(String query) {
        if (connected && connection != null) {
            synchronized (CONNECTION_LOCK) {
                try {
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery(query);
                    statement.close();
                    return resultSet;
                } catch (SQLException e) {
                    //e.printStackTrace();
                    LogHandler.logger.error("SQLException in executeQuery: " + e.getMessage());
                }
            }
        }
        return null;
    }

    public boolean execute(String query) {
        if (connected && connection != null) {
            synchronized (CONNECTION_LOCK) {
                try {
                    Statement statement = connection.createStatement();
                    boolean result = statement.execute(query);
                    statement.close();
                    return result;
                } catch (SQLException e) {
                    //e.printStackTrace();
                    LogHandler.logger.error("SQLException in execute: " + e.getMessage());
                }
            }
        }
        return false;
    }

    public int executeUpdate(String query) {
        if (connected && connection != null) {
            synchronized (CONNECTION_LOCK) {
                try {
                    Statement statement = connection.createStatement();
                    int result = statement.executeUpdate(query);
                    statement.close();
                    return result;
                } catch (SQLException e) {
                    //e.printStackTrace();
                    LogHandler.logger.error("SQLException in executeUpdate: " + e.getMessage());
                }
            }
        }
        return -1;
    }

    public int executeUpdateBytea(String query, byte[] bytes){
        if (connected && connection != null) {
            synchronized (CONNECTION_LOCK) {
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setBinaryStream(1,new ByteArrayInputStream(bytes),bytes.length);
                    int result = preparedStatement.executeUpdate();
                    preparedStatement.close();
                    return result;
                } catch (SQLException e) {
                    //e.printStackTrace();
                    LogHandler.logger.error("SQLException in executeUpdate: " + e.getMessage());
                }
            }
        }
        return -1;
    }

    public void closeConnection() {
        if (connected && connection != null) {
            synchronized (CONNECTION_LOCK) {
                try {
                    connection.close();
                    connected = false;
                } catch (SQLException e) {
                    //e.printStackTrace();
                    LogHandler.logger.error("could not close dbConnection: " + e.getMessage());
                }
            }
        }
    }

    public boolean isConnectionValid() {
        if (connection != null) {
            synchronized (CONNECTION_LOCK) {
                try {
                    boolean result = connection.isValid(2);
                    return result;
                } catch (SQLException e) {
                    //e.printStackTrace();
                    LogHandler.logger.error("could not check dbConnection Validity");
                }
            }
        }
        return false;
    }
}
