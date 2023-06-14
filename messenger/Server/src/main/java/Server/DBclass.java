package Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBclass {
    static Connection dbConnection;

    public static Connection getDbConnection() throws ClassNotFoundException, SQLException {
        if (dbConnection == null) {
            String connectionString = "jdbc:mysql://localhost:3306/messenger";
            dbConnection = DriverManager.getConnection(connectionString, "root", "qwerty1235");
        }
        return dbConnection;
    }

    public static void addNewMessege(String from, String to, String messege)
            throws ClassNotFoundException, SQLException {
        String insert = "INSERT INTO message ( user1, user2, text) VALUES(?,?,?)";
        PreparedStatement prSt = getDbConnection().prepareStatement(insert);
        prSt.setString(1, from);
        prSt.setString(2, to);
        prSt.setString(3, messege);
        prSt.executeUpdate();
    }

    public static void addUser(String login, String password) throws SQLException, ClassNotFoundException {
        String insert = "INSERT INTO user ( login, pasword) VALUES(?,?)";
        PreparedStatement prSt = getDbConnection().prepareStatement(insert);
        prSt.setString(1, login);
        prSt.setString(2, password);
        prSt.executeUpdate();
    }

    public static ResultSet getUser(String login, String password) throws ClassNotFoundException, SQLException {
        String select = "SELECT * FROM user WHERE login=? AND pasword=?";
        PreparedStatement prSt = getDbConnection().prepareStatement(select);
        prSt.setString(1, login);
        prSt.setString(2, password);
        return prSt.executeQuery();
    }

    public static ResultSet getUser(String login) throws ClassNotFoundException, SQLException {
        String select = "SELECT * FROM user WHERE login=?";
        PreparedStatement prSt = getDbConnection().prepareStatement(select);
        prSt.setString(1, login);
        return prSt.executeQuery();
    }

    public static ResultSet getAllMesseges(String login) throws ClassNotFoundException, SQLException {
        String select = "SELECT * FROM message WHERE user1 =? OR user2=?";
        PreparedStatement prSt = getDbConnection().prepareStatement(select);
        prSt.setString(1, login);
        prSt.setString(2, login);
        return prSt.executeQuery();
    }

    public static ResultSet getAllMesseges(String user1, String user2) throws ClassNotFoundException, SQLException {
        String select = "SELECT * FROM message WHERE (user1 =? AND user2=?) OR (user1 =? AND user2=?)";
        PreparedStatement prSt = getDbConnection().prepareStatement(select);
        prSt.setString(1, user1);
        prSt.setString(2, user2);
        prSt.setString(3, user2);
        prSt.setString(4, user1);
        return prSt.executeQuery();
    }

    public static ResultSet getAllMesseges() throws ClassNotFoundException, SQLException {
        String select = "SELECT * FROM message";
        PreparedStatement prSt = getDbConnection().prepareStatement(select);
        return prSt.executeQuery();
    }

    public static ResultSet getAllUser(String login) throws ClassNotFoundException, SQLException {
        String select = "SELECT * FROM user WHERE NOT login=?";
        PreparedStatement prSt = getDbConnection().prepareStatement(select);
        prSt.setString(1, login);
        return prSt.executeQuery();
    }
}
