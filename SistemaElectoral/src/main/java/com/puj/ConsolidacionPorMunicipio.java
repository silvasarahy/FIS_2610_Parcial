package com.puj;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConsolidacionPorMunicipio implements EstrategiaConsolidacion {

    @Override
    public Map<String, Integer> analizar(List<Acta> actas) {
        if (actas == null) {
            throw new IllegalArgumentException("La lista de actas no puede ser nula.");
        }
        Map<String, Integer> votosPorMunicipio = new HashMap<>();
        for (Acta acta : actas) {
            String nombreMunicipio = acta.getMesa()
                    .getPuesto()
                    .getMunicipio()
                    .getNombre();
            int totalActa = acta.calcularTotalVotos();
            votosPorMunicipio.put(
                    nombreMunicipio,
                    votosPorMunicipio.getOrDefault(nombreMunicipio, 0) + totalActa
            );
        }
        return votosPorMunicipio;
    }
}