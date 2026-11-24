package com.puj;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class OrganizacionPoliticaTest {

    @Test
    void testCrearOrganizacion_Negativa_IdONombreNuloOVacio() {
        assertThrows(IllegalArgumentException.class, () -> new OrganizacionPolitica("", "Partido Verde"));
        assertThrows(IllegalArgumentException.class, () -> new OrganizacionPolitica("ORG-1", null));
        assertThrows(IllegalArgumentException.class, () -> new OrganizacionPolitica("   ", "   "));
    }

    @Test
    void testCrearOrganizacion_Limite_ValoresMinimosUnCaracter() {
        OrganizacionPolitica org = new OrganizacionPolitica("1", "A");
        assertEquals("1", org.getId());
        assertEquals("A", org.getNombre());
        assertEquals("INSCRITA", org.getEstadoParticipacion());
    }

    @Test
    void testEstaHabilitada_Negativa_RecienInscritaNoEstaHabilitada() {
        OrganizacionPolitica org = new OrganizacionPolitica("ORG-2", "Movimiento Nuevo");
        assertFalse(org.estaHabilitada());
    }

    @Test
    void testHabilitar_Limite_TransicionDeInscritaAHabilitada() {
        OrganizacionPolitica org = new OrganizacionPolitica("ORG-2", "Movimiento Nuevo");
        org.habilitar();
        assertTrue(org.estaHabilitada());
        assertEquals("HABILITADA", org.getEstadoParticipacion());
    }
}