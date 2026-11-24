package com.puj;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NotificadorAlertasTest {

    private NotificadorAlertas notificador;
    private PuestoVotacion puesto;

    @BeforeEach
    void setUp() {
        notificador = new NotificadorAlertas();
        puesto = new PuestoVotacion("P-01", "Colegio Mayor", "Calle 40 # 7-10", null);
    }

    private Acta crearActaValida(String idActa, int numeroMesa) {
        Mesa mesa = new Mesa(numeroMesa, 200, puesto);
        mesa.instalar();
        mesa.cerrar();
        return new Acta(idActa, mesa);
    }

    @Test
    void testSuscribir_Negativa_NuloODuplicado() {
        assertThrows(IllegalArgumentException.class, () -> notificador.suscribir(null));

        Usuario testigo = new Usuario("U-1", "Laura Gómez", "TESTIGO");
        notificador.suscribir(testigo);
        assertThrows(IllegalArgumentException.class, () -> notificador.suscribir(testigo));
    }

    @Test
    void testSuscribir_Limite_UnObservadorSobreListaVacia() {
        Usuario autoridad = new Usuario("U-2", "Carlos Ruiz", "AUTORIDAD");
        notificador.suscribir(autoridad);
        assertEquals(1, notificador.getObservadores().size());
    }

    @Test
    void testDesuscribir_Negativa_NuloONoSuscrito() {
        Usuario analista = new Usuario("U-3", "Ana Torres", "ANALISTA");
        assertThrows(IllegalArgumentException.class, () -> notificador.desuscribir(null));
        assertThrows(IllegalStateException.class, () -> notificador.desuscribir(analista));
    }

    @Test
    void testDesuscribir_Limite_DejarCeroObservadores() {
        Usuario analista = new Usuario("U-3", "Ana Torres", "ANALISTA");
        notificador.suscribir(analista);
        notificador.desuscribir(analista);
        assertTrue(notificador.getObservadores().isEmpty());
    }

    @Test
    void testNotificarAlerta_Negativa_AlertaNula() {
        assertThrows(IllegalArgumentException.class, () -> notificador.notificarAlerta(null));
    }

    @Test
    void testNotificarAlerta_Limite_CeroObservadoresSuscritos() {
        Acta acta = crearActaValida("ACTA-01", 1);
        Alerta alerta = new Alerta("Incidente grave en mesa 1", acta);
        assertDoesNotThrow(() -> notificador.notificarAlerta(alerta));
        assertEquals(1, notificador.getHistorialAlertas().size());
    }

    @Test
    void testNotificarCierreMesa_Negativa_MesaOActaNula() {
        Mesa mesa = new Mesa(2, 150, puesto);
        mesa.instalar();
        mesa.cerrar();
        Acta acta = new Acta("ACTA-02", mesa);

        assertThrows(IllegalArgumentException.class, () -> notificador.notificarCierreMesa(null, acta));
        assertThrows(IllegalArgumentException.class, () -> notificador.notificarCierreMesa(mesa, null));
    }

    @Test
    void testNotificarCierreMesa_Limite_DifusionMultipleSimultanea() {
        Usuario testigo = new Usuario("U-1", "Laura Gómez", "TESTIGO");
        Usuario autoridad = new Usuario("U-2", "Carlos Ruiz", "AUTORIDAD");
        Usuario analista = new Usuario("U-3", "Ana Torres", "ANALISTA");

        notificador.suscribir(testigo);
        notificador.suscribir(autoridad);
        notificador.suscribir(analista);

        Mesa mesa = new Mesa(3, 150, puesto);
        mesa.instalar();
        mesa.cerrar();
        Acta acta = new Acta("ACTA-03", mesa);

        notificador.notificarCierreMesa(mesa, acta);

        assertEquals(1, testigo.getAlertasRecibidas().size());
        assertEquals(1, autoridad.getAlertasRecibidas().size());
        assertEquals(1, analista.getAlertasRecibidas().size());
    }
}