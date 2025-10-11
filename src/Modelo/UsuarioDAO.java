/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/*
 * DAO de USUARIO.
 * Responsabilidad:
 *  - Acceso a la tabla USUARIO y su FK a ROL.
 *  - Métodos mínimos para autenticación (buscarPorUsername) y gestión básica.
 *
 * Esquema esperado (ajusta si difiere):
 *  - USUARIO(ID, USERNAME, PASSWORD_HASH, NOMBRE, ROL_ID, ACTIVO)
 *  - ROL(ID, NOMBRE)  con valores: ADMIN, TECNICO, PRODUCTOR
*/

package modelo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    private static final String SQL_BASE =
        "SELECT u.ID, u.USERNAME, u.PASSWORD_HASH, u.NOMBRE, r.NOMBRE AS ROL_NOMBRE, u.ACTIVO " +
        "FROM USUARIO u JOIN ROL r ON u.ROL_ID = r.ID ";

    private Usuario map(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();
        u.setId(rs.getLong("ID"));
        u.setUsername(rs.getString("USERNAME"));
        u.setPasswordHash(rs.getString("PASSWORD_HASH"));
        u.setNombreCompleto(rs.getString("NOMBRE"));

        String rolNombre = rs.getString("ROL_NOMBRE");
        Rol rol;
        try {
            rol = Rol.valueOf(rolNombre); // ADMIN, TECNICO, PRODUCTOR
        } catch (Exception e) {
            // Si por alguna razón el nombre en BD no coincide, lo tratamos como PRODUCTOR por defecto
            rol = Rol.PRODUCTOR;
        }
        u.setRol(rol);

        String act = rs.getString("ACTIVO");
        u.setActivo(act != null && act.equalsIgnoreCase("S"));
        return u;
    }

    /*
     * Busca usuario por username (para login).
     * Retorna null si no existe.
     */
    public Usuario buscarPorUsername(String username) {
        String sql = SQL_BASE + "WHERE u.USERNAME = ?";
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error buscarPorUsername: " + e.getMessage());
        }
        return null;
    }

    /*
     * Lista todos los usuarios (para administración).
     */
    public List<Usuario> listar() {
        List<Usuario> lista = new ArrayList<>();
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(SQL_BASE + "ORDER BY u.ID");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(map(rs));
        } catch (SQLException e) {
            System.err.println("Error listar usuarios: " + e.getMessage());
        }
        return lista;
    }

    /*
     * Inserta un usuario.
     * - rol se resuelve vía subconsulta por nombre.
     * - activo: true -> 'S', false -> 'N'
     */
    public boolean insertar(Usuario u) {
        String sql =
            "INSERT INTO USUARIO (ID, USERNAME, PASSWORD_HASH, NOMBRE, ROL_ID, ACTIVO) " +
            "VALUES (USUARIO_SEQ.NEXTVAL, ?, ?, ?, (SELECT ID FROM ROL WHERE NOMBRE = ?), ?)";
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, u.getUsername());
            ps.setString(2, u.getPasswordHash());
            ps.setString(3, u.getNombreCompleto());
            ps.setString(4, u.getRol().name());
            ps.setString(5, u.isActivo() ? "S" : "N");
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error insertando usuario: " + e.getMessage());
            return false;
        }
    }

    /*
     * Actualiza datos del usuario (sin cambiar username).
     * - Para cambiar ROL, recibe u.getRol().
     * - passwordHash se actualiza tal cual (en MVP es texto plano).
     */
    public boolean actualizar(Usuario u) {
        String sql =
            "UPDATE USUARIO " +
            "SET PASSWORD_HASH = ?, NOMBRE = ?, ROL_ID = (SELECT ID FROM ROL WHERE NOMBRE = ?), ACTIVO = ? " +
            "WHERE ID = ?";
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, u.getPasswordHash());
            ps.setString(2, u.getNombreCompleto());
            ps.setString(3, u.getRol().name());
            ps.setString(4, u.isActivo() ? "S" : "N");
            ps.setLong(5, u.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error actualizando usuario: " + e.getMessage());
            return false;
        }
    }

    /*
     * Cambia el estado ACTIVO del usuario.
     */
    public boolean cambiarEstado(long idUsuario, boolean activo) {
        String sql = "UPDATE USUARIO SET ACTIVO = ? WHERE ID = ?";
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, activo ? "S" : "N");
            ps.setLong(2, idUsuario);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error cambiando estado de usuario: " + e.getMessage());
            return false;
        }
    }

    /*
     * Elimina usuario por ID.
     */
    public boolean eliminar(long idUsuario) {
        String sql = "DELETE FROM USUARIO WHERE ID = ?";
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setLong(1, idUsuario);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error eliminando usuario: " + e.getMessage());
            return false;
        }
    }
}
