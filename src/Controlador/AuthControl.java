/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/*
 * Controlador de autenticación (sin UI).
 * Responsabilidad:
 *  - Validar credenciales contra la BD usando UsuarioDAO.
 *  - Construir y devolver una Sesion con el rol real.
 * Contrato:
 *  - Retorna Sesion si es válido.
 *  - Lanza IllegalArgumentException en errores de validación/autenticación.
 */

package controlador;

import modelo.Sesion;
import modelo.Usuario;
import modelo.UsuarioDAO;

/*
 * Controlador de autenticación (sin UI).
 * - Valida credenciales usando UsuarioDAO.
 * - Devuelve Sesion con rol real.
 * - Lanza IllegalArgumentException en caso de error.
 */
public class AuthControl {

    public Sesion login(String username, String password) {
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            throw new IllegalArgumentException("Usuario y contraseña son obligatorios.");
        }

        UsuarioDAO dao = new UsuarioDAO();
        Usuario u = dao.buscarPorUsername(username);
        if (u == null) throw new IllegalArgumentException("Usuario no encontrado.");
        if (!password.equals(u.getPasswordHash())) throw new IllegalArgumentException("Contraseña incorrecta.");
        if (!u.isActivo()) throw new IllegalArgumentException("Usuario inactivo.");

        return new Sesion(u.getId(), u.getUsername(), u.getNombreCompleto(), u.getRol());
    }
}
