@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package cl.shirtaken.shirtaken_grupo1.ui.pantallas

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.shirtaken.shirtaken_grupo1.model.Polera
import cl.shirtaken.shirtaken_grupo1.viewmodel.PolerasViewModel
import coil.compose.AsyncImage

@Composable
fun PantallaCatalogo(
    abrirDetalle: (Int) -> Unit,
    abrirCarrito: () -> Unit,
    abrirHistorial: (() -> Unit)? = null,
    vm: PolerasViewModel = viewModel()
) {
    val catalogo: List<Polera> by vm.catalogoRemoto.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Catálogo") },
                actions = {
                    TextButton(onClick = abrirCarrito) { Text("Carrito") }
                    if (abrirHistorial != null) TextButton(onClick = abrirHistorial) { Text("Historial") }
                }
            )
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = padding) {
            items(items = catalogo, key = { it.id ?: 0L }) { p: Polera ->
                // ✅ FIX: Asegurar que siempre pasamos el ID correcto
                val idSeguro = p.id?.toInt() ?: return@items
                TarjetaPolera(p) {
                    android.util.Log.d("DEBUG", "Click en polera: ID=${p.id}, nombre=${p.nombre}")
                    abrirDetalle(idSeguro)
                }
            }
        }
    }
}

@Composable
fun TarjetaPolera(p: Polera, onClick: () -> Unit) {
    ListItem(
        headlineContent = { Text(p.nombre, style = MaterialTheme.typography.titleMedium) },
        supportingContent = { Text("${p.marca} - ${p.talla} - ${p.color} - $${p.precio}") },
        leadingContent = { AsyncImage(model = p.urlImagen, contentDescription = p.nombre) },
        modifier = Modifier.clickable(onClick = onClick)
    )
    HorizontalDivider()
}
