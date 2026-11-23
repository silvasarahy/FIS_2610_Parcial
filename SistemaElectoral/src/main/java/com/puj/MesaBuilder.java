package com.puj;

public class MesaBuilder {

    private int numero;
    private int censo;
    private PuestoVotacion puesto;

    public MesaBuilder conNumero(int numero) {
        this.numero = numero;
        return this;
    }

    public MesaBuilder conCenso(int censo) {
        this.censo = censo;
        return this;
    }

    public MesaBuilder enPuesto(PuestoVotacion puesto) {
        this.puesto = puesto;
        return this;
    }

    public Mesa build() {
        if (numero <= 0) {
            throw new IllegalArgumentException("El número de mesa debe ser positivo (> 0).");
        }
        if (puesto == null) {
            throw new IllegalArgumentException("La mesa debe estar asociada a un PuestoVotacion válido.");
        }
        if (censo <= 0) {
            throw new IllegalArgumentException("El censo de votantes debe ser mayor a 0.");
        }
        return new Mesa(numero, censo, puesto);
    }
}