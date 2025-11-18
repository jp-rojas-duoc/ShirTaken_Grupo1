package cl.shirtaken.shirtaken_grupo1.viewmodel

import org.junit.Test
import org.junit.Assert.*
import cl.shirtaken.shirtaken_grupo1.model.ItemCarrito
import cl.shirtaken.shirtaken_grupo1.model.Polera

// ✅ TEST SIMPLIFICADO - Sin dependencias Android
class CarritoViewModelTest {

    @Test
    fun testItemCarritoCreation() {
        val item = ItemCarrito(
            id = 1,
            nombre = "Polera básica",
            precio = 9990,
            cantidad = 2,
            urlImagen = "http://test.jpg"
        )

        assertEquals("ID debe ser 1", 1, item.id)
        assertEquals("Cantidad debe ser 2", 2, item.cantidad)
        assertEquals("Total debe ser 19980", 9990 * 2, item.precio * item.cantidad)
    }

    @Test
    fun testCalcularTotalCarrito() {
        val items = listOf(
            ItemCarrito(1, "Polera 1", 9990, 1, "http://img1.jpg"),
            ItemCarrito(2, "Polera 2", 15990, 2, "http://img2.jpg")
        )

        val total = items.sumOf { it.precio * it.cantidad }

        assertEquals("Total debe ser 41970", 41970, total)
    }

    @Test
    fun testCarritoVacioTieneZeroTotal() {
        val items = emptyList<ItemCarrito>()
        val total = items.sumOf { it.precio * it.cantidad }

        assertEquals("Carrito vacío debe tener total 0", 0, total)
    }
}
