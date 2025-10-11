/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package controlador;

import modelo.AsignacionLoteDAO;
import modelo.Lote;
import modelo.Rol;
import modelo.Sesion;

import java.util.List;

/*
 * Controla asignaciones de lotes a técnicos (ADMIN).
 */
public class AsignacionLoteControl {

    private final AsignacionLoteDAO dao = new AsignacionLoteDAO();

    public List<Lote> lotesVisibles(Sesion s) { return dao.lotesVisiblesPara(s); }

    public List<String> listarTecnicos() { return dao.listarTecnicosUsername(); }

    public boolean asignar(Sesion s, String usernameTecnico, String idLote) {
        if (s == null || s.getRol() != Rol.ADMIN)
            throw new IllegalArgumentException("Solo ADMIN puede asignar lotes.");
        return dao.asignarLoteAUsuario(usernameTecnico, idLote);
    }

    public boolean desasignar(Sesion s, String usernameTecnico, String idLote) {
        if (s == null || s.getRol() != Rol.ADMIN)
            throw new IllegalArgumentException("Solo ADMIN puede desasignar.");
        return dao.desasignarLoteDeUsuario(usernameTecnico, idLote);
    }

    public boolean desasignarTodo(Sesion s, String idLote) {
        if (s == null || s.getRol() != Rol.ADMIN)
            throw new IllegalArgumentException("Solo ADMIN puede desasignar todo.");
        return dao.desasignarTodoLote(idLote);
    }
}
