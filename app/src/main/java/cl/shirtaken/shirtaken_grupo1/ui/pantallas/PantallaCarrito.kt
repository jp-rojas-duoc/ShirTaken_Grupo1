@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package cl.shirtaken.shirtaken_grupo1.ui.pantallas

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cl.shirtaken.shirtaken_grupo1.viewmodel.CarritoViewModel
import kotlinx.coroutines.launch

@Composable
fun PantallaCarrito(
    vm: CarritoViewModel,
    volver: () -> Unit,
    irAPago: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Carrito") },
                navigationIcon = { TextButton(onClick = volver) { Text("Volver") } }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Total: $${vm.total}", style = MaterialTheme.typography.titleMedium)

            AnimatedVisibility(visible = vm.items.isEmpty()) {
                Box(Modifier.fillMaxWidth().padding(vertical = 24.dp)) {
                    Text("Tu carrito está vacío. Agrega productos desde el catálogo.")
                }
            }

            AnimatedVisibility(visible = vm.items.isNotEmpty()) {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(vm.items) { it ->
                        Card(Modifier.fillMaxWidth().animateContentSize()) {
                            Row(
                                Modifier.fillMaxWidth().padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(Modifier.weight(1f)) {
                                    Text(it.nombre, style = MaterialTheme.typography.titleMedium)
                                    Text("Precio: $${it.precio}")
                                    Text("Cantidad: ${it.cantidad}")
                                }
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    OutlinedButton(onClick = { vm.restar(it.id) }) { Text("-") }
                                    OutlinedButton(
                                        onClick = {
                                            vm.sumar(
                                                id = it.id,
                                                onSinStock = {
                                                    scope.launch {
                                                        snackbarHostState.showSnackbar("Sin stock disponible para ${it.nombre}")
                                                    }
                                                }
                                            )
                                        }
                                    ) { Text("+") }
                                    OutlinedButton(onClick = { vm.eliminar(it.id) }) { Text("Eliminar") }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(8.dp))
            Button(
                onClick = irAPago,
                enabled = vm.items.isNotEmpty(),
                modifier = Modifier.fillMaxWidth()
            ) { Text("Ir a pago") }
        }
    }
}
