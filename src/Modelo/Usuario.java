/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/*
 * Entidad USUARIO.
 * Mapea la tabla USUARIO con sus columnas principales.
 *
 * Campos comunes:
 *  - id (NUMBER)
 *  - username (VARCHAR2)
 *  - passwordHash (VARCHAR2) → en MVP puede ser texto plano; en producción usar hash.
 *  - nombreCompleto (VARCHAR2)
 *  - rol (enum) obtenido vía FK a ROL
 *  - activo (char 'S'/'N') lo exponemos como boolean para la app
 */
package modelo;

public class Usuario {
    private Long id;
    private String username;
    private String passwordHash;
    private String nombreCompleto;
    private Rol rol;
    private boolean activo;

    public Usuario() {}

    public Usuario(Long id, String username, String passwordHash, String nombreCompleto, Rol rol, boolean activo) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.nombreCompleto = nombreCompleto;
        this.rol = rol;
        this.activo = activo;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    @Override
    public String toString() {
        return username + " (" + rol + ")";
    }
}
