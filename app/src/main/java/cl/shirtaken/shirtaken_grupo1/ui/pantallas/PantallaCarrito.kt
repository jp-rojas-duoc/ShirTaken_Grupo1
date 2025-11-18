@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package cl.shirtaken.shirtaken_grupo1.ui.pantallas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import cl.shirtaken.shirtaken_grupo1.ui.components.AppTopBar
import cl.shirtaken.shirtaken_grupo1.ui.components.PriceTag
import cl.shirtaken.shirtaken_grupo1.ui.components.PrimaryButton
import cl.shirtaken.shirtaken_grupo1.ui.components.EmptyState
import cl.shirtaken.shirtaken_grupo1.viewmodel.CarritoViewModel

@Composable
fun PantallaCarrito(
    vm: CarritoViewModel,
    volver: () -> Unit,
    irAPago: () -> Unit
) {
    val items = vm.items
    val total = vm.total

    Scaffold(
        topBar = { AppTopBar("Carrito", onBack = volver) },
        bottomBar = {
            if (items.isNotEmpty()) {
                Surface(tonalElevation = 8.dp) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(Modifier.weight(1f)) {
                            Text("Total", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            PriceTag(total)
                        }
                        PrimaryButton(text = "Proceder al pago", onClick = irAPago)
                    }
                }
            }
        }
    ) { padding ->
        if (items.isEmpty()) {
            EmptyState(
                title = "Tu carrito está vacío",
                subtitle = "Agrega productos desde el catálogo"
            )
            return@Scaffold
        }

        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 12.dp)
        ) {
            items(items) { it ->
                Card(shape = RoundedCornerShape(12.dp), elevation = CardDefaults.cardElevation(4.dp)) {
                    Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        AsyncImage(
                            model = it.urlImagen,
                            contentDescription = it.nombre,
                            modifier = Modifier
                                .size(64.dp)
                                .clip(RoundedCornerShape(10.dp))
                        )
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)) {
                            Text(it.nombre, style = MaterialTheme.typography.titleMedium)
                            Spacer(Modifier.height(4.dp))
                            Text("Cantidad: ${it.cantidad}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Spacer(Modifier.height(4.dp))
                            PriceTag(it.precio * it.cantidad)
                        }
                        TextButton(onClick = { vm.eliminar(it.id) }) { Text("Eliminar") }
                    }
                }
            }
        }
    }
}
