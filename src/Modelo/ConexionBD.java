/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/*
 * Clase de conexión a la base de datos Oracle XE.
 * Se usa un Singleton para compartir la conexión entre todos los DAO.
 * Ajusta las credenciales según tu entorno local.
 */

package modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    private static final String URL = "jdbc:oracle:thin:@localhost:1521:xe";
    private static final String USER = "ADMINICA";
    private static final String PASS = "adminica123";

    private static Connection conexion = null;

    private ConexionBD() {}

   public static Connection getConnection() {
    try {
        if (conexion == null || conexion.isClosed()) {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conexion = DriverManager.getConnection(URL, USER, PASS);
            conexion.setAutoCommit(true);
            System.out.println("Conexión establecida con Oracle.");
        }
    } catch (ClassNotFoundException e) {
        throw new RuntimeException("Driver Oracle no encontrado: " + e.getMessage(), e);
    } catch (SQLException e) {
        throw new RuntimeException("Error conectando a Oracle: " + e.getMessage(), e);
    }
    return conexion;
}
}









