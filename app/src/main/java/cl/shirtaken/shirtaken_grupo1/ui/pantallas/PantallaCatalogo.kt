@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package cl.shirtaken.shirtaken_grupo1.ui.pantallas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.shirtaken.shirtaken_grupo1.model.Polera
import cl.shirtaken.shirtaken_grupo1.ui.components.AppTopBar
import cl.shirtaken.shirtaken_grupo1.ui.components.EmptyState
import cl.shirtaken.shirtaken_grupo1.ui.components.ProductCard
import cl.shirtaken.shirtaken_grupo1.viewmodel.PolerasViewModel

@Composable
fun PantallaCatalogo(
    abrirDetalle: (Int) -> Unit,
    abrirCarrito: () -> Unit,
    abrirHistorial: (() -> Unit)? = null,
    vm: PolerasViewModel = viewModel()
) {
    val catalogo by vm.catalogoRemoto.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Catálogo",
                actions = {
                    TextButton(onClick = abrirCarrito) { Text("Carrito") }
                    if (abrirHistorial != null) TextButton(onClick = abrirHistorial) { Text("Historial") }
                }
            )
        }
    ) { padding ->
        if (catalogo.isEmpty()) {
            EmptyState(
                title = "Aún no hay productos",
                subtitle = "Intenta recargar el catálogo",
                actionText = "Recargar",
                onAction = { vm.cargarCatalogoRemoto() }
            )
            return@Scaffold
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = padding.calculateTopPadding() + 12.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(items = catalogo, key = { it.id }) { p: Polera ->
                ProductCard(p) { abrirDetalle(p.id) }
            }
        }
    }
}
