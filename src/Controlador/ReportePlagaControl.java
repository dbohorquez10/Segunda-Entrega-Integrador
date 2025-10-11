/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import modelo.*;

/*
 * Reglas de negocio para Reporte de Plaga.
 *
 * Flujo crear reporte:
 *  1) Validar rol: solo ADMIN o TECNICO pueden crear reportes.
 *  2) Si es TECNICO: validar que el lote esté asignado a su usuario.
 *  3) Obtener el cultivo del lote y validar que la plaga esté asociada a ese cultivo (CULTIVO_PLAGA).
 *  4) Insertar el reporte, guardando el autor por USERNAME → USUARIO.ID (en el DAO).
 */
public class ReportePlagaControl {

    private final ReportePlagaDAO dao = new ReportePlagaDAO();
    private final AsignacionLoteDAO asignacionDAO = new AsignacionLoteDAO();
    private final CultivoPlagaDAO cultivoPlagaDAO = new CultivoPlagaDAO();
    private final LoteDAO loteDAO = new LoteDAO();

    /** Crea el reporte aplicando todas las validaciones. */
    public boolean crear(Sesion sesion, ReportePlaga r) {
        if (sesion == null || !(sesion.getRol() == Rol.ADMIN || sesion.getRol() == Rol.TECNICO)) {
            throw new IllegalArgumentException("No tienes permiso para crear reportes (se requiere ADMIN o TECNICO).");
        }

        if (r == null) {
            throw new IllegalArgumentException("Datos del reporte inválidos.");
        }
        if (isBlank(r.getIdLote()))  throw new IllegalArgumentException("Debes seleccionar un Lote.");
        if (isBlank(r.getIdPlaga())) throw new IllegalArgumentException("Debes seleccionar una Plaga.");
        if (isBlank(r.getIdReporte())) {
            // Si tu tabla genera ID por secuencia/trigger, este check se puede quitar y dejar null.
            throw new IllegalArgumentException("ID de reporte obligatorio (o configura secuencia/trigger).");
        }

        // 1) Si es técnico, el lote debe estar asignado a su usuario
        if (sesion.getRol() == Rol.TECNICO) {
            boolean asignado = asignacionDAO.tecnicoTieneLote(sesion.getUsername(), r.getIdLote());
            if (!asignado) {
                throw new IllegalArgumentException("Este lote no está asignado al técnico actual.");
            }
        }

        // 2) Obtener cultivo del lote
        Lote lote = loteDAO.buscarPorId(r.getIdLote());
        if (lote == null || isBlank(lote.getIdCultivo())) {
            throw new IllegalArgumentException("No se encontró el cultivo asociado al lote seleccionado.");
        }
        String idCultivo = lote.getIdCultivo();

        // 3) Validar relación cultivo–plaga
        boolean okRelacion = cultivoPlagaDAO.existeRelacion(idCultivo, r.getIdPlaga());
        if (!okRelacion) {
            throw new IllegalArgumentException("La plaga no está asociada al cultivo del lote.");
        }

        // 4) Insertar (el DAO transforma USERNAME → USUARIO.ID internamente)
        return dao.insertar(r, sesion.getUsername());
    }

    /** Lotes visibles según rol (ADMIN: todos; TECNICO: solo asignados) */
    public java.util.List<Lote> lotesVisibles(Sesion sesion) {
        return asignacionDAO.lotesVisiblesPara(sesion);
    }

    private boolean isBlank(String s) { return s == null || s.trim().isEmpty(); }
}
