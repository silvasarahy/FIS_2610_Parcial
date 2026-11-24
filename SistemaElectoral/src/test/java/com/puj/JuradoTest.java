package com.puj;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class JuradoTest {

    @Test
    void testCrearJurado_Negativa_DocumentoONombreNuloOVacio() {
        assertThrows(IllegalArgumentException.class, () -> new Jurado("", "Sofía Castro", "3001234567"));
        assertThrows(IllegalArgumentException.class, () -> new Jurado("9001", null, "3001234567"));
        assertThrows(IllegalArgumentException.class, () -> new Jurado("   ", "Sofía Castro", "3001234567"));
    }

    @Test
    void testCrearJurado_Limite_ContactoNuloYDocumentoUnDigito() {
        Jurado jurado = new Jurado("1", "S", null);
        assertEquals("1", jurado.getDocumento());
        assertEquals("S", jurado.getNombre());
        assertNull(jurado.getContacto());
    }

    @Test
    void testToString_Negativa_NoRetornaNulo() {
        Jurado jurado = new Jurado("9001", "Sofía Castro", "3001234567");
        assertNotNull(jurado.toString());
        assertTrue(jurado.toString().contains("9001"));
    }

    @Test
    void testSetContacto_Limite_ActualizarContactoVacio() {
        Jurado jurado = new Jurado("9001", "Sofía Castro", "3001234567");
        jurado.setContacto("");
        assertEquals("", jurado.getContacto());
    }
}