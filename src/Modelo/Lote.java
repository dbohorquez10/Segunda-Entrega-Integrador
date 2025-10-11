/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/*
 * Representa un lote agrícola (unidad de terreno).
 * 
 * Campos básicos:
 *  - idLote: identificador único del lote.
 *  - idCultivo: cultivo asociado (FK a CULTIVO).
 *  - area: área del lote.
 * 
 * Usado por:
 *  - LoteDAO (CRUD).
 *  - AsignacionLoteDAO (filtro por técnico).
 *  - ReportePlagaPanel (selección de lote).
 */
public class Lote {

    private String idLote;
    private String idCultivo;
    private String area;

    // ================== Constructores ==================

    public Lote() {
    }

    /*
     * Constructor base para crear un lote con todos sus campos.
     */
    public Lote(String idLote, String idCultivo, String area) {
        this.idLote = idLote;
        this.idCultivo = idCultivo;
        this.area = area;
    }

    // ================== Getters y Setters ==================

    public String getIdLote() {
        return idLote;
    }

    public void setIdLote(String idLote) {
        this.idLote = idLote;
    }

    public String getIdCultivo() {
        return idCultivo;
    }

    public void setIdCultivo(String idCultivo) {
        this.idCultivo = idCultivo;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    // ================== Otros métodos ==================

    @Override
    public String toString() {
        return idLote + " (Cultivo: " + idCultivo + ")";
    }
}
