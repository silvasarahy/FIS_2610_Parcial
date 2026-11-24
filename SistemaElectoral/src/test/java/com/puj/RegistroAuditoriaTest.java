package com.puj;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RegistroAuditoriaTest {
    @Test void constructor_conUsuarioNulo_debeRechazarlo() {
        assertThrows(IllegalArgumentException.class,
                () -> new RegistroAuditoria(null, "Acción"));
    }

    @Test void constructor_conAccionVacia_debeRechazarla() {
        assertThrows(IllegalArgumentException.class,
                () -> new RegistroAuditoria(new Usuario("U1", "Ana", "ADMIN"), "  "));
    }

    @Test void constructor_valido_debeNormalizarLaAccion() {
        Usuario usuario = new Usuario("U1", "Ana", "ADMINISTRADOR");
        RegistroAuditoria registro = new RegistroAuditoria(usuario, "  Publicar  ");
        assertSame(usuario, registro.getUsuario());
        assertEquals("Publicar", registro.getAccion());
        assertNotNull(registro.getFechaHora());
    }

    @Test void getUsuario_enRegistroValido_debeRetornarUsuarioOriginal() {
        Usuario usuario = new Usuario("U2", "Luis", "JURADO");
        RegistroAuditoria registro = new RegistroAuditoria(usuario, "Cerrar mesa");
        assertSame(usuario, registro.getUsuario());
        assertEquals("Luis", registro.getUsuario().getNombre());
    }

    @Test void getAccion_conValorLimite_debeConservarTextoReal() {
        RegistroAuditoria registro = new RegistroAuditoria(
                new Usuario("U1", "Ana", "ADMIN"), "-");
        assertEquals("-", registro.getAccion());
    }

    @Test void getters_yToString_debenExponerContenidoCompleto() {
        Usuario usuario = new Usuario("U1", "Ana", "ADMIN");
        RegistroAuditoria registro = new RegistroAuditoria(usuario, "Publicar consolidado");
        assertNotNull(registro.getFechaHora());
        assertTrue(registro.toString().contains("Ana"));
        assertTrue(registro.toString().contains("Publicar consolidado"));
    }
}
