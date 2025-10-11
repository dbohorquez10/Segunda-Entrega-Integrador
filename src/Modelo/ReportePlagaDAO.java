/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.sql.*;

/*
 * Acceso a datos de REPORTE_PLAGA.
 *
 * Reglas de inserción:
 *  - Guarda el autor del reporte enlazado a USUARIO, buscando por USERNAME.
 *  - Intenta primero con la columna ID_USUARIO (FK a USUARIO.ID).
 *  - Si tu tabla usa otra (ID_TECNICO / ID_ASISTENTE), reintenta automáticamente.
 *
 * Columnas con fallback:
 *  - PORCENTAJE  <-> PORCENTAJE_INFESTACION
 *  - OBSERVACIONES <-> OBSERVACION
 *
 * Campos esperados en ReportePlaga (modelo):
 *  - idReporte (String)
 *  - idLote (String)
 *  - idPlaga (String)
 *  - fechaReporte (java.sql.Date)
 *  - severidad (String)
 *  - porcentaje (Double)  // puede ser null
 *  - observaciones (String)
 */
public class ReportePlagaDAO {

    // Inserta el reporte. idUsuarioUsername es el username del usuario que reporta.
    public boolean insertar(ReportePlaga r, String idUsuarioUsername) {
        // Preferimos guardar el autor por ID_USUARIO (FK a USUARIO.ID)
        final String sqlPrefer = ""
                + "INSERT INTO REPORTE_PLAGA "
                + "(ID_REPORTE, ID_LOTE, ID_PLAGA, FECHA_REPORTE, SEVERIDAD, PORCENTAJE, OBSERVACIONES, ID_USUARIO) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, (SELECT ID FROM USUARIO WHERE USERNAME=?))";

        // Fallback 1: PORCENTAJE_INFESTACION + OBSERVACION + ID_USUARIO
        final String sqlAlt1 = ""
                + "INSERT INTO REPORTE_PLAGA "
                + "(ID_REPORTE, ID_LOTE, ID_PLAGA, FECHA_REPORTE, SEVERIDAD, PORCENTAJE_INFESTACION, OBSERVACION, ID_USUARIO) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, (SELECT ID FROM USUARIO WHERE USERNAME=?))";

        // Fallback 2: usar ID_TECNICO (si tu tabla guarda el autor así)
        final String sqlAlt2 = ""
                + "INSERT INTO REPORTE_PLAGA "
                + "(ID_REPORTE, ID_LOTE, ID_PLAGA, FECHA_REPORTE, SEVERIDAD, PORCENTAJE, OBSERVACIONES, ID_TECNICO) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, (SELECT ID FROM USUARIO WHERE USERNAME=?))";

        // Fallback 3: usar ID_ASISTENTE (si tu tabla guarda el autor así)
        final String sqlAlt3 = ""
                + "INSERT INTO REPORTE_PLAGA "
                + "(ID_REPORTE, ID_LOTE, ID_PLAGA, FECHA_REPORTE, SEVERIDAD, PORCENTAJE, OBSERVACIONES, ID_ASISTENTE) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, (SELECT ID FROM USUARIO WHERE USERNAME=?))";

        // Intentamos en cascada hasta que alguna funcione
        if (execInsert(sqlPrefer, r, idUsuarioUsername)) return true;
        if (execInsert(sqlAlt1, r, idUsuarioUsername)) return true;
        if (execInsert(sqlAlt2, r, idUsuarioUsername)) return true;
        return execInsert(sqlAlt3, r, idUsuarioUsername);
    }

    // ---------- helpers ----------

    private boolean execInsert(String sql, ReportePlaga r, String username) {
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            // Map común de parámetros según orden (ve las variantes arriba)
            // Todos inician igual:
            ps.setString(1, r.getIdReporte());
            ps.setString(2, r.getIdLote());
            ps.setString(3, r.getIdPlaga());
            ps.setDate(4, r.getFechaReporte());
            ps.setString(5, r.getSeveridad());

            // Posición 6: porcentaje (puede caer en PORCENTAJE o PORCENTAJE_INFESTACION)
            if (r.getPorcentaje() == null) {
                ps.setNull(6, java.sql.Types.NUMERIC);
            } else {
                ps.setDouble(6, r.getPorcentaje());
            }

            // Posición 7: observación/observaciones (string, puede ser null)
            if (r.getObservaciones() == null || r.getObservaciones().isBlank()) {
                ps.setNull(7, java.sql.Types.VARCHAR);
            } else {
                ps.setString(7, r.getObservaciones());
            }

            // Posición 8: username para buscar el ID en USUARIO
            ps.setString(8, username);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            // Si el error es por columna inválida, devolvemos false para que el llamador pruebe otro SQL.
            String msg = e.getMessage() != null ? e.getMessage().toUpperCase() : "";
            if (msg.contains("INVALID IDENTIFIER") || msg.contains("ORA-00904")) {
                System.err.println("[ReportePlagaDAO] Reintentando con SQL alterno por columnas diferentes...");
                return false;
            }
            // Otros errores deben escalarse para verlos en UI.
            throw new RuntimeException("[ReportePlagaDAO.insertar] " + e.getMessage(), e);
        }
    }
}
