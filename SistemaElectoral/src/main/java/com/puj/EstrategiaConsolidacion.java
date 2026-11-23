package com.puj;

import java.util.List;
import java.util.Map;

public interface EstrategiaConsolidacion {
    Map<String, Integer> analizar(List<Acta> actas);
}