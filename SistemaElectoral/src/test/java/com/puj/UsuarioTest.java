package com.puj;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {
    @Test void tieneRol_conRolInexistente_debeRetornarFalso() {
        Usuario usuario = new Usuario("U1", "Ana", "JURADO");
        assertFalse(usuario.tieneRol("ADMINISTRADOR"));
    }

    @Test void tieneRol_conRolExacto_debeRetornarVerdadero() {
        Usuario usuario = new Usuario("U1", "Ana", "ADMINISTRADOR");
        assertTrue(usuario.tieneRol("ADMINISTRADOR"));
    }

    @Test void tieneRol_conRolNulo_debeRetornarFalso() {
        Usuario usuario = new Usuario("U1", "Ana", "JURADO");
        assertFalse(usuario.tieneRol(null));
    }


    @Test void setters_debenActualizarNombreYRol() {
        Usuario usuario = new Usuario("U1", "Ana", "JURADO");
        usuario.setNombre("Ana Gómez");
        usuario.setRol("ADMINISTRADOR");
        assertEquals("Ana Gómez", usuario.getNombre());
        assertEquals("ADMINISTRADOR", usuario.getRol());
    }

    @Test void getters_debenRetornarDatosLimitados() {
        Usuario usuario = new Usuario("", "", "");
        assertEquals("", usuario.getId());
        assertEquals("", usuario.getNombre());
        assertEquals("", usuario.getRol());
    }

    @Test void toString_debeIncluirIdNombreYRol() {
        Usuario usuario = new Usuario("U1", "Ana", "ADMIN");
        String texto = usuario.toString();
        assertTrue(texto.contains("U1"));
        assertTrue(texto.contains("Ana"));
        assertTrue(texto.contains("ADMIN"));
    }
}
