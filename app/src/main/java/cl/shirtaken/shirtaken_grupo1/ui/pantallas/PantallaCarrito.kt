@file:OptIn(
    androidx.compose.material3.ExperimentalMaterial3Api::class,
    androidx.compose.animation.ExperimentalAnimationApi::class
)

package cl.shirtaken.shirtaken_grupo1.ui.pantallas

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import cl.shirtaken.shirtaken_grupo1.viewmodel.CarritoViewModel

@Composable
fun PantallaCarrito(
    vm: CarritoViewModel,
    volver: () -> Unit,
    irAPago: () -> Unit
) {
    val colorBoton by animateColorAsState(
        targetValue = if (vm.items.isNotEmpty()) MaterialTheme.colorScheme.primary else Color.Gray,
        animationSpec = tween(500),
        label = "colorBotonAnim"
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Tu carrito") },
                navigationIcon = { TextButton(onClick = volver) { Text("Volver") } }
            )
        }
    ) { paddingValues ->
        Column(
            Modifier.fillMaxSize().padding(paddingValues).padding(12.dp)
        ) {
            if (vm.items.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No tienes productos en el carrito.")
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(vm.items, key = { item -> item.id }) { item ->
                        AnimatedVisibility(
                            visible = true,
                            exit = slideOutHorizontally(targetOffsetX = { -it }) + shrinkVertically() + fadeOut()
                        ) {
                            ElevatedCard(Modifier.fillMaxWidth()) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Image(
                                        painter = rememberAsyncImagePainter(model = item.urlImagen),
                                        contentDescription = item.nombre,
                                        modifier = Modifier.size(72.dp)
                                    )
                                    Column(Modifier.weight(1f)) {
                                        Text(item.nombre, style = MaterialTheme.typography.titleMedium)
                                        Text("Precio: $${item.precio}")
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            TextButton(onClick = { vm.restar(item.id) }) { Text("-") }
                                            Text(" ${item.cantidad} ")
                                            TextButton(onClick = { vm.sumar(item.id) }) { Text("+") }
                                        }
                                    }
                                    TextButton(onClick = { vm.eliminar(item.id) }) { Text("Quitar") }
                                }
                            }
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Total: ", style = MaterialTheme.typography.titleLarge)
                    AnimatedContent(
                        targetState = vm.total,
                        transitionSpec = {
                            if (targetState > initialState) {
                                slideInVertically { h -> h } + fadeIn() togetherWith
                                        slideOutVertically { h -> -h } + fadeOut()
                            } else {
                                slideInVertically { h -> -h } + fadeIn() togetherWith
                                        slideOutVertically { h -> h } + fadeOut()
                            }
                        },
                        label = "totalAnimado"
                    ) { totalActual ->
                        Text(
                            text = "$$totalActual",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Spacer(Modifier.height(8.dp))

                Button(
                    onClick = irAPago,
                    enabled = vm.items.isNotEmpty(),
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = colorBoton)
                ) { Text("Ir a pago") }
            }
        }
    }
}