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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.shirtaken.shirtaken_grupo1.model.Polera
import cl.shirtaken.shirtaken_grupo1.viewmodel.PolerasViewModel
import coil.compose.AsyncImage
@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun PantallaCatalogo(
    abrirDetalle: (Int) -> Unit,
    abrirCarrito: () -> Unit,
    vm: PolerasViewModel = viewModel()
) {
    val catalogo by vm.catalogo.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Catálogo") },
                actions = { TextButton(onClick = abrirCarrito) { Text("Carrito") } }
            )
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = padding) {
            items(catalogo) { p ->
                TarjetaPolera(p) { abrirDetalle(p.id) }
            }
        }
    }
}

@Composable
private fun TarjetaPolera(p: Polera, onClick: () -> Unit) {
    ListItem(
        headlineContent = { Text(p.nombre) },
        supportingContent = { Text("${p.marca} • ${p.talla} • ${p.color} • $${p.precio}") },
        leadingContent = { AsyncImage(model = p.urlImagen, contentDescription = p.nombre) },
        modifier = Modifier.clickable(onClick = onClick)
    )
    HorizontalDivider()
}
