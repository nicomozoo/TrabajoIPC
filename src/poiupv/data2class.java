/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poiupv;

import java.util.ArrayList;
import java.util.List;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;

/**
 *
 * @author pocky
 */
public class data2class {
    public static List<String> obtenerEnunciados() {
        List<String> ejs = new ArrayList<>();
        Connection conn = data1class.conectar();

        if (conn != null) {
            try {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT text FROM problem");

                while (rs.next()) {
                    ejs.add(rs.getString("text"));
                }

                rs.close();
                stmt.close();
                conn.close();
            } catch (Exception e) {
                System.out.println("Error al consultar: " + e.getMessage());
            }
        }
        return ejs;
      
    }
    
}
