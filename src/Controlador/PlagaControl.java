/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/*
 * Controlador de Plagas (sin UI).
 * Reglas:
 *  - ADMIN: crear, actualizar, eliminar.
 *  - TECNICO / PRODUCTOR: solo lectura.
 * Contrato:
 *  - Métodos CRUD lanzan IllegalArgumentException si faltan datos o no hay permisos.
 */

package controlador;

import modelo.Plaga;
import modelo.PlagaDAO;
import modelo.Rol;
import modelo.Sesion;

import java.util.List;

/*
 * Controlador de Plaga:
 *  - Valida permisos (solo ADMIN crea/actualiza/elimina).
 *  - guardar(): decide crear/actualizar según exista el ID en BD.
 */
public class PlagaControl {

    private final PlagaDAO dao = new PlagaDAO();

    public List<Plaga> listar() { return dao.listar(); }

    public String guardar(Sesion s, Plaga p) {
        if (s == null || s.getRol() != Rol.ADMIN) {
            throw new IllegalArgumentException("No tienes permiso para guardar plagas (se requiere ADMIN).");
        }
        boolean existe = dao.existe(p.getIdPlaga());
        boolean ok = existe ? dao.actualizar(p) : dao.insertar(p);
        if (!ok) throw new IllegalStateException("No se pudo " + (existe ? "actualizar" : "crear") + " la plaga.");
        return existe ? "ACTUALIZADO" : "CREADO";
    }

    public boolean eliminar(Sesion s, String id) {
        if (s == null || s.getRol() != Rol.ADMIN) {
            throw new IllegalArgumentException("No tienes permiso para eliminar plagas (se requiere ADMIN).");
        }
        return dao.eliminar(id);
    }
}
