/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/*
 * Representa la sesión de un usuario autenticado.
 * Se usa en la capa de Vista (Swing) y Controlador para aplicar permisos por rol.
 *
 * Campos mínimos:
 *  - idUsuario: PK de USUARIO
 *  - username: para mostrar en la UI
 *  - nombreCompleto: para encabezados/mensajes
 *  - rol: ADMIN/TECNICO/PRODUCTOR (enum)
 */
package modelo;

public class Sesion {
    private Long idUsuario;
    private String username;
    private String nombreCompleto;
    private Rol rol;

    public Sesion() {}

    public Sesion(Long idUsuario, String username, String nombreCompleto, Rol rol) {
        this.idUsuario = idUsuario;
        this.username = username;
        this.nombreCompleto = nombreCompleto;
        this.rol = rol;
    }

    public Long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }

    // Atajos de permiso para la UI
    public boolean esAdmin()     { return rol == Rol.ADMIN; }
    public boolean esTecnico()   { return rol == Rol.TECNICO; }
    public boolean esProductor() { return rol == Rol.PRODUCTOR; }
}
