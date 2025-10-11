/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/*
 * DAO para la tabla de relación CULTIVO_PLAGA.
 * Permite verificar qué plagas afectan a cada cultivo.
 */

package modelo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/*
 * Relación cultivo-plaga. Asume tabla CULTIVO_PLAGA con ID_CULTIVO, ID_PLAGA.
 */
public class CultivoPlagaDAO {

    public List<String> plagasPermitidas(String idCultivo) {
        List<String> out = new ArrayList<>();
        String sql = "SELECT cp.ID_PLAGA FROM CULTIVO_PLAGA cp WHERE cp.ID_CULTIVO = ?";
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, idCultivo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(rs.getString(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("[CultivoPlagaDAO.plagasPermitidas] " + e.getMessage(), e);
        }
        return out;
    }

    public boolean existeRelacion(String idCultivo, String idPlaga) {
        String sql = "SELECT 1 FROM CULTIVO_PLAGA WHERE ID_CULTIVO=? AND ID_PLAGA=?";
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, idCultivo);
            ps.setString(2, idPlaga);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("[CultivoPlagaDAO.existeRelacion] " + e.getMessage(), e);
        }
    }
}
