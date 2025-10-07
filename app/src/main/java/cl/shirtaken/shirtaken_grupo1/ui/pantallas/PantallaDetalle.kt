@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package cl.shirtaken.shirtaken_grupo1.ui.pantallas

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import cl.shirtaken.shirtaken_grupo1.model.Polera
import cl.shirtaken.shirtaken_grupo1.viewmodel.PolerasViewModel

@Composable
fun PantallaDetalle(
    id: Int,
    vm: PolerasViewModel = PolerasViewModel(),
    volver: () -> Unit,
    agregarAlCarrito: (Polera) -> Unit,   // 👈 recibe la acción para agregar
    abrirCarrito: () -> Unit              // 👈 y para navegar al carrito
) {
    val polera = vm.obtenerPorId(id)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(polera?.nombre ?: "Detalle") },
                navigationIcon = { TextButton(onClick = volver) { Text("Volver") } },
                actions = { TextButton(onClick = abrirCarrito) { Text("Carrito") } }
            )
        }
    ) { p ->
        if (polera == null) {
            Box(Modifier.fillMaxSize().padding(p), contentAlignment = Alignment.Center) {
                Text("Producto no encontrado")
            }
        } else {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(p)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = polera.urlImagen,
                        placeholder = painterResource(android.R.drawable.ic_menu_report_image),
                        error = painterResource(android.R.drawable.ic_menu_report_image)
                    ),
                    contentDescription = polera.nombre,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp),
                    contentScale = ContentScale.Crop
                )

                Text("${polera.marca} · Talla ${polera.talla} · ${polera.color}")
                Text("Precio: $${polera.precio}", style = MaterialTheme.typography.titleMedium)

                Button(
                    onClick = {
                        agregarAlCarrito(polera) // ✅ agrega al VM compartido
                        abrirCarrito()            // ✅ navega al carrito
                    },
                    enabled = polera.conStock,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (polera.conStock) "Agregar al carrito" else "Sin stock")
                }
            }
        }
    }
}
