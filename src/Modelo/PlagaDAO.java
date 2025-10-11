/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/*
 * DAO para la entidad PLAGA.
 * CRUD completo; solo el ADMIN puede modificar.
 */

package modelo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/*
 * DAO de PLAGA:
 *  - listar() con fallback de nombres
 *  - existe(id) para decidir insert/update
 *  - insertar/actualizar con fallback de columnas (TIPO_PLAGA o TIPO)
 */
public class PlagaDAO {

    private String getCol(ResultSet rs, String... opciones) throws SQLException {
        ResultSetMetaData md = rs.getMetaData();
        for (int i = 1; i <= md.getColumnCount(); i++) {
            String label = md.getColumnLabel(i);
            String name  = md.getColumnName(i);
            for (String op : opciones) {
                if (op.equalsIgnoreCase(label) || op.equalsIgnoreCase(name)) {
                    return rs.getString(i);
                }
            }
        }
        return null;
    }

    public List<Plaga> listar() {
        List<Plaga> lista = new ArrayList<>();
        String sql = "SELECT * FROM PLAGA ORDER BY 1";
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String id    = getCol(rs, "ID_PLAGA","ID","IDPLAGA","CODIGO");
                String cient = getCol(rs, "NOMBRE_CIENTIFICO","NOM_CIENTIFICO","NCIENTIFICO");
                String comun = getCol(rs, "NOMBRE_COMUN","NOM_COMUN","NCOMUN");
                String tipo  = getCol(rs, "TIPO_PLAGA","TIPO");
                lista.add(new Plaga(id, cient, comun, tipo));
            }
        } catch (SQLException e) {
            throw new RuntimeException("[PlagaDAO.listar] " + e.getMessage(), e);
        }
        return lista;
    }

    public boolean existe(String id) {
        String sql = "SELECT 1 FROM PLAGA WHERE ID_PLAGA = ?";
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("[PlagaDAO.existe] " + e.getMessage(), e);
        }
    }

    public boolean insertar(Plaga p) {
        String prefer = "INSERT INTO PLAGA (ID_PLAGA, NOMBRE_CIENTIFICO, NOMBRE_COMUN, TIPO_PLAGA) VALUES (?,?,?,?)";
        String alt    = "INSERT INTO PLAGA (ID_PLAGA, NOMBRE_CIENTIFICO, NOMBRE_COMUN, TIPO) VALUES (?,?,?,?)";
        return exec(p, prefer, alt, true);
    }

    public boolean actualizar(Plaga p) {
        String prefer = "UPDATE PLAGA SET NOMBRE_CIENTIFICO=?, NOMBRE_COMUN=?, TIPO_PLAGA=? WHERE ID_PLAGA=?";
        String alt    = "UPDATE PLAGA SET NOMBRE_CIENTIFICO=?, NOMBRE_COMUN=?, TIPO=? WHERE ID_PLAGA=?";
        return exec(p, prefer, alt, false);
    }

    public boolean eliminar(String id) {
        String sql = "DELETE FROM PLAGA WHERE ID_PLAGA=?";
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("[PlagaDAO.eliminar] " + e.getMessage(), e);
        }
    }

    private boolean exec(Plaga p, String prefer, String alt, boolean insert) {
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(prefer)) {
            if (insert) {
                ps.setString(1, p.getIdPlaga());
                ps.setString(2, p.getNombreCientifico());
                ps.setString(3, p.getNombreComun());
                ps.setString(4, p.getTipoPlaga());
            } else {
                ps.setString(1, p.getNombreCientifico());
                ps.setString(2, p.getNombreComun());
                ps.setString(3, p.getTipoPlaga());
                ps.setString(4, p.getIdPlaga());
            }
            return ps.executeUpdate() > 0;
        } catch (SQLException e1) {
            if (e1.getMessage() != null && e1.getMessage().toUpperCase().contains("INVALID IDENTIFIER")) {
                try (Connection cn2 = ConexionBD.getConnection();
                     PreparedStatement ps2 = cn2.prepareStatement(alt)) {
                    if (insert) {
                        ps2.setString(1, p.getIdPlaga());
                        ps2.setString(2, p.getNombreCientifico());
                        ps2.setString(3, p.getNombreComun());
                        ps2.setString(4, p.getTipoPlaga());
                    } else {
                        ps2.setString(1, p.getNombreCientifico());
                        ps2.setString(2, p.getNombreComun());
                        ps2.setString(3, p.getTipoPlaga());
                        ps2.setString(4, p.getIdPlaga());
                    }
                    return ps2.executeUpdate() > 0;
                } catch (SQLException e2) {
                    throw new RuntimeException("[PlagaDAO.exec/fallback] " + e2.getMessage(), e2);
                }
            } else {
                throw new RuntimeException("[PlagaDAO.exec] " + e1.getMessage(), e1);
            }
        }
    }
}
