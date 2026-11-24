package com.puj;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ActaTest {

    private Mesa mesaCerrada;
    private Candidato candidato1;

    @BeforeEach
    void setUp() {
        Departamento depto = new Departamento("11", "Bogotá D.C.");
        Municipio muni = new Municipio("11001", "Bogotá", depto);
        PuestoVotacion puesto = new PuestoVotacion("P01", "Corferias", "Cra 37 # 24-67", muni);
        mesaCerrada = new Mesa(1, 100, puesto);
        mesaCerrada.instalar();
        mesaCerrada.cerrar();

        OrganizacionPolitica partido = new OrganizacionPolitica("OP1", "Partido A");
        partido.habilitar();
        candidato1 = new Candidato("1010", "Ana Gómez", partido);
    }

    // 1. Constructor Acta(String id, Mesa mesa)
    @Test
    void testCrearActaMesaNoCerrada_Negativa() {
        Departamento depto = new Departamento("11", "Bogotá D.C.");
        Municipio muni = new Municipio("11001", "Bogotá", depto);
        PuestoVotacion puesto = new PuestoVotacion("P01", "Corferias", "Cra 37 # 24-67", muni);
        Mesa mesaAbierta = new Mesa(2, 50, puesto);
        mesaAbierta.instalar();

        assertThrows(IllegalStateException.class, () -> new Acta("ACT-02", mesaAbierta));
    }

    @Test
    void testCrearActaMesaCensoCero_Limite() {
        Departamento depto = new Departamento("11", "Bogotá D.C.");
        Municipio muni = new Municipio("11001", "Bogotá", depto);
        PuestoVotacion puesto = new PuestoVotacion("P01", "Corferias", "Cra 37 # 24-67", muni);
        Mesa mesaCero = new Mesa(3, 0, puesto);
        mesaCero.instalar();
        mesaCero.cerrar();

        Acta acta = new Acta("ACT-03", mesaCero);
        assertEquals("BORRADOR", acta.getEstado());
        assertEquals(0, acta.calcularTotalVotos());
    }

    // 2. registrarVotos(Candidato, int)
    @Test
    void testRegistrarVotosEnActaCerradaOVotosNegativos_Negativa() {
        Acta acta = new Acta("ACT-01", mesaCerrada);
        assertThrows(IllegalArgumentException.class, () -> acta.registrarVotos(candidato1, -5));

        acta.validar();
        acta.cerrar();
        assertThrows(IllegalStateException.class, () -> acta.registrarVotos(candidato1, 10));
    }

    @Test
    void testRegistrarVotosCero_Limite() {
        Acta acta = new Acta("ACT-01", mesaCerrada);
        acta.registrarVotos(candidato1, 0);
        assertEquals(0, acta.calcularTotalVotos());
        assertEquals(1, acta.getResultados().size());
    }

    // 3. calcularTotalVotos()
    @Test
    void testCalcularTotalVotosSinResultados_Limite() {
        Acta acta = new Acta("ACT-01", mesaCerrada);
        assertEquals(0, acta.calcularTotalVotos());
    }

    @Test
    void testCalcularTotalVotosIncluyendoBlancoYNulos_NegativaYConsistencia() {
        Acta acta = new Acta("ACT-01", mesaCerrada);
        acta.registrarVotos(candidato1, 30);
        acta.setVotosBlanco(5);
        acta.setVotosNulos(2);
        assertThrows(IllegalArgumentException.class, () -> acta.registrarVotos(candidato1, -1));
        assertEquals(37, acta.calcularTotalVotos());
    }

    // 4. verificarConsistencia()
    @Test
    void testVerificarConsistenciaSuperaCenso_Negativa() {
        Acta acta = new Acta("ACT-01", mesaCerrada); // Censo = 100
        acta.registrarVotos(candidato1, 101);
        Alerta alerta = acta.verificarConsistencia();
        assertNotNull(alerta);
    }

    @Test
    void testVerificarConsistenciaExactamenteIgualAlCenso_Limite() {
        Acta acta = new Acta("ACT-01", mesaCerrada); // Censo = 100
        acta.registrarVotos(candidato1, 100);
        assertNull(acta.verificarConsistencia());
    }

    // 5. cerrar()
    @Test
    void testCerrarActaSinValidar_Negativa() {
        Acta acta = new Acta("ACT-01", mesaCerrada);
        assertThrows(IllegalStateException.class, acta::cerrar);
    }

    @Test
    void testValidarYCerrarActa_Limite() {
        Acta acta = new Acta("ACT-01", mesaCerrada);
        acta.validar();
        acta.cerrar();
        assertEquals("CERRADA", acta.getEstado());
    }
}