package com.puj;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AsignacionJuradoTest {

    @Test
    void testAsignarJurado_Negativa_JuradoMesaOFuncionNulaOVacia() {
        PuestoVotacion puesto = new PuestoVotacion("P-01", "Sede Norte", "Cra 7 # 100", null);
        Mesa mesa = new Mesa(1, 300, puesto);
        Jurado jurado = new Jurado("8001", "Pedro Mora", "3109998877");

        assertThrows(IllegalArgumentException.class, () -> new AsignacionJurado(null, mesa, "PRESIDENTE"));
        assertThrows(IllegalArgumentException.class, () -> new AsignacionJurado(jurado, null, "PRESIDENTE"));
        assertThrows(IllegalArgumentException.class, () -> new AsignacionJurado(jurado, mesa, ""));
    }

    @Test
    void testAsignarJurado_Limite_AsignacionInicialActiva() {
        PuestoVotacion puesto = new PuestoVotacion("P-01", "Sede Norte", "Cra 7 # 100", null);
        Mesa mesa = new Mesa(1, 300, puesto);
        Jurado jurado = new Jurado("8001", "Pedro Mora", "3109998877");

        AsignacionJurado asignacion = new AsignacionJurado(jurado, mesa, "VOCAL");
        assertTrue(asignacion.isActiva());
        assertNull(asignacion.getMotivoRetiro());
    }

    @Test
    void testRetirar_Negativa_MotivoVacioORetiroDuplicado() {
        PuestoVotacion puesto = new PuestoVotacion("P-01", "Sede Norte", "Cra 7 # 100", null);
        Mesa mesa = new Mesa(1, 300, puesto);
        Jurado jurado = new Jurado("8001", "Pedro Mora", "3109998877");
        AsignacionJurado asignacion = new AsignacionJurado(jurado, mesa, "PRESIDENTE");

        assertThrows(IllegalArgumentException.class, () -> asignacion.retirar(""));
        asignacion.retirar("Incapacidad médica");
        assertThrows(IllegalStateException.class, () -> asignacion.retirar("Otro motivo"));
    }

    @Test
    void testRetirar_Limite_MotivoUnCaracterYCambioEstado() {
        PuestoVotacion puesto = new PuestoVotacion("P-01", "Sede Norte", "Cra 7 # 100", null);
        Mesa mesa = new Mesa(1, 300, puesto);
        Jurado jurado = new Jurado("8001", "Pedro Mora", "3109998877");
        AsignacionJurado asignacion = new AsignacionJurado(jurado, mesa, "PRESIDENTE");

        asignacion.retirar("X");
        assertFalse(asignacion.isActiva());
        assertEquals("X", asignacion.getMotivoRetiro());
    }
}