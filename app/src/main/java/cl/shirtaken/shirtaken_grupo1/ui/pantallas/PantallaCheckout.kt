@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package cl.shirtaken.shirtaken_grupo1.ui.pantallas

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cl.shirtaken.shirtaken_grupo1.ui.utils.Validadores
import cl.shirtaken.shirtaken_grupo1.viewmodel.CarritoViewModel

@Composable
fun PantallaCheckout(
    vm: CarritoViewModel,
    cancelar: () -> Unit,
    finalizar: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var cargando by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    val eNombre = Validadores.nombre(nombre)
    val eEmail = Validadores.email(email)
    val eTelefono = Validadores.telefono(telefono)
    val eDireccion = Validadores.direccion(direccion)

    val valido = listOf(eNombre, eEmail, eTelefono, eDireccion).all { it == null } && vm.items.isNotEmpty()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Pago") },
                navigationIcon = { TextButton(onClick = cancelar) { Text("Volver") } }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = remember { SnackbarHostState() })
        }
    ) { p ->
        Column(
            Modifier.fillMaxSize().padding(p).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Resumen: ${vm.items.size} productos · Total $${vm.total}")

            OutlinedTextField(
                value = nombre, onValueChange = { nombre = it },
                label = { Text("Nombre completo") }, isError = eNombre != null,
                supportingText = { if (eNombre != null) Text(eNombre) },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = email, onValueChange = { email = it },
                label = { Text("Email") }, isError = eEmail != null,
                supportingText = { if (eEmail != null) Text(eEmail) },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = telefono, onValueChange = { telefono = it },
                label = { Text("Teléfono") }, isError = eTelefono != null,
                supportingText = { if (eTelefono != null) Text(eTelefono) },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = direccion, onValueChange = { direccion = it },
                label = { Text("Dirección de envío") }, isError = eDireccion != null,
                supportingText = { if (eDireccion != null) Text(eDireccion) },
                modifier = Modifier.fillMaxWidth()
            )

            if (error != null) {
                Text(error!!, color = MaterialTheme.colorScheme.error)
            }

            Spacer(Modifier.height(8.dp))
            Button(
                onClick = {
                    cargando = true
                    error = null
                    vm.confirmarCompra(
                        onOk = {
                            cargando = false
                            finalizar()
                        },
                        onError = { msg ->
                            cargando = false
                            error = msg
                        }
                    )
                },
                enabled = valido && !cargando,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (cargando) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(Modifier.width(8.dp))
                }
                Text("Confirmar compra")
            }

            if (vm.items.isEmpty()) {
                Text("Tu carrito está vacío. Vuelve y agrega productos.")
            }
        }
    }
}
