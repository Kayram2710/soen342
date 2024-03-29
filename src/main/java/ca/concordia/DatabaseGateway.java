package ca.concordia;

import java.sql.*;
import java.util.ArrayList;

public class DatabaseGateway {

    private Connection connection = null;
    private Statement statement = null;

    public DatabaseGateway(String path){
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(path);
            statement = connection.createStatement();
        } catch (ClassNotFoundException e) {
            System.err.println("Error loading SQLite JDBC driver: " + e.getMessage());
        }catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
        }
    }

    public void passStatement(String command){
        try {
            statement.executeUpdate(command);
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
        }
    }

    public void unmannagedPass(String command) throws SQLException {
        statement.executeUpdate(command);
    }

    public ArrayList<Object> runQuery(String command) {
        ArrayList<Object> result = new ArrayList<>();

        try {

            ResultSet resultSet = statement.executeQuery(command);
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    result.add(resultSet.getObject(i));
                }
            }

        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
        }

        return result;
    }

}
