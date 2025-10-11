/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/*
 * Entidad PLAGA.
 * Campos típicos:
 *  - idPlaga (ej: 'P001')
 *  - nombreCientifico
 *  - nombreComun
 *  - tipoPlaga (ej: insecto, hongo, bacteria, virus)
 */
package modelo;

public class Plaga {
    private String idPlaga;
    private String nombreCientifico;
    private String nombreComun;
    private String tipoPlaga;

    public Plaga() {}

    public Plaga(String idPlaga, String nombreCientifico, String nombreComun, String tipoPlaga) {
        this.idPlaga = idPlaga;
        this.nombreCientifico = nombreCientifico;
        this.nombreComun = nombreComun;
        this.tipoPlaga = tipoPlaga;
    }

    public String getIdPlaga() { return idPlaga; }
    public void setIdPlaga(String idPlaga) { this.idPlaga = idPlaga; }

    public String getNombreCientifico() { return nombreCientifico; }
    public void setNombreCientifico(String nombreCientifico) { this.nombreCientifico = nombreCientifico; }

    public String getNombreComun() { return nombreComun; }
    public void setNombreComun(String nombreComun) { this.nombreComun = nombreComun; }

    public String getTipoPlaga() { return tipoPlaga; }
    public void setTipoPlaga(String tipoPlaga) { this.tipoPlaga = tipoPlaga; }

    @Override
    public String toString() {
        return idPlaga + " - " + nombreComun;
    }
}
