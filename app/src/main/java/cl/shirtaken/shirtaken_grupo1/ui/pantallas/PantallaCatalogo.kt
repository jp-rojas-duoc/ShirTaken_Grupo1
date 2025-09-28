@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package cl.shirtaken.shirtaken_grupo1.ui.pantallas

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import cl.shirtaken.shirtaken_grupo1.viewmodel.PolerasViewModel

@Composable
fun PantallaCatalogo(
    vm: PolerasViewModel = PolerasViewModel(),
    abrirDetalle: (Int) -> Unit
) {
    LaunchedEffect(Unit) { vm.cargar() }
    val items = vm.catalogo.value

    Scaffold(topBar = { CenterAlignedTopAppBar(title = { Text("Catálogo") }) }) { p ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(p).padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(items) { polera ->
                Card(
                    modifier = Modifier.fillMaxWidth().clickable { abrirDetalle(polera.id) }
                ) {
                    Row(Modifier.padding(12.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Image(
                            painter = rememberAsyncImagePainter(polera.urlImagen),
                            contentDescription = polera.nombre,
                            modifier = Modifier.size(84.dp)
                        )
                        Column(Modifier.weight(1f)) {
                            Text(polera.nombre, style = MaterialTheme.typography.titleMedium)
                            Text("${polera.marca} · Talla ${polera.talla} · ${polera.color}")
                            Text("Precio: $${polera.precio}")
                            if (!polera.conStock) Text("Sin stock", color = MaterialTheme.colorScheme.error)
                        }
                        AssistChip(
                            onClick = { vm.alternarFavorito(polera.id) },
                            label = { Text(if (polera.esFavorita) "★" else "☆") }
                        )
                    }
                }
            }
        }
    }
}
