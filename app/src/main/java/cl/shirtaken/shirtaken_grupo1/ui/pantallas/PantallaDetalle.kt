@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package cl.shirtaken.shirtaken_grupo1.ui.pantallas

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
    var cargando by remember { mutableStateOf(true) }

    // ✅ Obtener por ID de forma CORRECTA
    LaunchedEffect(id) {
        try {
            polera = vm.obtenerPorId(id)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cargando = false
        }
    }

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
            val p = polera
            if (p != null && !cargando) {
                ExtendedFloatingActionButton(
                    text = { Text(if (p.conStock) "Agregar al carrito" else "Sin stock") },
                    icon = {},
                    onClick = { if (p.conStock) agregarAlCarrito(p) }
                )
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            if (cargando) {
                CircularProgressIndicator()
            } else {
                val p = polera
                if (p != null) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AsyncImage(
                            model = p.urlImagen,
                            contentDescription = p.nombre,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(250.dp)
                        )
                        Spacer(Modifier.height(12.dp))
                        Text(p.nombre, style = MaterialTheme.typography.titleLarge)
                        Text("${p.marca} • ${p.talla} • ${p.color}")
                        Text("Precio: $${p.precio}", style = MaterialTheme.typography.titleMedium)
                        Text(
                            if (p.conStock) "Con stock" else "Sin stock",
                            color = if (p.conStock) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                        )
                        Spacer(Modifier.height(8.dp))
                        Button(onClick = abrirCarrito) { Text("Ver carrito") }
                    }
                } else {
                    Text("No se encontró la pólera")
                }
            }
        }
    }
}
