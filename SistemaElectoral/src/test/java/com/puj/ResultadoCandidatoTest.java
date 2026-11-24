package com.puj;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ResultadoCandidatoTest {

    @Test
    void testCrearResultadoVotosNegativos_Negativa() {
        OrganizacionPolitica partido = new OrganizacionPolitica("OP1", "Partido A");
        partido.habilitar();
        Candidato candidato = new Candidato("1010", "Carlos Ruiz", partido);

        assertThrows(IllegalArgumentException.class, () -> new ResultadoCandidato(candidato, -1));
    }

    @Test
    void testCrearResultadoCeroYAltoVotos_Limite() {
        OrganizacionPolitica partido = new OrganizacionPolitica("OP1", "Partido A");
        partido.habilitar();
        Candidato candidato = new Candidato("1010", "Carlos Ruiz", partido);

        ResultadoCandidato resCero = new ResultadoCandidato(candidato, 0);
        assertEquals(0, resCero.getVotos());

        ResultadoCandidato resAlto = new ResultadoCandidato(candidato, 100000);
        assertEquals(100000, resAlto.getVotos());
    }
}