@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package cl.shirtaken.shirtaken_grupo1.ui.pantallas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cl.shirtaken.shirtaken_grupo1.ui.components.AppTopBar
import cl.shirtaken.shirtaken_grupo1.ui.components.EmptyState
import cl.shirtaken.shirtaken_grupo1.ui.components.PriceTag

data class PedidoUi(
    val id: Int,
    val fecha: String,
    val total: Int,
    val estado: String
)

@Composable
fun PantallaHistorial(
    volver: () -> Unit,
    pedidos: List<PedidoUi> = emptyList()
) {
    Scaffold(
        topBar = { AppTopBar("Historial de compras", onBack = volver) }
    ) { padding ->
        if (pedidos.isEmpty()) {
            EmptyState(
                title = "Aún no has realizado compras",
                subtitle = "Explora el catálogo y encuentra tu pólera ideal"
            )
            return@Scaffold
        }

        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(pedidos) { p ->
                Card {
                    Column(Modifier.padding(12.dp)) {
                        Text("Pedido #${p.id}", style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(4.dp))
                        Text("Fecha: ${p.fecha}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(Modifier.height(6.dp))
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Estado: ${p.estado}", style = MaterialTheme.typography.bodySmall)
                            PriceTag(p.total)
                        }
                    }
                }
            }
        }
    }
}
