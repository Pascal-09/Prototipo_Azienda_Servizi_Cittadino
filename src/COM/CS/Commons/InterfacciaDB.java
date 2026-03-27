package COM.CS.Commons;

import COM.CS.Entity.Utente;

import java.sql.*;
import java.util.ArrayList;

public class InterfacciaDB {
    private Connection con;

    public InterfacciaDB() {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            con  = DriverManager.getConnection("jdbc:mysql://localhost:3306/citizenservices","root","1234");
        } catch (ClassNotFoundException | SQLException ex){
            lostConnection();
        }
    }
    public Connection getConnection(){
        return con;
    }

    public void lostConnection(){
        ErroreDBMS errore = new ErroreDBMS();
        errore.setVisible(true);
    }
}
