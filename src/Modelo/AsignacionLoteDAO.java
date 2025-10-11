/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/*
 * DAO: Asignación de lotes a técnicos (FK: ASIGNACION_LOTE.ID_TECNICO -> USUARIO.ID).
 * Provee:
 *  - asignarLoteAUsuario(username, idLote)
 *  - desasignarLoteDeUsuario(username, idLote)
 *  - desasignarTodoLote(idLote)
 *  - lotesVisiblesPara(sesion)
 *  - tecnicoTieneLote(username, idLote)
 *  - listarTecnicosUsername()  // para combos en la vista
 */
public class AsignacionLoteDAO {

    private boolean tableExists(Connection cn, String table) throws SQLException {
        DatabaseMetaData md = cn.getMetaData();
        try (ResultSet rs = md.getTables(null, cn.getSchema(), table.toUpperCase(), new String[]{"TABLE"})) {
            return rs.next();
        }
    }

    private Integer userIdByUsername(Connection cn, String username) throws SQLException {
        String q = "SELECT ID FROM USUARIO WHERE USERNAME=?";
        try (PreparedStatement ps = cn.prepareStatement(q)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return null;
    }

    public boolean tecnicoTieneLote(String usernameTecnico, String idLote) {
        try (Connection cn = ConexionBD.getConnection()) {
            if (!tableExists(cn, "ASIGNACION_LOTE")) {
                System.err.println("[AsignacionLoteDAO] AVISO: no existe ASIGNACION_LOTE, modo libre activado.");
                return true;
            }
            String sql = "SELECT 1 " +
                         "FROM ASIGNACION_LOTE a " +
                         "JOIN USUARIO u ON u.ID = a.ID_TECNICO " +
                         "WHERE u.USERNAME = ? AND a.ID_LOTE = ?";
            try (PreparedStatement ps = cn.prepareStatement(sql)) {
                ps.setString(1, usernameTecnico);
                ps.setString(2, idLote);
                try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
            }
        } catch (SQLException e) {
            throw new RuntimeException("[AsignacionLoteDAO.tecnicoTieneLote] " + e.getMessage(), e);
        }
    }

    public List<Lote> lotesVisiblesPara(Sesion sesion) {
        List<Lote> out = new ArrayList<>();
        try (Connection cn = ConexionBD.getConnection()) {
            PreparedStatement ps;
            if (!tableExists(cn, "ASIGNACION_LOTE")) {
                System.err.println("[AsignacionLoteDAO] AVISO: no existe ASIGNACION_LOTE, devolviendo todos los lotes.");
                ps = cn.prepareStatement("SELECT * FROM LOTE ORDER BY 1");
            } else if (sesion.getRol() == Rol.ADMIN) {
                ps = cn.prepareStatement("SELECT * FROM LOTE ORDER BY 1");
            } else {
                String sql = "SELECT l.* " +
                             "FROM LOTE l " +
                             "JOIN ASIGNACION_LOTE a ON a.ID_LOTE = l.ID_LOTE " +
                             "JOIN USUARIO u ON u.ID = a.ID_TECNICO " +
                             "WHERE u.USERNAME = ? ORDER BY 1";
                ps = cn.prepareStatement(sql);
                ps.setString(1, sesion.getUsername());
            }
            try (ps; ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String id = rs.getString("ID_LOTE");
                    String idCult = null, area = null;
                    try { idCult = rs.getString("ID_CULTIVO"); } catch (SQLException ignore) {}
                    try { area   = rs.getString("AREA"); } catch (SQLException ignore) {}
                    out.add(new Lote(id, idCult, area));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("[AsignacionLoteDAO.lotesVisiblesPara] " + e.getMessage(), e);
        }
        return out;
    }

    public boolean asignarLoteAUsuario(String username, String idLote) {
        String sql = "INSERT INTO ASIGNACION_LOTE (ID_TECNICO, ID_LOTE) VALUES (?, ?)";
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            Integer uid = userIdByUsername(cn, username);
            if (uid == null) throw new RuntimeException("Usuario no encontrado: " + username);
            ps.setInt(1, uid);
            ps.setString(2, idLote);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("[AsignacionLoteDAO.asignarLoteAUsuario] " + e.getMessage(), e);
        }
    }

    public boolean desasignarLoteDeUsuario(String username, String idLote) {
        String sql = "DELETE FROM ASIGNACION_LOTE WHERE ID_LOTE=? AND ID_TECNICO=(SELECT ID FROM USUARIO WHERE USERNAME=?)";
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, idLote);
            ps.setString(2, username);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("[AsignacionLoteDAO.desasignarLoteDeUsuario] " + e.getMessage(), e);
        }
    }

    public boolean desasignarTodoLote(String idLote) {
        String sql = "DELETE FROM ASIGNACION_LOTE WHERE ID_LOTE=?";
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, idLote);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("[AsignacionLoteDAO.desasignarTodoLote] " + e.getMessage(), e);
        }
    }

    /** Lista usernames de técnicos (USUARIO join ROL='TECNICO') para combos. */
    public List<String> listarTecnicosUsername() {
        List<String> out = new ArrayList<>();
        String sql = "SELECT u.USERNAME " +
                     "FROM USUARIO u JOIN ROL r ON r.ID = u.ROL_ID " +
                     "WHERE r.NOMBRE='TECNICO' ORDER BY u.USERNAME";
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(rs.getString(1));
        } catch (SQLException e) {
            throw new RuntimeException("[AsignacionLoteDAO.listarTecnicosUsername] " + e.getMessage(), e);
        }
        return out;
    }
}
