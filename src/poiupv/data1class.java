/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poiupv;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.Connection;
/**
 *
 * @author pocky
 */
public class data1class {
    public static Connection conectar(){
        try{
            String url= "jdbc:sqlite:lib/data.db";
            return DriverManager.getConnection(url);
        }catch (SQLException e) {
            System.out.println("Error de conexión: " + e.getMessage());
            return null;

}}
    
}
