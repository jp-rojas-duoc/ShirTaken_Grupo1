@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package cl.shirtaken.shirtaken_grupo1.ui.screens
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import cl.shirtaken.shirtaken_grupo1.R

@Composable
fun HomeScreen() {
    // Scaffold es la estructura base recomendada (barra superior, contenido, etc.)
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("ShirTaken") })
        }
    ) { innerPadding ->
        // Column organiza elementos en vertical
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp) // Espaciado uniforme
        ) {
            // Logo en el centro superior
            Image(
                painter = painterResource(id = R.drawable.logo), // coloca logo.png en res/drawable
                contentDescription = "Logo ShirTaken",
                modifier = Modifier.size(120.dp)
            )

            // Texto de bienvenida
            Text(
                text = "Bienvenido a ShirTaken",
                style = MaterialTheme.typography.headlineSmall
            )

            // Botón principal
            Button(onClick = { /* TODO: acción */ }) {
                Text("Explorar catálogo")
            }

            // Botón secundario
            OutlinedButton(onClick = { /* TODO: acción */ }) {
                Text("Ver favoritos")
            }
        }
    }
}
