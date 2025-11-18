@file:OptIn(androidx.compose.animation.ExperimentalAnimationApi::class)

package cl.shirtaken.shirtaken_grupo1.ui.pantallas

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.shirtaken.shirtaken_grupo1.repository.RepositorioClima
import cl.shirtaken.shirtaken_grupo1.ui.components.PrimaryButton
import cl.shirtaken.shirtaken_grupo1.ui.components.SecondaryButton
import kotlinx.coroutines.launch

@Composable
fun PantallaInicio(
    abrirCatalogo: () -> Unit,
    abrirCarrito: () -> Unit
) {
    var visible by remember { mutableStateOf(false) }
    var clima by remember { mutableStateOf("🌤️ Cargando clima...") }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        visible = true
        scope.launch {
            val repo = RepositorioClima()
            clima = repo.obtenerClimaActual()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.08f)
                    )
                )
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Banner clima
        AnimatedVisibility(
            visible = visible,
            enter = slideInVertically(initialOffsetY = { -it }) + fadeIn()
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 22.dp, bottom = 12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
                elevation = CardDefaults.cardElevation(8.dp),
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Clima en Santiago",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        clima,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 26.sp
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        "Perfecto para lucir nuestras póleras",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.92f)
                    )
                }
            }
        }

        // Centro
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AnimatedVisibility(
                visible = visible,
                enter = slideInVertically(initialOffsetY = { -it / 2 }, animationSpec = tween(900)) + fadeIn()
            ) {
                Text(
                    "👕 SHIRTAKEN",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 42.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center
                )
            }
            Spacer(Modifier.height(12.dp))
            AnimatedVisibility(visible = visible, enter = fadeIn(animationSpec = tween(900, delayMillis = 250))) {
                Text(
                    "Tu tienda premium de póleras personalizadas",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.72f),
                    textAlign = TextAlign.Center
                )
            }
            Spacer(Modifier.height(44.dp))
            AnimatedVisibility(
                visible = visible,
                enter = slideInVertically(initialOffsetY = { it / 2 }, animationSpec = tween(900, delayMillis = 200)) + fadeIn()
            ) {
                PrimaryButton(text = "Explorar Catálogo", onClick = abrirCatalogo, modifier = Modifier.fillMaxWidth(0.78f))
            }
            Spacer(Modifier.height(12.dp))
            AnimatedVisibility(
                visible = visible,
                enter = slideInVertically(initialOffsetY = { it / 2 }, animationSpec = tween(900, delayMillis = 300)) + fadeIn()
            ) {
                SecondaryButton(text = "🛒 Ver Carrito", onClick = abrirCarrito, modifier = Modifier.fillMaxWidth(0.78f))
            }
        }

        AnimatedVisibility(visible = visible, enter = fadeIn(animationSpec = tween(800, delayMillis = 400))) {
            Text(
                "✨ Los mejores diseños en póleras ✨",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 22.dp)
            )
        }
    }
}
