package com.puj;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AlertaTest {

    private Acta crearActaBase() {
        PuestoVotacion puesto = new PuestoVotacion("P-01", "Colegio Central", "Cll 10 # 5-20", null);
        Mesa mesa = new Mesa(1, 100, puesto);
        mesa.instalar();
        mesa.cerrar();
        return new Acta("ACTA-100", mesa);
    }

    @Test
    void testCrearAlerta_Negativa_AlertaRecienCreadaNoEstaResuelta() {
        Acta acta = crearActaBase();
        Alerta alerta = new Alerta("Votos superan censo", acta);
        assertFalse(alerta.isResuelta());
    }

    @Test
    void testCrearAlerta_Limite_DescripcionUnCaracter() {
        Acta acta = crearActaBase();
        Alerta alerta = new Alerta("X", acta);
        assertEquals("X", alerta.getDescripcion());
        assertEquals(acta, alerta.getActa());
    }

    @Test
    void testToString_Negativa_ActaNulaLanzaNullPointer() {
        Alerta alertaSinActa = new Alerta("Alerta huérfana", null);
        assertThrows(NullPointerException.class, alertaSinActa::toString);
    }

    @Test
    void testResolver_Limite_IdempotenciaAlResolver() {
        Acta acta = crearActaBase();
        Alerta alerta = new Alerta("Inconsistencia detectada", acta);
        alerta.resolver();
        assertTrue(alerta.isResuelta());
        alerta.resolver();
        assertTrue(alerta.isResuelta());
    }
}