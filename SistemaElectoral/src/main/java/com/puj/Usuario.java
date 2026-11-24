package com.puj;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// HU-15: Usuario del sistema con un rol (implementa ObservadorElectoral para el patrón Observer)
public class Usuario implements ObservadorElectoral {

    private String id;
    private String nombre;
    private String rol;
    private final List<Alerta> alertasRecibidas;

    public Usuario(String id, String nombre, String rol) {
        this.id = id;
        this.nombre = nombre;
        this.rol = rol;
        this.alertasRecibidas = new ArrayList<>();
    }

    public boolean tieneRol(String rolRequerido) {
        return rol.equals(rolRequerido);
    }

    @Override
    public void actualizar(Alerta alerta) {
        if (alerta == null) {
            throw new IllegalArgumentException("La alerta recibida no puede ser nula.");
        }
        this.alertasRecibidas.add(alerta);
    }

    public List<Alerta> getAlertasRecibidas() {
        return Collections.unmodifiableList(alertasRecibidas);
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    @Override
    public String toString() {
        return "Usuario{id=" + id + ", nombre=" + nombre + ", rol=" + rol + "}";
    }
}