/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/*
 * Entidad REPORTE_PLAGA.
 * Representa el informe que el TÉCNICO registra sobre la presencia/estado de una plaga en un lote.
 *
 * Reglas de negocio (se validarán en el Control/DAO):
 *  - Solo TÉCNICO o ADMIN pueden crear reportes.
 *  - El TÉCNICO solo puede reportar sobre lotes que trabaja (verificación vía INSPECCION del técnico sobre ese lote).
 *  - La PLAGA debe estar permitida para el CULTIVO del LOTE (validación vía tabla CULTIVO_PLAGA).
 *
 * Campos típicos (ajusta a tu esquema real si difiere):
 *  - idReporte       (String o Long según tu BD)
 *  - idLote          (String)    → FK a LOTE
 *  - idPlaga         (String)    → FK a PLAGA
 *  - fechaReporte    (java.sql.Date)  (por defecto SYSDATE en BD si quieres)
 *  - severidad       (String)    → leve | moderada | severa (u otro catálogo)
 *  - porcentaje      (Double)    → 0..100 (opcional si ya manejas en INSPECCION)
 *  - observaciones   (String)    → texto libre
 */
package modelo;

import java.sql.Date;

public class ReportePlaga {
    private String idReporte;
    private String idLote;
    private String idPlaga;
    private Date fechaReporte;
    private String severidad;
    private Double porcentaje;
    private String observaciones;

    public ReportePlaga() {}

    public ReportePlaga(String idReporte, String idLote, String idPlaga, Date fechaReporte,
                        String severidad, Double porcentaje, String observaciones) {
        this.idReporte = idReporte;
        this.idLote = idLote;
        this.idPlaga = idPlaga;
        this.fechaReporte = fechaReporte;
        this.severidad = severidad;
        this.porcentaje = porcentaje;
        this.observaciones = observaciones;
    }

    public String getIdReporte() { return idReporte; }
    public void setIdReporte(String idReporte) { this.idReporte = idReporte; }

    public String getIdLote() { return idLote; }
    public void setIdLote(String idLote) { this.idLote = idLote; }

    public String getIdPlaga() { return idPlaga; }
    public void setIdPlaga(String idPlaga) { this.idPlaga = idPlaga; }

    public Date getFechaReporte() { return fechaReporte; }
    public void setFechaReporte(Date fechaReporte) { this.fechaReporte = fechaReporte; }

    public String getSeveridad() { return severidad; }
    public void setSeveridad(String severidad) { this.severidad = severidad; }

    public Double getPorcentaje() { return porcentaje; }
    public void setPorcentaje(Double porcentaje) { this.porcentaje = porcentaje; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}
