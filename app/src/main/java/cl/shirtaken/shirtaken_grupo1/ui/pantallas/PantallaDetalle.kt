@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package cl.shirtaken.shirtaken_grupo1.ui.pantallas

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.shirtaken.shirtaken_grupo1.model.Polera
import cl.shirtaken.shirtaken_grupo1.viewmodel.PolerasViewModel
import coil.compose.AsyncImage

@Composable
fun PantallaDetalle(
    id: Int,
    volver: () -> Unit,
    agregarAlCarrito: (Polera) -> Unit,
    abrirCarrito: () -> Unit,
    vm: PolerasViewModel = viewModel()
) {
    var polera by remember { mutableStateOf<Polera?>(null) }

    LaunchedEffect(id) { polera = vm.obtenerPorId(id) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle") },
                navigationIcon = {
                    IconButton(onClick = volver) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            polera?.let { p ->
                ExtendedFloatingActionButton(
                    onClick = { agregarAlCarrito(p) }
                ) {
                    Text("Agregar al carrito")
                }
            }
        }
    ) { padding ->
        polera?.let { p ->
            Column(Modifier.padding(padding).padding(16.dp)) {
                AsyncImage(model = p.urlImagen, contentDescription = p.nombre)
                Spacer(Modifier.height(12.dp))
                Text(p.nombre, style = MaterialTheme.typography.titleLarge)
                Text("${p.marca} • ${p.talla} • ${p.color}")
                Text("Precio: $${p.precio}")
                Text(
                    if (p.conStock) "Con stock" else "Sin stock",
                    color = if (p.conStock) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                )
                Spacer(Modifier.height(8.dp))
                Button(onClick = abrirCarrito) { Text("Ver carrito") }
            }
        } ?: run {
            Box(Modifier.padding(padding).fillMaxSize()) {
                CircularProgressIndicator()
            }
        }
    }
}
