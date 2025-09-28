@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package cl.shirtaken.shirtaken_grupo1.ui.pantallas

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import cl.shirtaken.shirtaken_grupo1.viewmodel.PolerasViewModel

@Composable
fun PantallaDetalle(
    id: Int,
    vm: PolerasViewModel = PolerasViewModel(),
    volver: () -> Unit
) {
    val polera = vm.obtenerPorId(id)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(polera?.nombre ?: "Detalle") },
                navigationIcon = { TextButton(onClick = volver) { Text("Volver") } }
            )
        }
    ) { p ->
        if (polera == null) {
            Box(Modifier.fillMaxSize().padding(p), contentAlignment = Alignment.Center) {
                Text("Producto no encontrado")
            }
        } else {
            Column(
                Modifier.fillMaxSize().padding(p).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(polera.urlImagen),
                    contentDescription = polera.nombre,
                    modifier = Modifier.fillMaxWidth().height(220.dp)
                )
                Text("${polera.marca} · Talla ${polera.talla} · ${polera.color}")
                Text("Precio: $${polera.precio}", style = MaterialTheme.typography.titleMedium)
                Button(onClick = { /* TODO: agregar al carrito */ }, enabled = polera.conStock) {
                    Text(if (polera.conStock) "Agregar al carrito" else "Sin stock")
                }
            }
        }
    }
}
