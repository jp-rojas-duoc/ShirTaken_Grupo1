@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package cl.shirtaken.shirtaken_grupo1.ui.pantallas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.shirtaken.shirtaken_grupo1.data.local.PedidoConItems
import cl.shirtaken.shirtaken_grupo1.viewmodel.HistorialViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun PantallaHistorial(
    volver: () -> Unit,
    vm: HistorialViewModel = viewModel()
) {
    val historial by vm.historial.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Historial de compras") },
                navigationIcon = { TextButton(onClick = volver) { Text("Volver") } }
            )
        }
    ) { padding ->
        if (historial.isEmpty()) {
            Box(Modifier.padding(padding).fillMaxSize()) {
                Text("Aún no tienes compras registradas.", modifier = Modifier.padding(16.dp))
            }
        } else {
            LazyColumn(Modifier.padding(padding)) {
                items(historial) { pedido ->
                    TarjetaPedido(pedido)
                    Divider()
                }
            }
        }
    }
}

@Composable
private fun TarjetaPedido(pedido: PedidoConItems) {
    val fechaTxt = remember(pedido.pedido.fechaMs) {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        sdf.format(Date(pedido.pedido.fechaMs))
    }

    Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text("Pedido #${pedido.pedido.id} • $fechaTxt", style = MaterialTheme.typography.titleMedium)
        Text("Total: $${pedido.pedido.total}", style = MaterialTheme.typography.bodyMedium)
        pedido.items.forEach { itx ->
            Text("- ${itx.nombre} x${itx.cantidad} • $${itx.precio}", style = MaterialTheme.typography.bodySmall)
        }
    }
}
