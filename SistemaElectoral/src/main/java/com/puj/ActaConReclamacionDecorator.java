package com.puj;

public class ActaConReclamacionDecorator extends ActaDecorator {

    private final Reclamacion reclamacion;

    public ActaConReclamacionDecorator(Acta actaEnvuelta, Reclamacion reclamacion) {
        super(actaEnvuelta);
        if (reclamacion == null) {
            throw new IllegalArgumentException("La reclamación no puede ser nula");
        }
        this.reclamacion = reclamacion;
    }

    @Override
    public String obtenerDetalleEstado() {
        String base = (actaEnvuelta instanceof ActaDecorator)
                ? ((ActaDecorator) actaEnvuelta).obtenerDetalleEstado()
                : "Acta " + actaEnvuelta.getId() + " [" + actaEnvuelta.getEstado() + "]";
        return base + " | IMPUGNADA (" + reclamacion.toString() + ")";
    }

    public Reclamacion getReclamacion() {
        return reclamacion;
    }
}