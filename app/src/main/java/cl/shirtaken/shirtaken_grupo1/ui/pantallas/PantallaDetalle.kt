@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package cl.shirtaken.shirtaken_grupo1.ui.pantallas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import cl.shirtaken.shirtaken_grupo1.model.Polera
import cl.shirtaken.shirtaken_grupo1.ui.components.AppTopBar
import cl.shirtaken.shirtaken_grupo1.ui.components.PriceTag
import cl.shirtaken.shirtaken_grupo1.viewmodel.PolerasViewModel
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.Arrangement

@Composable
fun PantallaDetalle(
    id: Int,
    volver: () -> Unit,
    agregarAlCarrito: (Polera) -> Unit,
    abrirCarrito: () -> Unit,
    vm: PolerasViewModel = viewModel()
) {
    var polera by remember { mutableStateOf<Polera?>(null) }
    var cargando by remember { mutableStateOf(true) }

    LaunchedEffect(id) {
        polera = vm.obtenerPorId(id)
        cargando = false
    }

    Scaffold(
        topBar = { AppTopBar("Detalle", onBack = volver) }
    ) { padding ->
        if (cargando) {
            Box(Modifier.fillMaxSize().padding(padding)) {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            }
            return@Scaffold
        }

        val p = polera
        if (p == null) {
            Box(Modifier.fillMaxSize().padding(padding)) {
                Text("No se encontró la pólera", Modifier.align(Alignment.Center))
            }
            return@Scaffold
        }

        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                AsyncImage(
                    model = p.urlImagen,
                    contentDescription = p.nombre,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(16.dp))
                )
            }
            Spacer(Modifier.height(16.dp))
            Text(p.nombre, style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(4.dp))
            Text("${p.marca} • ${p.talla} • ${p.color}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(8.dp))
            PriceTag(p.precio)
            Spacer(Modifier.height(8.dp))
            val stockText = if (p.conStock) "Disponible para envío inmediato" else "Temporalmente sin stock"
            val stockColor = if (p.conStock) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
            Text(stockText, color = stockColor, style = MaterialTheme.typography.bodySmall)

            Spacer(Modifier.height(20.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = abrirCarrito, modifier = Modifier.weight(1f)) { Text("Ver carrito") }
                Button(
                    onClick = { if (p.conStock) agregarAlCarrito(p) },
                    enabled = p.conStock,
                    modifier = Modifier.weight(1.2f)
                ) {
                    Text(if (p.conStock) "Agregar al carrito" else "Sin stock")
                }
            }
        }
    }
}
