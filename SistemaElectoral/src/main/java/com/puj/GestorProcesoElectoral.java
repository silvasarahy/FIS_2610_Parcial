package com.puj;

import java.time.LocalDate;

public class GestorProcesoElectoral {

    private static GestorProcesoElectoral instancia;

    private ProcesoElectoral procesoActivo;
    private LocalDate fechaAperturaUrnas;
    private LocalDate fechaCierreUrnas;

    private GestorProcesoElectoral() {
        this.procesoActivo = new ProcesoElectoral("PROC-2026", "Legislativas 2026", LocalDate.of(2026, 3, 8));
        this.fechaAperturaUrnas = LocalDate.of(2026, 3, 8);
        this.fechaCierreUrnas = LocalDate.of(2026, 3, 8);
    }

    public static synchronized GestorProcesoElectoral getInstance() {
        if (instancia == null) {
            instancia = new GestorProcesoElectoral();
        }
        return instancia;
    }

    public void configurarProcesoActivo(ProcesoElectoral nuevoProceso, LocalDate apertura, LocalDate cierre) {
        if (nuevoProceso == null) {
            throw new IllegalArgumentException("El proceso electoral activo no puede ser nulo.");
        }
        if (apertura == null || cierre == null || cierre.isBefore(apertura)) {
            throw new IllegalArgumentException("El calendario electoral tiene fechas inválidas.");
        }
        this.procesoActivo = nuevoProceso;
        this.fechaAperturaUrnas = apertura;
        this.fechaCierreUrnas = cierre;
    }

    public ProcesoElectoral getProcesoActivo() {
        return procesoActivo;
    }

    public LocalDate getFechaAperturaUrnas() {
        return fechaAperturaUrnas;
    }

    public LocalDate getFechaCierreUrnas() {
        return fechaCierreUrnas;
    }
}