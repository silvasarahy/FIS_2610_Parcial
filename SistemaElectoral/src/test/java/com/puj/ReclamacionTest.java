package com.puj;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class ReclamacionTest {
    private Acta acta() {
        PuestoVotacion puesto = new PuestoVotacion("P1", "Puesto", "Calle", null);
        Mesa mesa = new Mesa(1, 100, puesto);
        mesa.instalar(); mesa.cerrar();
        return new Acta("A1", mesa);
    }

    @Test void constructor_sinMotivo_debeRechazarLaReclamacion() {
        assertThrows(IllegalArgumentException.class,
                () -> new Reclamacion("R1", "Ana", LocalDate.now(), "  ", acta()));
    }

    @Test void constructor_conActaLimiteValida_debeRadicarla() {
        Reclamacion r = new Reclamacion("R1", "Ana", LocalDate.now(), "Conteo", acta());
        assertEquals("RADICADA", r.getEstado());
        assertEquals("Conteo", r.getMotivo());
        assertNull(r.getDecision());
    }

    @Test void constructor_conActaNula_debeRechazarLaReclamacion() {
        assertThrows(IllegalArgumentException.class,
                () -> new Reclamacion("R1", "Ana", LocalDate.now(), "Conteo", null));
    }

    @Test void iniciarRevision_debeCambiarEstado() {
        Reclamacion r = new Reclamacion("R1", "Ana", LocalDate.now(), "Conteo", acta());
        r.iniciarRevision();
        assertEquals("EN_REVISION", r.getEstado());
    }

    @Test void iniciarRevision_despuesDeResolver_debePermitirCambioDeEstado() {
        Reclamacion r = new Reclamacion("R1", "Ana", LocalDate.now(), "Conteo", acta());
        r.resolver("Aceptar");
        r.iniciarRevision();
        assertEquals("EN_REVISION", r.getEstado());
    }

    @Test void resolver_debeGuardarDecisionSinModificarMotivo() {
        Reclamacion r = new Reclamacion("R1", "Ana", LocalDate.now(), "Conteo", acta());
        r.resolver("Rechazar");
        assertEquals("RESUELTA", r.getEstado());
        assertEquals("Rechazar", r.getDecision());
        assertEquals("Conteo", r.getMotivo());
    }

    @Test void resolver_conDecisionVacia_debeAceptarCasoLimite() {
        Reclamacion r = new Reclamacion("R1", "Ana", LocalDate.now(), "Conteo", acta());
        r.resolver("");
        assertEquals("", r.getDecision());
        assertEquals("RESUELTA", r.getEstado());
    }

    @Test void getters_yToString_debenExponerDatosDeLaReclamacion() {
        Acta acta = acta();
        LocalDate fecha = LocalDate.of(2026, 1, 1);
        Reclamacion r = new Reclamacion("R1", "Ana", fecha, "Conteo", acta);
        assertEquals("R1", r.getId());
        assertEquals("Ana", r.getSolicitante());
        assertEquals(fecha, r.getFecha());
        assertEquals("Conteo", r.getMotivo());
        assertSame(acta, r.getActa());
        assertEquals("RADICADA", r.getEstado());
        assertTrue(r.toString().contains("R1"));
        assertTrue(r.toString().contains("A1"));
    }
}
