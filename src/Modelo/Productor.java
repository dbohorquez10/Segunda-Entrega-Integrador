/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/*
 * Entidad PRODUCTOR.
 * Perfil responsable del lugar de producción y los lotes (puede actualizar sus datos y pedir inspecciones).
 */
package modelo;

public class Productor {
    private Long idProductor;
    private String numeroIdentidad;
    private String nombreCompleto;
    private String direccion;
    private String telefono;
    private String correoElectronico;

    public Productor() {}

    public Productor(Long idProductor, String numeroIdentidad, String nombreCompleto,
                     String direccion, String telefono, String correoElectronico) {
        this.idProductor = idProductor;
        this.numeroIdentidad = numeroIdentidad;
        this.nombreCompleto = nombreCompleto;
        this.direccion = direccion;
        this.telefono = telefono;
        this.correoElectronico = correoElectronico;
    }

    public Long getIdProductor() { return idProductor; }
    public void setIdProductor(Long idProductor) { this.idProductor = idProductor; }

    public String getNumeroIdentidad() { return numeroIdentidad; }
    public void setNumeroIdentidad(String numeroIdentidad) { this.numeroIdentidad = numeroIdentidad; }

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getCorreoElectronico() { return correoElectronico; }
    public void setCorreoElectronico(String correoElectronico) { this.correoElectronico = correoElectronico; }
}
