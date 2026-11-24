package com.puj;

import java.time.LocalDateTime;

// HU-15: Registro de una acción realizada por un usuario
public class RegistroAuditoria {

    private Usuario usuario;
    private String accion;
    private LocalDateTime fechaHora;

    public RegistroAuditoria(Usuario usuario, String accion) {
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario auditor no puede ser nulo");
        }
        if (accion == null || accion.trim().isEmpty()) {
            throw new IllegalArgumentException("La acción auditada no puede estar vacía");
        }
        this.usuario = usuario;
        this.accion = accion.trim();
        this.fechaHora = LocalDateTime.now();
    }

    public Usuario getUsuario() { return usuario; }
    public String getAccion() { return accion; }
    public LocalDateTime getFechaHora() { return fechaHora; }

    @Override
    public String toString() {
        return "RegistroAuditoria{usuario=" + usuario.getNombre() + ", accion=" + accion + ", fecha=" + fechaHora + "}";
    }
}
