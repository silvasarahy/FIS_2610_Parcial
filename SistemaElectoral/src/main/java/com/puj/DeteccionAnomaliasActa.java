package com.puj;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeteccionAnomaliasActa implements EstrategiaConsolidacion {

    @Override
    public Map<String, Integer> analizar(List<Acta> actas) {
        if (actas == null) {
            throw new IllegalArgumentException("La lista de actas no puede ser nula.");
        }
        Map<String, Integer> anomalias = new HashMap<>();
        for (Acta acta : actas) {
            int totalVotos = acta.calcularTotalVotos();
            int censoMesa = acta.getMesa().getCenso();
            if (totalVotos > censoMesa) {
                int exceso = totalVotos - censoMesa;
                anomalias.put("Mesa-" + acta.getMesa().getNumero(), exceso);
            }
        }
        return anomalias;
    }
}