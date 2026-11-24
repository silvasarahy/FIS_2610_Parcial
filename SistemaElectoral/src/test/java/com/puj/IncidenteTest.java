package com.puj;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class IncidenteTest {
    private Incidente incidente() {
        PuestoVotacion puesto = new PuestoVotacion("P1", "Puesto Central", "Calle 1", null);
        return new Incidente("I1", "LOGISTICO", "Retraso", LocalDateTime.now(), puesto);
    }

    @Test void asignarResponsable_debeCambiarEstado() {
        Incidente incidente = incidente();
        incidente.asignarResponsable("Ana");
        assertEquals("Ana", incidente.getResponsable());
        assertEquals("EN_SEGUIMIENTO", incidente.getEstado());
    }

    @Test void asignarResponsable_conResponsableLimite_debeAceptarTexto() {
        Incidente incidente = incidente();
        incidente.asignarResponsable("Operador-1");
        assertEquals("Operador-1", incidente.getResponsable());
    }

    @Test void resolver_debeMarcarIncidenteResuelto() {
        Incidente incidente = incidente();
        incidente.resolver();
        assertEquals("RESUELTO", incidente.getEstado());
    }

    @Test void resolver_dosVeces_debeMantenerEstadoResuelto() {
        Incidente incidente = incidente();
        incidente.resolver();
        incidente.resolver();
        assertEquals("RESUELTO", incidente.getEstado());
    }

    @Test void getters_ySetters_debenActualizarDatosSinPerderValores() {
        Incidente incidente = incidente();
        incidente.setTipo("SEGURIDAD");
        incidente.setDescripcion("Acceso restringido");
        assertEquals("I1", incidente.getId());
        assertEquals("SEGURIDAD", incidente.getTipo());
        assertEquals("Acceso restringido", incidente.getDescripcion());
        assertNotNull(incidente.getFechaHora());
        assertEquals("Puesto Central", incidente.getPuesto().getNombre());
        assertNull(incidente.getResponsable());
    }

    @Test void toString_conDatosLimite_debeIncluirEstadoYDescripcion() {
        Incidente incidente = incidente();
        String texto = incidente.toString();
        assertTrue(texto.contains("I1"));
        assertTrue(texto.contains("REPORTADO"));
        assertTrue(texto.contains("Retraso"));
    }
}
