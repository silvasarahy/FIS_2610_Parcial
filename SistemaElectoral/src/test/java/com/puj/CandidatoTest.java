package com.puj;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CandidatoTest {

    @Test
    void testInscribirCandidato_Negativa_OrganizacionNoHabilitadaODatosVacios() {
        OrganizacionPolitica orgNoHabilitada = new OrganizacionPolitica("ORG-1", "Alianza");
        assertThrows(IllegalStateException.class,
                () -> new Candidato("102030", "Mateo Pérez", orgNoHabilitada));

        orgNoHabilitada.habilitar();
        assertThrows(IllegalArgumentException.class,
                () -> new Candidato("", "Mateo Pérez", orgNoHabilitada));
        assertThrows(IllegalArgumentException.class,
                () -> new Candidato("102030", null, orgNoHabilitada));
    }

    @Test
    void testInscribirCandidato_Limite_SinOrganizacionIndependiente() {
        Candidato independiente = new Candidato("1", "L", null);
        assertNull(independiente.getOrganizacion());
        assertEquals("INSCRITO", independiente.getEstado());
    }

    @Test
    void testEstadoInicial_Negativa_NoAceptadoAlCrear() {
        OrganizacionPolitica org = new OrganizacionPolitica("ORG-1", "Alianza");
        org.habilitar();
        Candidato candidato = new Candidato("102030", "Mateo Pérez", org);
        assertNotEquals("ACEPTADO", candidato.getEstado());
    }

    @Test
    void testAceptar_Limite_CambioEstadoAceptado() {
        OrganizacionPolitica org = new OrganizacionPolitica("ORG-1", "Alianza");
        org.habilitar();
        Candidato candidato = new Candidato("102030", "Mateo Pérez", org);
        candidato.aceptar();
        assertEquals("ACEPTADO", candidato.getEstado());
    }
}