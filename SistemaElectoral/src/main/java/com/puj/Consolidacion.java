package com.puj;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// HU-13: Consolidación y publicación de resultados
// HU-14: Consulta de resultados por territorio y candidato
public class Consolidacion {

    private ProcesoElectoral proceso;
    private List<Acta> actas;
    private String estadoPublicacion;

    public Consolidacion(ProcesoElectoral proceso) {
        this.proceso = proceso;
        this.actas = new ArrayList<>();
        this.estadoPublicacion = "EN_PREPARACION";
    }

    // HU-13: solo se consolidan actas cerradas
    public void agregarActa(Acta acta) {
        if (!acta.getEstado().equals("CERRADA")) {
            throw new IllegalStateException("Solo se consolidan actas cerradas");
        }
        actas.add(acta);
    }

    public int totalVotosCandidato(Candidato candidato) {
        int total = 0;
        for (Acta acta : actas) {
            total += votosEnActa(acta, candidato);
        }
        return total;
    }

    // HU-14: votos de un candidato en un departamento
    public int votosPorDepartamento(Candidato candidato, Departamento departamento) {
        int total = 0;
        for (Acta acta : actas) {
            if (acta.getMesa().getPuesto().getMunicipio().getDepartamento() == departamento) {
                total += votosEnActa(acta, candidato);
            }
        }
        return total;
    }

    // HU-14: votos de un candidato en un municipio
    public int votosPorMunicipio(Candidato candidato, Municipio municipio) {
        int total = 0;
        for (Acta acta : actas) {
            if (acta.getMesa().getPuesto().getMunicipio() == municipio) {
                total += votosEnActa(acta, candidato);
            }
        }
        return total;
    }

    private int votosEnActa(Acta acta, Candidato candidato) {
        for (ResultadoCandidato r : acta.getResultados()) {
            if (r.getCandidato() == candidato) {
                return r.getVotos();
            }
        }
        return 0;
    }

    public void validar() {
        estadoPublicacion = "VALIDADA";
    }

    public void publicar() {
        if (!estadoPublicacion.equals("VALIDADA")) {
            throw new IllegalStateException("La consolidación debe validarse antes de publicarse");
        }
        estadoPublicacion = "PUBLICADA";
    }

    public ProcesoElectoral getProceso() { return proceso; }
    public List<Acta> getActas() { return actas; }
    public String getEstadoPublicacion() { return estadoPublicacion; }

    @Override
    public String toString() {
        return "Consolidacion{proceso=" + proceso.getId() + ", actas=" + actas.size() + ", estado=" + estadoPublicacion + "}";
    }
    private EstrategiaConsolidacion estrategia;

    public void setEstrategia(EstrategiaConsolidacion estrategia) {
        if (estrategia == null) {
            throw new IllegalArgumentException("La estrategia de consolidación no puede ser nula.");
        }
        this.estrategia = estrategia;
    }

    public Map<String, Integer> ejecutarAnalisis(List<Acta> actas) {
        if (this.estrategia == null) {
            throw new IllegalStateException("Debe asignar una estrategia antes de ejecutar el análisis.");
        }
        return this.estrategia.analizar(actas);
    }
}
