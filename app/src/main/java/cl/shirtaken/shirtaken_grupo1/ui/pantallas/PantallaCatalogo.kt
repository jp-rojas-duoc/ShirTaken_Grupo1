@file:OptIn(
    androidx.compose.material3.ExperimentalMaterial3Api::class,
    androidx.compose.animation.ExperimentalAnimationApi::class
)

package cl.shirtaken.shirtaken_grupo1.ui.pantallas

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.shirtaken.shirtaken_grupo1.model.Polera
import cl.shirtaken.shirtaken_grupo1.viewmodel.PolerasViewModel
import coil.compose.SubcomposeAsyncImage

@Composable
fun PantallaCatalogo(
    abrirDetalle: (Int) -> Unit,
    abrirCarrito: () -> Unit,
    vm: PolerasViewModel = viewModel()
) {
    val catalogo by vm.catalogo.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("👕 Catálogo Shirtaken") },
                actions = {
                    IconButton(onClick = abrirCarrito) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Ver Carrito"
                        )
                    }
                }
            )
        }
    ) { padding ->

        if (catalogo.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {

                Text("No hay poleras disponibles.")
            }
        } else {

            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 160.dp),
                modifier = Modifier.fillMaxSize(),
                contentPadding = padding,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                content = {
                    items(catalogo, key = { it.id }) { polera ->

                        AnimatedVisibility(
                            visible = true,
                            enter = fadeIn(animationSpec = tween(500)) + slideInVertically(
                                initialOffsetY = { it / 2 },
                                animationSpec = tween(500)
                            )
                        ) {
                            TarjetaPoleraGrid(polera = polera, onClick = { abrirDetalle(polera.id) })
                        }
                    }
                }
            )
        }
    }
}

@Composable
private fun TarjetaPoleraGrid(polera: Polera, onClick: () -> Unit) {
    ElevatedCard(
        modifier = Modifier.clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            SubcomposeAsyncImage(
                model = polera.urlImagen,
                loading = { CircularProgressIndicator(modifier = Modifier.padding(32.dp)) },
                contentDescription = polera.nombre,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                contentScale = ContentScale.Crop
            )


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Text(
                    text = polera.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis // Evita que textos largos rompan el diseño
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "$${polera.precio}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}