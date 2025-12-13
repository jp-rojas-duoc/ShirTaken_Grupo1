@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package cl.shirtaken.shirtaken_grupo1.ui.pantallas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
    val catalogo by vm.catalogoRemoto.collectAsStateWithLifecycle(initialValue = emptyList())
    var cargando by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        cargando = true
        error = null
        try { vm.cargarCatalogoRemoto() } catch (e: Exception) { error = e.message } finally { cargando = false }
    }

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
        when {
            cargando -> {
                Box(Modifier.fillMaxSize().padding(padding)) {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }
            }
            error != null -> {
                EmptyState(
                    title = "No pudimos cargar",
                    subtitle = error ?: "Error desconocido",
                    actionText = "Reintentar",
                    onAction = {
                        cargando = true
                        error = null
                        vm.cargarCatalogoRemoto()
                        cargando = false
                    }
                )
            }
            catalogo.isEmpty() -> {
                EmptyState(
                    title = "Aún no hay productos",
                    subtitle = "Intenta recargar el catálogo",
                    actionText = "Recargar",
                    onAction = {
                        cargando = true
                        vm.cargarCatalogoRemoto()
                        cargando = false
                    }
                )
            }
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                        .padding(top = padding.calculateTopPadding() + 12.dp, bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = catalogo,
                        key = { p -> if (p.id > 0) "p-${p.id}" else "${p.nombre}-${p.marca}" }
                    ) { p: Polera ->
                        ProductCard(p) { abrirDetalle(p.id) }
                    }
                }
            }
        }
    }
}
