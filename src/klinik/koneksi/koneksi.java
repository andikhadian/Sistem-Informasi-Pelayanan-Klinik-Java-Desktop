/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package klinik.koneksi;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class koneksi {

    private static Connection mysqlKonek;

    public static Connection getDB() throws SQLException {
        if (mysqlKonek == null) {
            try {
                String url_DB = "jdbc:mysql://localhost:3306/klinik";
                String user = "root";
                String pw = "root";

                DriverManager.registerDriver(new com.mysql.jdbc.Driver());
                mysqlKonek = DriverManager.getConnection(url_DB, user, pw);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Tidak dapat terhubung ke Database", "Peringatan", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
        }
        return mysqlKonek;
    }
}
