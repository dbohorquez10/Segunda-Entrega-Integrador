/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/*
 * Entidad CULTIVO.
 * Campos típicos (ajusta nombres si en tu BD difieren):
 *  - idCultivo (ej: 'C001')
 *  - nombreCientifico
 *  - nombreComun
 *  - cicloCultivo (ej: "Corto", "Permanente")
 */
package modelo;

public class Cultivo {
    private String idCultivo;
    private String nombreCientifico;
    private String nombreComun;
    private String cicloCultivo;

    public Cultivo() {}

    public Cultivo(String idCultivo, String nombreCientifico, String nombreComun, String cicloCultivo) {
        this.idCultivo = idCultivo;
        this.nombreCientifico = nombreCientifico;
        this.nombreComun = nombreComun;
        this.cicloCultivo = cicloCultivo;
    }

    public String getIdCultivo() { return idCultivo; }
    public void setIdCultivo(String idCultivo) { this.idCultivo = idCultivo; }

    public String getNombreCientifico() { return nombreCientifico; }
    public void setNombreCientifico(String nombreCientifico) { this.nombreCientifico = nombreCientifico; }

    public String getNombreComun() { return nombreComun; }
    public void setNombreComun(String nombreComun) { this.nombreComun = nombreComun; }

    public String getCicloCultivo() { return cicloCultivo; }
    public void setCicloCultivo(String cicloCultivo) { this.cicloCultivo = cicloCultivo; }

    @Override
    public String toString() {
        return idCultivo + " - " + nombreComun;
    }
}

