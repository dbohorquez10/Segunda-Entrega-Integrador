/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/*
 * Entidad INSPECCION.
 * Campos típicos (ajusta a tu esquema):
 *  - idInspeccion (String)
 *  - idLote (String) → FK a LOTE
 *  - idAsistente (String) → FK a ASISTENTE_TECNICO (o USUARIO técnico)
 *  - fechaInspeccion (java.sql.Date) (en tu DAO puedes usar SYSDATE)
 *  - totalPlantas (int)
 *  - plantasAfectadas (int)
 *  - estadoFenologico (String)
 *  - porcentajeInfestacion (double)
 *  - observaciones (String)
 */
package modelo;

import java.sql.Date;

public class Inspeccion {
    private String idInspeccion;
    private String idLote;
    private String idAsistente;
    private Date fechaInspeccion;
    private int totalPlantas;
    private int plantasAfectadas;
    private String estadoFenologico;
    private double porcentajeInfestacion;
    private String observaciones;

    public Inspeccion() {}

    public Inspeccion(String idInspeccion, String idLote, String idAsistente, Date fechaInspeccion,
                      int totalPlantas, int plantasAfectadas, String estadoFenologico,
                      double porcentajeInfestacion, String observaciones) {
        this.idInspeccion = idInspeccion;
        this.idLote = idLote;
        this.idAsistente = idAsistente;
        this.fechaInspeccion = fechaInspeccion;
        this.totalPlantas = totalPlantas;
        this.plantasAfectadas = plantasAfectadas;
        this.estadoFenologico = estadoFenologico;
        this.porcentajeInfestacion = porcentajeInfestacion;
        this.observaciones = observaciones;
    }

    public String getIdInspeccion() { return idInspeccion; }
    public void setIdInspeccion(String idInspeccion) { this.idInspeccion = idInspeccion; }

    public String getIdLote() { return idLote; }
    public void setIdLote(String idLote) { this.idLote = idLote; }

    public String getIdAsistente() { return idAsistente; }
    public void setIdAsistente(String idAsistente) { this.idAsistente = idAsistente; }

    public Date getFechaInspeccion() { return fechaInspeccion; }
    public void setFechaInspeccion(Date fechaInspeccion) { this.fechaInspeccion = fechaInspeccion; }

    public int getTotalPlantas() { return totalPlantas; }
    public void setTotalPlantas(int totalPlantas) { this.totalPlantas = totalPlantas; }

    public int getPlantasAfectadas() { return plantasAfectadas; }
    public void setPlantasAfectadas(int plantasAfectadas) { this.plantasAfectadas = plantasAfectadas; }

    public String getEstadoFenologico() { return estadoFenologico; }
    public void setEstadoFenologico(String estadoFenologico) { this.estadoFenologico = estadoFenologico; }

    public double getPorcentajeInfestacion() { return porcentajeInfestacion; }
    public void setPorcentajeInfestacion(double porcentajeInfestacion) { this.porcentajeInfestacion = porcentajeInfestacion; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}
