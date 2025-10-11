/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/*
 * Clase de conexión a la base de datos Oracle.
 * MVC: pertenece a la capa "Modelo".
 *
 * Configuración:
 *  - Servidor: 192.168.254.215 (Base remota de la universidad)
 *  - Puerto: 1521
 *  - SID: orcl
 *  - Usuario: ADMINICA
 *  - Contraseña: adminica123
 *
 * Requisitos:
 *  - Librería ojdbc11.jar agregada al proyecto (en NetBeans -> Bibliotecas).
 *  - Conexión a la red de la universidad (VPN o LAN).
 *
 * Uso:
 *   Connection cn = ConexionBD.getConnection();
 *   PreparedStatement ps = cn.prepareStatement("SELECT * FROM USUARIO");
 */
package modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    // Datos de conexión actualizados al servidor de la universidad
    private static final String URL  = "jdbc:oracle:thin:@192.168.254.215:1521:orcl";
    private static final String USER = "ADMINICA";
    private static final String PASS = "adminica123";

    private static Connection conexion = null;

    public static Connection getConnection() {
        try {
            if (conexion == null || conexion.isClosed()) {
                try {
                    Class.forName("oracle.jdbc.driver.OracleDriver");
                } catch (ClassNotFoundException e) {
                    System.err.println("No se encontró el driver de Oracle (ojdbc11.jar).");
                }
                conexion = DriverManager.getConnection(URL, USER, PASS);
                System.out.println("Conectado correctamente a la base de datos de la universidad.");
            }
        } catch (SQLException e) {
            System.err.println("Error al conectar a la base de datos: " + e.getMessage());
            throw new RuntimeException("Error al conectar con la base de datos.", e);
        }
        return conexion;
    }

    public static void closeConnection() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                System.out.println("Conexión cerrada correctamente.");
            }
        } catch (SQLException e) {
            System.err.println("Error cerrando la conexión: " + e.getMessage());
        }
    }
}
