/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/*
 * Entidad lógica de ASIGNACIÓN de LOTE a TÉCNICO.
 * No requiere tabla propia: se apoya en INSPECCION como “evidencia de asignación”.
 *
 * Campos:
 *  - idLote        : String   (LOTE.ID)
 *  - idAsistente   : String   (ID del técnico que aparece en INSPECCION.ID_ASISTENTE)
 *
 */
package modelo;

public class AsignacionLote {
    private String idLote;
    private String idAsistente;

    public AsignacionLote() {}

    public AsignacionLote(String idLote, String idAsistente) {
        this.idLote = idLote;
        this.idAsistente = idAsistente;
    }

    public String getIdLote() { return idLote; }
    public void setIdLote(String idLote) { this.idLote = idLote; }

    public String getIdAsistente() { return idAsistente; }
    public void setIdAsistente(String idAsistente) { this.idAsistente = idAsistente; }
}
