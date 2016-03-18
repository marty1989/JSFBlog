/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author c0564747
 */
public class Utils {
    public final static String SALT = "ThisARandomStringOfCharacters";
    
    public static String hash(String password) throws UnsupportedEncodingException, NoSuchAlgorithmException{
        String salted = password + SALT;
        MessageDigest md = MessageDigest.getInstance("SHA1");
        byte[] hash = md.digest(salted.getBytes("UTF-8"));
        StringBuilder stringBuilder = new StringBuilder();
        for(byte b : hash){
            String hex = Integer.toHexString(b & 0xff).toUpperCase();
            if(hex.length() == 1){
                stringBuilder.append("0");
            }
            stringBuilder.append(hex);
        }
        return stringBuilder.toString();
   }
    
    public static Connection getConnection() throws SQLException{
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        
        String host = "localhost";
        String port = "3306";
        String user = "blog";
        String db = "blog";
        String pass = "password";
        String jdbc = "jdbc:mysql://" + host + ":" + port + "/" + db; 
        return DriverManager.getConnection(jdbc, user, pass);
    }
}
