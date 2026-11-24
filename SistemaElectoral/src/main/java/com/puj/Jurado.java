package com.puj;

// HU-07: Jurado de votación
public class Jurado {

    private String documento;
    private String nombre;
    private String contacto;

    public Jurado(String documento, String nombre, String contacto) {
        if (documento == null || documento.trim().isEmpty()) {
            throw new IllegalArgumentException("El documento del jurado no puede estar vacío");
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del jurado no puede estar vacío");
        }
        this.documento = documento;
        this.nombre = nombre;
        this.contacto = contacto;
    }

    public String getDocumento() { return documento; }
    public void setDocumento(String documento) { this.documento = documento; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getContacto() { return contacto; }
    public void setContacto(String contacto) { this.contacto = contacto; }

    @Override
    public String toString() {
        return "Jurado{documento=" + documento + ", nombre=" + nombre + ", contacto=" + contacto + "}";
    }
}
