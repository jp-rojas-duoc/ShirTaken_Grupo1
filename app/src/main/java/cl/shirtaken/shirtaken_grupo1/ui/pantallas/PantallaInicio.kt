@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package cl.shirtaken.shirtaken_grupo1.ui.pantallas

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PantallaInicio(abrirCatalogo: () -> Unit) {
    Scaffold(topBar = { CenterAlignedTopAppBar(title = { Text("ShirTaken") }) }) { p ->
        Column(
            modifier = Modifier.fillMaxSize().padding(p).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Bienvenido a ShirTaken", style = MaterialTheme.typography.headlineSmall)
            Button(onClick = abrirCatalogo) { Text("Explorar catálogo") }
        }
    }
}
