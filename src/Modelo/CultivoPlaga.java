/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/*
 * Entidad CULTIVO_PLAGA (tabla de relación).
 * Define qué plagas están asociadas/permitidas para un cultivo.
 *
 * Uso principal:
 *  - Antes de registrar un ReportePlaga, validar que la plaga elegida esté permitida
 *    para el cultivo del lote (LOTE → idCultivo) usando esta relación.
 *
 * Campos típicos:
 *  - idCultivo (String) → FK a CULTIVO
 *  - idPlaga   (String) → FK a PLAGA
 */
package modelo;

public class CultivoPlaga {
    private String idCultivo;
    private String idPlaga;

    public CultivoPlaga() {}

    public CultivoPlaga(String idCultivo, String idPlaga) {
        this.idCultivo = idCultivo;
        this.idPlaga = idPlaga;
    }

    public String getIdCultivo() { return idCultivo; }
    public void setIdCultivo(String idCultivo) { this.idCultivo = idCultivo; }

    public String getIdPlaga() { return idPlaga; }
    public void setIdPlaga(String idPlaga) { this.idPlaga = idPlaga; }
}
