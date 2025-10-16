@file:OptIn(
    androidx.compose.material3.ExperimentalMaterial3Api::class,
    androidx.compose.animation.ExperimentalAnimationApi::class
)

package cl.shirtaken.shirtaken_grupo1.ui.pantallas

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add // ✨ ¡CORRECCIÓN! Usamos el ícono 'Add' que sí está incluido
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.shirtaken.shirtaken_grupo1.model.Polera
import cl.shirtaken.shirtaken_grupo1.viewmodel.PolerasViewModel
import coil.compose.SubcomposeAsyncImage
import kotlinx.coroutines.launch

@Composable
fun PantallaDetalle(
    id: Int,
    volver: () -> Unit,
    agregarAlCarrito: (Polera) -> Unit,
    abrirCarrito: () -> Unit,
    vm: PolerasViewModel = viewModel()
) {
    var polera by remember { mutableStateOf<Polera?>(null) }
    var contentVisible by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(id) {
        polera = vm.obtenerPorId(id)
        contentVisible = true
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalles de la Polera") },
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
                if (p.conStock) {
                    ExtendedFloatingActionButton(
                        onClick = {
                            agregarAlCarrito(p)
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = "${p.nombre} agregado al carrito!",
                                    duration = SnackbarDuration.Short
                                )
                            }
                        },
                        // ✨ Se cambió el ícono aquí para evitar el error
                        icon = { Icon(Icons.Default.Add, contentDescription = null) },
                        text = { Text("Agregar al carrito") },
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                }
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        polera?.let { p ->
            AnimatedVisibility(
                visible = contentVisible,
                enter = fadeIn(animationSpec = tween(500)) + slideInVertically(
                    initialOffsetY = { it / 4 },
                    animationSpec = tween(500)
                )
            ) {
                Column(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        SubcomposeAsyncImage(
                            model = p.urlImagen,
                            loading = { CircularProgressIndicator(modifier = Modifier.align(Alignment.Center)) },
                            contentDescription = p.nombre,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Spacer(Modifier.height(24.dp))

                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(p.nombre, style = MaterialTheme.typography.headlineSmall)
                            Spacer(Modifier.height(8.dp))
                            Text(
                                "${p.marca} • ${p.talla} • ${p.color}",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(Modifier.height(16.dp))
                            Text(
                                "Precio: $${p.precio}",
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(Modifier.height(8.dp))
                            val stockColor = if (p.conStock) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                            Text(
                                if (p.conStock) "¡En stock!" else "Sin stock",
                                style = MaterialTheme.typography.bodyMedium,
                                color = stockColor,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(Modifier.height(24.dp))

                    Button(
                        onClick = abrirCarrito,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                    ) {
                        Text("Ver mi carrito")
                    }
                }
            }
        } ?: run {
            Box(Modifier.padding(padding).fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}