package sample;

import com.mysql.cj.jdbc.Driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DataBaseHandler extends Configs {
    
    Connection dbConnection;
    Driver driver;
    
    public Connection getDbConnection () throws ClassNotFoundException, SQLException {
        String connectionString = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName+
                "?autoReconnect=true&useSSL=false&useLegacyDatetimeCode=false&serverTimezone=UTC";
        driver = new com.mysql.cj.jdbc.Driver();
        DriverManager.registerDriver(driver);
        dbConnection = DriverManager.getConnection(connectionString, dbUser, dbPass);
        return dbConnection;
    }
    
    public void calcWrite(String calc){
        String insert="INSERT INTO "+Const.HISTORY_TABLE+" ("+Const.CALCULATION+") "+"VALUE(?)";
        
        try {PreparedStatement prSt = getDbConnection().prepareStatement(insert);
            prSt.setString(1,calc);
            prSt.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
