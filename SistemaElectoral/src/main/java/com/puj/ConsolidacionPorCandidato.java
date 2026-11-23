package com.puj;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConsolidacionPorCandidato implements EstrategiaConsolidacion {

    @Override
    public Map<String, Integer> analizar(List<Acta> actas) {
        if (actas == null) {
            throw new IllegalArgumentException("La lista de actas no puede ser nula.");
        }
        Map<String, Integer> votosPorCandidato = new HashMap<>();
        for (Acta acta : actas) {
            for (ResultadoCandidato resultado : acta.getResultados()) {
                String nombreCandidato = resultado.getCandidato().getNombre();
                int votos = resultado.getVotos();
                votosPorCandidato.put(
                        nombreCandidato,
                        votosPorCandidato.getOrDefault(nombreCandidato, 0) + votos
                );
            }
        }
        return votosPorCandidato;
    }
}