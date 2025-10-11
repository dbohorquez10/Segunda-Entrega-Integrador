/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/*
 * DAO para la entidad CULTIVO.
 * Permite CRUD completo: listar, insertar, actualizar y eliminar cultivos.
 * Solo el ADMIN puede crear o modificar registros.
 */

package modelo;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/*
 * DAO de CULTIVO:
 *  - listar() con fallback de nombres
 *  - existe(id) para decidir insert/update
 *  - insertar/actualizar con fallback de columnas (CICLO_CULTIVO o CICLO)
 */
public class CultivoDAO {

    // ---- util: obtener columna por cualquiera de los alias posibles
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

    public List<Cultivo> listar() {
        List<Cultivo> lista = new ArrayList<>();
        String sql = "SELECT * FROM CULTIVO ORDER BY 1";
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String id    = getCol(rs, "ID_CULTIVO","ID","IDCULTIVO","CODIGO");
                String cient = getCol(rs, "NOMBRE_CIENTIFICO","NOM_CIENTIFICO","NCIENTIFICO");
                String comun = getCol(rs, "NOMBRE_COMUN","NOM_COMUN","NCOMUN");
                String ciclo = getCol(rs, "CICLO_CULTIVO","CICLO");
                lista.add(new Cultivo(id, cient, comun, ciclo));
            }
        } catch (SQLException e) {
            throw new RuntimeException("[CultivoDAO.listar] " + e.getMessage(), e);
        }
        return lista;
    }

    public boolean existe(String id) {
        String sql = "SELECT 1 FROM CULTIVO WHERE ID_CULTIVO = ?";
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("[CultivoDAO.existe] " + e.getMessage(), e);
        }
    }

    public boolean insertar(Cultivo c) {
        String prefer = "INSERT INTO CULTIVO (ID_CULTIVO, NOMBRE_CIENTIFICO, NOMBRE_COMUN, CICLO_CULTIVO) VALUES (?,?,?,?)";
        String alt    = "INSERT INTO CULTIVO (ID_CULTIVO, NOMBRE_CIENTIFICO, NOMBRE_COMUN, CICLO) VALUES (?,?,?,?)";
        return exec(c, prefer, alt, true);
    }

    public boolean actualizar(Cultivo c) {
        String prefer = "UPDATE CULTIVO SET NOMBRE_CIENTIFICO=?, NOMBRE_COMUN=?, CICLO_CULTIVO=? WHERE ID_CULTIVO=?";
        String alt    = "UPDATE CULTIVO SET NOMBRE_CIENTIFICO=?, NOMBRE_COMUN=?, CICLO=? WHERE ID_CULTIVO=?";
        return exec(c, prefer, alt, false);
    }

    public boolean eliminar(String id) {
        String sql = "DELETE FROM CULTIVO WHERE ID_CULTIVO=?";
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("[CultivoDAO.eliminar] " + e.getMessage(), e);
        }
    }

    // ---- helper: ejecuta insert/update con fallback de columna
    private boolean exec(Cultivo c, String prefer, String alt, boolean insert) {
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(prefer)) {
            if (insert) {
                ps.setString(1, c.getIdCultivo());
                ps.setString(2, c.getNombreCientifico());
                ps.setString(3, c.getNombreComun());
                ps.setString(4, c.getCicloCultivo());
            } else {
                ps.setString(1, c.getNombreCientifico());
                ps.setString(2, c.getNombreComun());
                ps.setString(3, c.getCicloCultivo());
                ps.setString(4, c.getIdCultivo());
            }
            return ps.executeUpdate() > 0;
        } catch (SQLException e1) {
            // Si falla por columna inválida, reintenta con el SQL alterno
            if (e1.getMessage() != null && e1.getMessage().toUpperCase().contains("INVALID IDENTIFIER")) {
                try (Connection cn2 = ConexionBD.getConnection();
                     PreparedStatement ps2 = cn2.prepareStatement(alt)) {
                    if (insert) {
                        ps2.setString(1, c.getIdCultivo());
                        ps2.setString(2, c.getNombreCientifico());
                        ps2.setString(3, c.getNombreComun());
                        ps2.setString(4, c.getCicloCultivo());
                    } else {
                        ps2.setString(1, c.getNombreCientifico());
                        ps2.setString(2, c.getNombreComun());
                        ps2.setString(3, c.getCicloCultivo());
                        ps2.setString(4, c.getIdCultivo());
                    }
                    return ps2.executeUpdate() > 0;
                } catch (SQLException e2) {
                    throw new RuntimeException("[CultivoDAO.exec/fallback] " + e2.getMessage(), e2);
                }
            } else {
                throw new RuntimeException("[CultivoDAO.exec] " + e1.getMessage(), e1);
            }
        }
    }
}
