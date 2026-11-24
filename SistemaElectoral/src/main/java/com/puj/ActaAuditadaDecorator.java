package com.puj;

import java.time.LocalDateTime;

public class ActaAuditadaDecorator extends ActaDecorator {

    private final String codigoAuditor;
    private final LocalDateTime fechaAuditoria;

    public ActaAuditadaDecorator(Acta actaEnvuelta, String codigoAuditor) {
        super(actaEnvuelta);
        if (codigoAuditor == null || codigoAuditor.trim().isEmpty()) {
            throw new IllegalArgumentException("El código del auditor es obligatorio");
        }
        this.codigoAuditor = codigoAuditor.trim();
        this.fechaAuditoria = LocalDateTime.now();
    }

    @Override
    public String obtenerDetalleEstado() {
        String base = (actaEnvuelta instanceof ActaDecorator)
                ? ((ActaDecorator) actaEnvuelta).obtenerDetalleEstado()
                : "Acta " + actaEnvuelta.getId() + " [" + actaEnvuelta.getEstado() + "]";
        return base + " | AUDITADA (Auditor: " + codigoAuditor + ", Fecha: " + fechaAuditoria + ")";
    }

    public String getCodigoAuditor() {
        return codigoAuditor;
    }

    public LocalDateTime getFechaAuditoria() {
        return fechaAuditoria;
    }
}