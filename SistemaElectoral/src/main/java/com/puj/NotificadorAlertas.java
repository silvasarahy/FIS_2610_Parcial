package com.puj;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NotificadorAlertas {

    private final List<ObservadorElectoral> observadores;
    private final List<Alerta> historialAlertas;

    public NotificadorAlertas() {
        this.observadores = new ArrayList<>();
        this.historialAlertas = new ArrayList<>();
    }

    public void suscribir(ObservadorElectoral observador) {
        if (observador == null) {
            throw new IllegalArgumentException("El observador electoral no puede ser nulo.");
        }
        if (observadores.contains(observador)) {
            throw new IllegalArgumentException("El observador ya se encuentra suscrito.");
        }
        observadores.add(observador);
    }

    public void desuscribir(ObservadorElectoral observador) {
        if (observador == null) {
            throw new IllegalArgumentException("El observador a desuscribir no puede ser nulo.");
        }
        if (!observadores.remove(observador)) {
            throw new IllegalStateException("El observador no estaba suscrito al notificador.");
        }
    }

    public void notificarAlerta(Alerta alerta) {
        if (alerta == null) {
            throw new IllegalArgumentException("No se puede emitir una alerta nula.");
        }
        historialAlertas.add(alerta);
        for (ObservadorElectoral observador : observadores) {
            observador.actualizar(alerta);
        }
    }

    public void notificarCierreMesa(Mesa mesa, Acta acta) {
        if (mesa == null) {
            throw new IllegalArgumentException("La mesa cerrada no puede ser nula.");
        }
        if (acta == null) {
            throw new IllegalArgumentException("El acta de cierre no puede ser nula.");
        }
        Alerta alertaCierre = new Alerta("Cierre registrado en la mesa: " + mesa.toString(), acta);
        notificarAlerta(alertaCierre);
    }

    public List<ObservadorElectoral> getObservadores() {
        return Collections.unmodifiableList(observadores);
    }

    public List<Alerta> getHistorialAlertas() {
        return Collections.unmodifiableList(historialAlertas);
    }
}