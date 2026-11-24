package com.puj;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

public class ConsolidacionStrategyTest {

    private Departamento depto;
    private Municipio muni;
    private Consolidacion consolidacion;
    private Acta actaCerrada;
    private Candidato cand1;

    @BeforeEach
    void setUp() {
        ProcesoElectoral proceso = new ProcesoElectoral("PR-2026", "Legislativas 2026", LocalDate.of(2026, 3, 8));
        consolidacion = new Consolidacion(proceso);

        depto = new Departamento("11", "Bogotá D.C.");
        muni = new Municipio("11001", "Bogotá", depto);
        PuestoVotacion puesto = new PuestoVotacion("P01", "Unicentro", "Dirección", muni);
        Mesa mesa = new Mesa(1, 100, puesto);
        mesa.instalar();
        mesa.cerrar();

        OrganizacionPolitica partido = new OrganizacionPolitica("OP1", "Partido A");
        cand1 = new Candidato("1010", "Laura Pérez", partido);

        actaCerrada = new Acta("ACT-01", mesa);
        actaCerrada.registrarVotos(cand1, 100);
        actaCerrada.validar();
        actaCerrada.cerrar();
    }

    // 1. agregarActa(Acta)
    @Test
    void testAgregarActaNoCerrada_Negativa() {
        Mesa mesa2 = new Mesa(2, 50, actaCerrada.getMesa().getPuesto());
        mesa2.instalar();
        mesa2.cerrar();
        Acta actaBorrador = new Acta("ACT-02", mesa2);

        assertThrows(IllegalStateException.class, () -> consolidacion.agregarActa(actaBorrador));
    }

    @Test
    void testAgregarActaCerrada_Limite() {
        consolidacion.agregarActa(actaCerrada);
        assertEquals(1, consolidacion.getActas().size());
    }

    // 2. totalVotosCandidato / votosPorDepartamento / votosPorMunicipio
    @Test
    void testConsultasTerritorialesSinActas_Limite() {
        assertEquals(0, consolidacion.totalVotosCandidato(cand1));
        assertEquals(0, consolidacion.votosPorDepartamento(cand1, depto));
        assertEquals(0, consolidacion.votosPorMunicipio(cand1, muni));
    }

    @Test
    void testConsultasTerritorialesCandidatoInexistente_Negativa() {
        consolidacion.agregarActa(actaCerrada);
        OrganizacionPolitica partido = new OrganizacionPolitica("OP2", "Partido B");
        Candidato otro = new Candidato("2020", "Sin Votos", partido);

        assertEquals(0, consolidacion.totalVotosCandidato(otro));
        assertEquals(0, consolidacion.votosPorDepartamento(otro, depto));
        assertEquals(0, consolidacion.votosPorMunicipio(otro, muni));
    }

    // 3. publicar()
    @Test
    void testPublicarSinValidar_Negativa() {
        assertThrows(IllegalStateException.class, () -> consolidacion.publicar());
    }

    @Test
    void testValidarYPublicar_Limite() {
        consolidacion.validar();
        consolidacion.publicar();
        assertEquals("PUBLICADA", consolidacion.getEstadoPublicacion());
    }

    // 4. setEstrategia(EstrategiaConsolidacion)
    @Test
    void testSetEstrategiaNula_Negativa() {
        assertThrows(IllegalArgumentException.class, () -> consolidacion.setEstrategia(null));
    }

    @Test
    void testCambioDinamicoEstrategias_Limite() {
        consolidacion.setEstrategia(new ConsolidacionPorCandidato());
        Map<String, Integer> resCand = consolidacion.ejecutarAnalisis(List.of(actaCerrada));
        assertEquals(100, resCand.get("Laura Pérez"));

        consolidacion.setEstrategia(new ConsolidacionPorMunicipio());
        Map<String, Integer> resMuni = consolidacion.ejecutarAnalisis(List.of(actaCerrada));
        assertEquals(100, resMuni.get("Bogotá"));
    }

    // 5. ejecutarAnalisis(List<Acta>) y las 3 Estrategias
    @Test
    void testEjecutarAnalisisSinEstrategiaOListaNula_Negativa() {
        assertThrows(IllegalStateException.class, () -> consolidacion.ejecutarAnalisis(List.of(actaCerrada)));
        assertThrows(IllegalArgumentException.class, () -> new ConsolidacionPorCandidato().analizar(null));
        assertThrows(IllegalArgumentException.class, () -> new ConsolidacionPorMunicipio().analizar(null));
        assertThrows(IllegalArgumentException.class, () -> new DeteccionAnomaliasActa().analizar(null));
    }

    @Test
    void testEstrategiasListaVaciaYCensoExacto_Limite() {
        consolidacion.setEstrategia(new ConsolidacionPorCandidato());
        assertTrue(consolidacion.ejecutarAnalisis(Collections.emptyList()).isEmpty());

        // actaCerrada tiene 100 votos y censo 100 (100% exacto -> no hay anomalía)
        DeteccionAnomaliasActa detector = new DeteccionAnomaliasActa();
        assertTrue(detector.analizar(List.of(actaCerrada)).isEmpty());
    }
}