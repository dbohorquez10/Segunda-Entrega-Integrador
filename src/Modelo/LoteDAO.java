/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/*
 * Acceso a datos de LOTE.
 * - Admin crea y actualiza.
 * - Técnico solo actualiza (según control de permisos en el Controlador).
 */
public class LoteDAO {

    public boolean insertar(Lote l) {
        String sql = "INSERT INTO LOTE (ID_LOTE, ID_CULTIVO, AREA) VALUES (?, ?, ?)";
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, l.getIdLote());
            ps.setString(2, l.getIdCultivo());
            ps.setString(3, l.getArea());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("[LoteDAO.insertar] " + e.getMessage(), e);
        }
    }

    public boolean actualizar(Lote l) {
        String sql = "UPDATE LOTE SET ID_CULTIVO=?, AREA=? WHERE ID_LOTE=?";
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, l.getIdCultivo());
            ps.setString(2, l.getArea());
            ps.setString(3, l.getIdLote());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("[LoteDAO.actualizar] " + e.getMessage(), e);
        }
    }

    public boolean eliminar(String idLote) {
        String sql = "DELETE FROM LOTE WHERE ID_LOTE=?";
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, idLote);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("[LoteDAO.eliminar] " + e.getMessage(), e);
        }
    }

    public Lote buscarPorId(String idLote) {
        String sql = "SELECT * FROM LOTE WHERE ID_LOTE=?";
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, idLote);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String idCult = null, area = null;
                    try { idCult = rs.getString("ID_CULTIVO"); } catch (SQLException ignore) {}
                    try { area   = rs.getString("AREA"); } catch (SQLException ignore) {}
                    return new Lote(idLote, idCult, area);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("[LoteDAO.buscarPorId] " + e.getMessage(), e);
        }
        return null;
    }

    public List<Lote> listarTodos() {
        List<Lote> out = new ArrayList<>();
        String sql = "SELECT * FROM LOTE ORDER BY 1";
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String id = rs.getString("ID_LOTE");
                String idCult = null, area = null;
                try { idCult = rs.getString("ID_CULTIVO"); } catch (SQLException ignore) {}
                try { area   = rs.getString("AREA"); } catch (SQLException ignore) {}
                out.add(new Lote(id, idCult, area));
            }
        } catch (SQLException e) {
            throw new RuntimeException("[LoteDAO.listarTodos] " + e.getMessage(), e);
        }
        return out;
    }
}
