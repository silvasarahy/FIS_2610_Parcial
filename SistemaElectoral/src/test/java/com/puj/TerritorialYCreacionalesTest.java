package com.puj;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TerritorialYCreacionalesTest {

    private Departamento cundinamarca;
    private Municipio bogota;
    private PuestoVotacion puestoUnicentro;

    @BeforeEach
    void setUp() {
        cundinamarca = new Departamento("25", "Cundinamarca");
        bogota = new Municipio("11001", "Bogotá D.C.", cundinamarca);
        puestoUnicentro = new PuestoVotacion("P-01", "Colegio Unicentro", "Calle 127 # 15", bogota);
    }

    // 1. PRUEBAS SINGLETON: GestorProcesoElectoral

    @Test
    void testGetInstance_Negativa_NoRetornaNull() {
        GestorProcesoElectoral instancia = GestorProcesoElectoral.getInstance();
        assertNotNull(instancia, "getInstance nunca debe retornar null");
    }

    @Test
    void testGetInstance_Limite_UnicaInstanciaEnMultiplesLlamadas() {
        GestorProcesoElectoral g1 = GestorProcesoElectoral.getInstance();
        GestorProcesoElectoral g2 = GestorProcesoElectoral.getInstance();
        assertSame(g1, g2, "Ambas llamadas deben devolver exactamente la misma referencia");
    }

    @Test
    void testConfigurarProcesoActivo_Negativa_ProcesoNuloOFechasInvalidas() {
        GestorProcesoElectoral gestor = GestorProcesoElectoral.getInstance();
        LocalDate hoy = LocalDate.of(2026, 5, 31);

        assertThrows(IllegalArgumentException.class, () ->
                gestor.configurarProcesoActivo(null, hoy, hoy));

        ProcesoElectoral proc = new ProcesoElectoral("P-99", "Presidenciales", hoy);
        assertThrows(IllegalArgumentException.class, () ->
                gestor.configurarProcesoActivo(proc, hoy, hoy.minusDays(1)));
    }

    @Test
    void testConfigurarProcesoActivo_Limite_MismaFechaAperturaYCierre() {
        GestorProcesoElectoral gestor = GestorProcesoElectoral.getInstance();
        LocalDate diaEleccion = LocalDate.of(2026, 5, 31);
        ProcesoElectoral proc = new ProcesoElectoral("P-26", "Presidenciales 2026", diaEleccion);

        gestor.configurarProcesoActivo(proc, diaEleccion, diaEleccion);
        assertEquals("P-26", gestor.getProcesoActivo().getId());
        assertEquals(diaEleccion, gestor.getFechaAperturaUrnas());
        assertEquals(diaEleccion, gestor.getFechaCierreUrnas());
    }

    // 2. PRUEBAS BUILDER: MesaBuilder

    @Test
    void testMesaBuilder_Negativa_NumeroNegativoCensoCeroOPuestoNull() {
        MesaBuilder builderNumeroInvalido = new MesaBuilder()
                .conNumero(0)
                .conCenso(100)
                .enPuesto(puestoUnicentro);
        assertThrows(IllegalArgumentException.class, builderNumeroInvalido::build);

        MesaBuilder builderCensoInvalido = new MesaBuilder()
                .conNumero(1)
                .conCenso(-5)
                .enPuesto(puestoUnicentro);
        assertThrows(IllegalArgumentException.class, builderCensoInvalido::build);

        MesaBuilder builderSinPuesto = new MesaBuilder()
                .conNumero(1)
                .conCenso(100)
                .enPuesto(null);
        assertThrows(IllegalArgumentException.class, builderSinPuesto::build);
    }

    @Test
    void testMesaBuilder_Limite_ValoresMinimosValidos() {
        Mesa mesa = new MesaBuilder()
                .conNumero(1)
                .conCenso(1)
                .enPuesto(puestoUnicentro)
                .build();

        assertNotNull(mesa);
        assertEquals(1, mesa.getNumero());
        assertEquals(1, mesa.getCenso());
        assertEquals("CONFIGURADA", mesa.getEstado());
    }

    // 3. PRUEBAS ENTIDAD: Mesa

    @Test
    void testConstructorMesa_Negativa_CensoNegativo() {
        assertThrows(IllegalArgumentException.class, () ->
                new Mesa(1, -1, puestoUnicentro));
    }

    @Test
    void testConstructorMesa_Limite_CensoCero() {
        Mesa mesaCero = new Mesa(1, 0, puestoUnicentro);
        assertEquals(0, mesaCero.getCenso());
    }

    @Test
    void testInstalarMesa_Negativa_DobleInstalacion() {
        Mesa mesa = new Mesa(1, 250, puestoUnicentro);
        mesa.instalar();
        assertThrows(IllegalStateException.class, mesa::instalar);
    }

    @Test
    void testInstalarMesa_Limite_TransicionDesdeConfigurada() {
        Mesa mesa = new Mesa(1, 250, puestoUnicentro);
        assertEquals("CONFIGURADA", mesa.getEstado());
        mesa.instalar();
        assertEquals("INSTALADA", mesa.getEstado());
    }

    @Test
    void testCerrarMesa_Negativa_CerrarSinInstalar() {
        Mesa mesa = new Mesa(1, 250, puestoUnicentro);
        assertThrows(IllegalStateException.class, mesa::cerrar);
    }

    @Test
    void testCerrarMesa_Limite_CierreInmediatoTrasInstalar() {
        Mesa mesa = new Mesa(1, 250, puestoUnicentro);
        mesa.instalar();
        mesa.cerrar();
        assertEquals("CERRADA", mesa.getEstado());
    }

    // 4. PRUEBAS ENTIDAD: PuestoVotacion

    @Test
    void testAgregarMesa_Negativa_PuestoInactivoOMesaDuplicada() {
        Mesa m1 = new Mesa(1, 300, puestoUnicentro);
        puestoUnicentro.agregarMesa(m1);

        Mesa m1Duplicada = new Mesa(1, 200, puestoUnicentro);
        assertThrows(IllegalStateException.class, () -> puestoUnicentro.agregarMesa(m1Duplicada));

        puestoUnicentro.setActivo(false);
        Mesa m2 = new Mesa(2, 300, puestoUnicentro);
        assertThrows(IllegalStateException.class, () -> puestoUnicentro.agregarMesa(m2));
    }

    @Test
    void testGetMesas_Limite_PuestoRecienCreadoSinMesas() {
        assertTrue(puestoUnicentro.getMesas().isEmpty(), "Un puesto nuevo debe iniciar con lista vacía []");
        Mesa m1 = new Mesa(1, 150, puestoUnicentro);
        puestoUnicentro.agregarMesa(m1);
        assertEquals(1, puestoUnicentro.getMesas().size());
    }

    // 5. PRUEBAS ENTIDADES: Municipio, Departamento y ProcesoElectoral

    @Test
    void testAgregarPuesto_Negativa_AgregarElementoNuloDetectadoEnLista() {
        bogota.agregarPuesto(null);
        assertNull(bogota.getPuestos().get(0));
    }

    @Test
    void testGetPuestos_Limite_MunicipioSinPuestos() {
        assertEquals(0, bogota.getPuestos().size());
        bogota.agregarPuesto(puestoUnicentro);
        assertEquals(1, bogota.getPuestos().size());
    }

    @Test
    void testAgregarMunicipio_Negativa_AgregarElementoNuloDetectadoEnLista() {
        cundinamarca.agregarMunicipio(null);
        assertNull(cundinamarca.getMunicipios().get(0));
    }

    @Test
    void testGetMunicipios_Limite_DepartamentoSinMunicipios() {
        assertTrue(cundinamarca.getMunicipios().isEmpty());
        cundinamarca.agregarMunicipio(bogota);
        assertEquals(1, cundinamarca.getMunicipios().size());
    }

    @Test
    void testProcesoElectoral_Negativa_AgregarDepartamentoNulo() {
        ProcesoElectoral proceso = new ProcesoElectoral("PR-01", "Congreso", LocalDate.of(2026, 3, 8));
        proceso.agregarDepartamento(null);
        assertNull(proceso.getDepartamentos().get(0));
    }

    @Test
    void testProcesoElectoral_Limite_ListaDepartamentosVaciaYCambioEstado() {
        ProcesoElectoral proceso = new ProcesoElectoral("PR-01", "Congreso", LocalDate.of(2026, 3, 8));
        assertTrue(proceso.getDepartamentos().isEmpty());
        assertEquals("EN_CONFIGURACION", proceso.getEstado());

        proceso.cambiarEstado("EN_CURSO");
        assertEquals("EN_CURSO", proceso.getEstado());
    }
}