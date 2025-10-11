/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/*
 * Controlador de Cultivos (sin UI).
 * Reglas:
 *  - ADMIN: crear, actualizar, eliminar.
 *  - TECNICO / PRODUCTOR: solo lectura.
 * Contrato:
 *  - Métodos CRUD lanzan IllegalArgumentException si faltan datos o no hay permisos.
 */

package controlador;

import modelo.Cultivo;
import modelo.CultivoDAO;
import modelo.Rol;
import modelo.Sesion;

import java.util.List;

/*
 * Controlador de Cultivo:
 *  - Valida permisos (solo ADMIN crea/actualiza/elimina).
 *  - guardar(): decide crear/actualizar según exista el ID en BD.
 */
public class CultivoControl {

    private final CultivoDAO dao = new CultivoDAO();

    public List<Cultivo> listar() { return dao.listar(); }

    public String guardar(Sesion s, Cultivo c) {
        if (s == null || s.getRol() != Rol.ADMIN) {
            throw new IllegalArgumentException("No tienes permiso para guardar cultivos (se requiere ADMIN).");
        }
        boolean existe = dao.existe(c.getIdCultivo());
        boolean ok = existe ? dao.actualizar(c) : dao.insertar(c);
        if (!ok) throw new IllegalStateException("No se pudo " + (existe ? "actualizar" : "crear") + " el cultivo.");
        return existe ? "ACTUALIZADO" : "CREADO";
    }

    public boolean eliminar(Sesion s, String id) {
        if (s == null || s.getRol() != Rol.ADMIN) {
            throw new IllegalArgumentException("No tienes permiso para eliminar cultivos (se requiere ADMIN).");
        }
        return dao.eliminar(id);
    }
}
