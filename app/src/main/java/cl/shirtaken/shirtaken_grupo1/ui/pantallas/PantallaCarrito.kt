@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package cl.shirtaken.shirtaken_grupo1.ui.pantallas

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import cl.shirtaken.shirtaken_grupo1.viewmodel.CarritoViewModel

@Composable
fun PantallaCarrito(
    vm: CarritoViewModel,
    volver: () -> Unit,
    irAPago: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Tu carrito") },
                navigationIcon = { TextButton(onClick = volver) { Text("Volver") } }
            )
        }
    ) { p ->
        Column(Modifier.fillMaxSize().padding(p).padding(12.dp)) {

            if (vm.items.isEmpty()) {
                Text("No tienes productos en el carrito.")
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(vm.items) { it ->
                        ElevatedCard(Modifier.fillMaxWidth()) {
                            Row(
                                Modifier.padding(12.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Image(
                                    painter = rememberAsyncImagePainter(
                                        model = it.urlImagen,
                                        placeholder = painterResource(android.R.drawable.ic_menu_report_image),
                                        error = painterResource(android.R.drawable.ic_menu_report_image)
                                    ),
                                    contentDescription = it.nombre,
                                    modifier = Modifier.size(72.dp)
                                )
                                Column(Modifier.weight(1f)) {
                                    Text(it.nombre, style = MaterialTheme.typography.titleMedium)
                                    Text("Precio: $${it.precio}")
                                    Row {
                                        TextButton(onClick = { vm.restar(it.id) }) { Text("-") }
                                        Text(" ${it.cantidad} ")
                                        TextButton(onClick = { vm.sumar(it.id) }) { Text("+") }
                                    }
                                }
                                TextButton(onClick = { vm.eliminar(it.id) }) { Text("Quitar") }
                            }
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))
                Text("Total: $${vm.total}", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = irAPago,
                    enabled = vm.items.isNotEmpty(),
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Ir a pago") }
            }
        }
    }
}
