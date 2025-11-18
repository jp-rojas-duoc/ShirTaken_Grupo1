package cl.shirtaken.shirtaken_grupo1.viewmodel

import org.junit.Test
import org.junit.Assert.*

// ✅ TEST SIMPLIFICADO - Sin dependencias de Android
class PolerasViewModelTest {

    @Test
    fun testPoleraModeloCreation() {
        // Test básico sin dependencias Android
        val polera = cl.shirtaken.shirtaken_grupo1.model.Polera(
            id = 1,
            nombre = "Polera Test",
            marca = "ShirTaken",
            precio = 9990,
            talla = "M",
            color = "Negro",
            urlImagen = "http://test.jpg"
        )

        assertEquals("ID debe ser 1", 1, polera.id)
        assertEquals("Nombre debe coincidir", "Polera Test", polera.nombre)
        assertEquals("Precio debe ser 9990", 9990, polera.precio)
    }

    @Test
    fun testPoleraStockValidation() {
        val polera = cl.shirtaken.shirtaken_grupo1.model.Polera(
            id = 2,
            nombre = "Premium",
            marca = "ShirTaken",
            precio = 25000,
            talla = "XL",
            color = "Azul",
            urlImagen = "http://premium.jpg",
            conStock = true
        )

        assertTrue("Debe tener stock", polera.conStock)
    }
}
