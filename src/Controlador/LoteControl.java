/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package controlador;

import modelo.*;

import java.util.List;

/*
 * Controlador de Lote:
 *  - Admin: crear/actualizar/eliminar.
 *  - Técnico: actualizar (no crear/eliminar).
 */
public class LoteControl {

    private final LoteDAO dao = new LoteDAO();

    public List<Lote> listarTodos() { return dao.listarTodos(); }

    public boolean crear(Sesion s, Lote l) {
        if (s == null || s.getRol() != Rol.ADMIN)
            throw new IllegalArgumentException("Solo ADMIN puede crear lotes.");
        if (l == null || isBlank(l.getIdLote()))
            throw new IllegalArgumentException("ID de lote es obligatorio.");
        return dao.insertar(l);
    }

    public boolean actualizar(Sesion s, Lote l) {
        if (s == null) throw new IllegalArgumentException("Sesión inválida.");
        // Admin y Técnico pueden actualizar
        if (l == null || isBlank(l.getIdLote()))
            throw new IllegalArgumentException("ID de lote es obligatorio.");
        return dao.actualizar(l);
    }

    public boolean eliminar(Sesion s, String id) {
        if (s == null || s.getRol() != Rol.ADMIN)
            throw new IllegalArgumentException("Solo ADMIN puede eliminar lotes.");
        return dao.eliminar(id);
    }

    public Lote buscarPorId(String id) { return dao.buscarPorId(id); }

    private boolean isBlank(String v) { return v == null || v.trim().isEmpty(); }
}
