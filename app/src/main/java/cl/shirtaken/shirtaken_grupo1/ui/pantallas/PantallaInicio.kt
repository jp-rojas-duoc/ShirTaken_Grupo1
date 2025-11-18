@file:OptIn(androidx.compose.animation.ExperimentalAnimationApi::class)

package cl.shirtaken.shirtaken_grupo1.ui.pantallas

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PantallaInicio(
    abrirCatalogo: () -> Unit,
    abrirCarrito: () -> Unit // Se mantiene para compatibilidad con AppNavegacion
) {
    // 1. Estado para controlar la visibilidad y disparar la animación
    var visible by remember { mutableStateOf(false) }

    // 2. Efecto que cambia el estado a `true` una sola vez cuando la pantalla se muestra
    LaunchedEffect(Unit) {
        visible = true
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        // Título animado que desliza desde arriba
        AnimatedVisibility(
            visible = visible,
            enter = slideInVertically(
                initialOffsetY = { -it / 2 },
                animationSpec = tween(durationMillis = 1000)
            ) + fadeIn(animationSpec = tween(1000))
        ) {
            Text(
                text = "👕 SHIRTAKEN",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineLarge
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Subtítulo que aparece con un fade-in
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(animationSpec = tween(2000, delayMillis = 500))
        ) {
            Text(
                text = "Tu tienda de poleras",
                style = MaterialTheme.typography.titleMedium
            )
        }

        Spacer(modifier = Modifier.height(48.dp))

        // Botón animado que desliza desde abajo
        AnimatedVisibility(
            visible = visible,
            enter = slideInVertically(
                initialOffsetY = { it / 2 },
                animationSpec = tween(durationMillis = 1000, delayMillis = 300)
            ) + fadeIn(animationSpec = tween(1000, delayMillis = 300))
        ) {
            Button(
                onClick = abrirCatalogo,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            ) {
                Text("Explorar Catálogo", fontSize = 18.sp)
            }
        }
    }
}