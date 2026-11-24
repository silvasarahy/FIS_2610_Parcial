package com.puj;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class SistemaElectoralFacadeTest {
    private Mesa mesaInstalada(int numero, int censo) {
        PuestoVotacion puesto = new PuestoVotacion("P1", "Puesto", "Calle 1", null);
        Mesa mesa = new Mesa(numero, censo, puesto);
        mesa.instalar();
        return mesa;
    }

    @Test void constructor_personalizado_conUsuarioNulo_debeRechazarlo() {
        assertThrows(IllegalArgumentException.class,
                () -> new SistemaElectoralFacade(null));
    }

    @Test void constructor_personalizado_conUsuarioValido_debeRegistrarAuditoria() {
        Usuario usuario = new Usuario("U1", "Ana", "ADMIN");
        SistemaElectoralFacade facade = new SistemaElectoralFacade(usuario);
        facade.registrarTerritorio("11", new Object());
        assertEquals("Ana", facade.getHistorialAuditoria().get(0).getUsuario().getNombre());
    }

    @Test void registrarTerritorio_conCodigoVacio_debeRechazarlo() {
        SistemaElectoralFacade facade = new SistemaElectoralFacade();
        assertThrows(IllegalArgumentException.class,
                () -> facade.registrarTerritorio("  ", new Object()));
    }

    @Test void registrarTerritorio_conTerritorioNulo_debeRechazarlo() {
        SistemaElectoralFacade facade = new SistemaElectoralFacade();
        assertThrows(IllegalArgumentException.class,
                () -> facade.registrarTerritorio("11", null));
    }

    @Test void registrarMesa_conMesaNula_debeRechazarla() {
        SistemaElectoralFacade facade = new SistemaElectoralFacade();
        assertThrows(IllegalArgumentException.class, () -> facade.registrarMesa(null));
    }

    @Test void registrarMesa_conCensoCero_debeAceptarCasoLimite() {
        SistemaElectoralFacade facade = new SistemaElectoralFacade();
        Mesa mesa = mesaInstalada(1, 0);
        assertDoesNotThrow(() -> facade.registrarMesa(mesa));
        assertEquals(1, facade.generarConsolidado().get("totalMesas"));
    }

    @Test void cerrarMesaYActa_conMesaInexistente_debeRechazarla() {
        SistemaElectoralFacade facade = new SistemaElectoralFacade();
        assertThrows(IllegalArgumentException.class,
                () -> facade.cerrarMesaYActa("99", "A99"));
    }

    @Test void cerrarMesaYActa_conActaValida_debeCerrarYCrearActa() {
        SistemaElectoralFacade facade = new SistemaElectoralFacade();
        Mesa mesa = mesaInstalada(1, 100);
        facade.registrarMesa(mesa);
        Acta acta = facade.cerrarMesaYActa("1", "A1");
        assertEquals("A1", acta.getId());
        assertEquals("CERRADA", mesa.getEstado());
    }

    @Test void cerrarMesaYActa_conActaDuplicada_debeRechazarla() {
        SistemaElectoralFacade facade = new SistemaElectoralFacade();
        facade.registrarMesa(mesaInstalada(1, 100));
        facade.cerrarMesaYActa("1", "A1");
        assertThrows(IllegalStateException.class,
                () -> facade.cerrarMesaYActa("1", "A2"));
    }

    @Test void radicarIncidente_conNulo_debeRechazarlo() {
        SistemaElectoralFacade facade = new SistemaElectoralFacade();
        assertThrows(IllegalArgumentException.class,
                () -> facade.radicarIncidente(null));
    }

    @Test void radicarIncidente_valido_debeAgregarlo() {
        SistemaElectoralFacade facade = new SistemaElectoralFacade();
        PuestoVotacion puesto = new PuestoVotacion("P1", "Puesto", "Calle", null);
        Incidente incidente = new Incidente("I1", "LOGISTICO", "Retraso",
                LocalDateTime.now(), puesto);
        assertSame(incidente, facade.radicarIncidente(incidente));
        assertEquals(1, facade.getIncidentes().size());
    }

    @Test void radicarReclamacion_sinMotivo_debeRechazarla() {
        assertThrows(IllegalArgumentException.class,
                () -> new Reclamacion("R1", "Ana", LocalDate.now(), " ",
                        new Acta("A1", mesaCerrada())));
    }

    @Test void radicarReclamacion_sinActa_debeRechazarla() {
        SistemaElectoralFacade facade = new SistemaElectoralFacade();
        Acta acta = new Acta("A1", mesaCerrada());
        Reclamacion reclamacion = new Reclamacion("R1", "Ana", LocalDate.now(), "Conteo", acta);
        assertThrows(IllegalArgumentException.class,
                () -> facade.radicarReclamacion("1", reclamacion));
    }

    @Test void radicarReclamacion_valida_debeImpugnarActa() {
        SistemaElectoralFacade facade = new SistemaElectoralFacade();
        facade.registrarMesa(mesaInstalada(1, 100));
        Acta acta = facade.cerrarMesaYActa("1", "A1");
        Reclamacion reclamacion = new Reclamacion("R1", "Ana", LocalDate.now(), "Conteo", acta);
        ActaDecorator decorada = facade.radicarReclamacion("1", reclamacion);
        assertTrue(decorada instanceof ActaConReclamacionDecorator);
        assertEquals(1, facade.getReclamaciones().size());
    }

    @Test void generarConsolidado_sinDatos_debeRetornarCeros() {
        SistemaElectoralFacade facade = new SistemaElectoralFacade();
        Map<String, Object> consolidado = facade.generarConsolidado();
        assertEquals(0, consolidado.get("totalTerritorios"));
        assertEquals(0, consolidado.get("totalMesas"));
        assertEquals(0, consolidado.get("mesasCerradasConActa"));
        assertThrows(UnsupportedOperationException.class,
                () -> consolidado.put("extra", 1));
    }

    @Test void generarConsolidado_conDatosLimite_debeContarVotos() {
        SistemaElectoralFacade facade = new SistemaElectoralFacade();
        facade.registrarTerritorio("11", new Object());
        facade.registrarMesa(mesaInstalada(1, 0));
        Acta acta = facade.cerrarMesaYActa("1", "A1");
        acta.setVotosBlanco(0);
        acta.setVotosNulos(0);
        Map<String, Object> consolidado = facade.generarConsolidado();
        assertEquals(1, consolidado.get("totalTerritorios"));
        assertEquals(1, consolidado.get("totalMesas"));
        assertEquals(0, consolidado.get("totalVotosEmitidos"));
    }

    @Test void getters_deListas_sinDatos_debenRetornarListasVacias() {
        SistemaElectoralFacade facade = new SistemaElectoralFacade();
        assertTrue(facade.getIncidentes().isEmpty());
        assertTrue(facade.getReclamaciones().isEmpty());
        assertTrue(facade.getHistorialAuditoria().isEmpty());
    }

    @Test void getters_deListas_debenSerInmodificables() {
        SistemaElectoralFacade facade = new SistemaElectoralFacade();
        assertThrows(UnsupportedOperationException.class,
                () -> facade.getIncidentes().add(null));
        assertThrows(UnsupportedOperationException.class,
                () -> facade.getReclamaciones().add(null));
        assertThrows(UnsupportedOperationException.class,
                () -> facade.getHistorialAuditoria().add(null));
    }

    @Test void setUsuarioAuditor_conNulo_debeRechazarlo() {
        SistemaElectoralFacade facade = new SistemaElectoralFacade();
        assertThrows(IllegalArgumentException.class,
                () -> facade.setUsuarioAuditor(null));
    }

    @Test void setUsuarioAuditor_valido_debeCambiarAuditoria() {
        SistemaElectoralFacade facade = new SistemaElectoralFacade();
        facade.setUsuarioAuditor(new Usuario("U2", "Luis", "AUDITOR"));
        facade.registrarMesa(mesaInstalada(2, 50));
        assertEquals("Luis", facade.getHistorialAuditoria().get(0).getUsuario().getNombre());
    }

    private Mesa mesaCerrada() {
        PuestoVotacion puesto = new PuestoVotacion("P1", "Puesto", "Calle", null);
        Mesa mesa = new Mesa(1, 100, puesto);
        mesa.instalar();
        mesa.cerrar();
        return mesa;
    }
}
