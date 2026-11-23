package com.puj;

import java.util.List;

public abstract class ActaDecorator extends Acta {

    protected final Acta actaEnvuelta;

    public ActaDecorator(Acta actaEnvuelta) {
        super(validarNoNula(actaEnvuelta).getId(), actaEnvuelta.getMesa());
        this.actaEnvuelta = actaEnvuelta;
    }

    private static Acta validarNoNula(Acta acta) {
        if (acta == null) {
            throw new IllegalArgumentException("El acta a decorar no puede ser nula");
        }
        return acta;
    }

    public abstract String obtenerDetalleEstado();

    public Acta getActaEnvuelta() {
        return actaEnvuelta;
    }

    @Override
    public void registrarVotos(Candidato candidato, int votos) {
        actaEnvuelta.registrarVotos(candidato, votos);
    }

    @Override
    public int calcularTotalVotos() {
        return actaEnvuelta.calcularTotalVotos();
    }

    @Override
    public Alerta verificarConsistencia() {
        return actaEnvuelta.verificarConsistencia();
    }

    @Override
    public void validar() {
        actaEnvuelta.validar();
    }

    @Override
    public void cerrar() {
        actaEnvuelta.cerrar();
    }

    @Override
    public String getId() {
        return actaEnvuelta.getId();
    }

    @Override
    public Mesa getMesa() {
        return actaEnvuelta.getMesa();
    }

    @Override
    public String getEstado() {
        return actaEnvuelta.getEstado();
    }

    @Override
    public List<ResultadoCandidato> getResultados() {
        return actaEnvuelta.getResultados();
    }

    @Override
    public int getVotosBlanco() {
        return actaEnvuelta.getVotosBlanco();
    }

    @Override
    public void setVotosBlanco(int votosBlanco) {
        actaEnvuelta.setVotosBlanco(votosBlanco);
    }

    @Override
    public int getVotosNulos() {
        return actaEnvuelta.getVotosNulos();
    }

    @Override
    public void setVotosNulos(int votosNulos) {
        actaEnvuelta.setVotosNulos(votosNulos);
    }

    @Override
    public String toString() {
        return actaEnvuelta.toString() + " -> " + obtenerDetalleEstado();
    }
}