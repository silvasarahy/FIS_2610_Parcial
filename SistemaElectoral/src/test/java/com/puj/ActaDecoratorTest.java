package com.puj;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ActaDecoratorTest {
    private Acta actaCerrada() {
        PuestoVotacion puesto = new PuestoVotacion("P1", "Puesto", "Dirección", null);
        Mesa mesa = new Mesa(1, 100, puesto);
        mesa.instalar();
        mesa.cerrar();
        return new Acta("A1", mesa);
    }

    @Test void actaAuditada_conActaNula_debeRechazarla() {
        assertThrows(IllegalArgumentException.class,
                () -> new ActaAuditadaDecorator(null, "AUD-1"));
    }

    @Test void actaAuditada_conCodigoLimite_debeExponerDatos() {
        ActaAuditadaDecorator decorador = new ActaAuditadaDecorator(actaCerrada(), " AUD-1 ");
        assertEquals("AUD-1", decorador.getCodigoAuditor());
        assertNotNull(decorador.getFechaAuditoria());
        assertTrue(decorador.obtenerDetalleEstado().contains("AUDITADA"));
        assertSame(decorador.getActaEnvuelta().getId(), decorador.getId());
    }

    @Test void actaAuditada_conCodigoVacio_debeRechazarlo() {
        assertThrows(IllegalArgumentException.class,
                () -> new ActaAuditadaDecorator(actaCerrada(), "  "));
    }

    @Test void actaConReclamacion_conReclamacionNula_debeRechazarla() {
        assertThrows(IllegalArgumentException.class,
                () -> new ActaConReclamacionDecorator(actaCerrada(), null));
    }

    @Test void decoradores_apilados_debenConservarLaCadenaCompleta() {
        Acta base = actaCerrada();
        Reclamacion reclamacion = new Reclamacion("R1", "Ciudadano", java.time.LocalDate.now(), "Conteo", base);
        ActaAuditadaDecorator auditada = new ActaAuditadaDecorator(base, "AUD-2");
        ActaConReclamacionDecorator impugnada = new ActaConReclamacionDecorator(auditada, reclamacion);

        assertSame(auditada, impugnada.getActaEnvuelta());
        assertSame(reclamacion, impugnada.getReclamacion());
        assertTrue(impugnada.obtenerDetalleEstado().contains("AUDITADA"));
        assertTrue(impugnada.obtenerDetalleEstado().contains("IMPUGNADA"));
        assertEquals(base.getId(), impugnada.getId());
    }
}
