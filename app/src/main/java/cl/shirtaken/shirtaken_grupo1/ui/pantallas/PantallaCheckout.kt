@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package cl.shirtaken.shirtaken_grupo1.ui.pantallas

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import cl.shirtaken.shirtaken_grupo1.viewmodel.CarritoViewModel
import cl.shirtaken.shirtaken_grupo1.repository.RepositorioPedidos
import kotlinx.coroutines.launch
import cl.shirtaken.shirtaken_grupo1.data.remote.PedidoItemDto
import cl.shirtaken.shirtaken_grupo1.data.remote.PedidoRequestDto
import cl.shirtaken.shirtaken_grupo1.data.remote.providePedidosApi


enum class EntregaLite { RETIRO, ENVIO }

@Composable
fun PantallaCheckoutLite(
    vm: CarritoViewModel,
    cancelar: () -> Unit,
    finalizar: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var entrega by remember { mutableStateOf(EntregaLite.RETIRO) }
    var cargando by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val repoPedidos = remember { RepositorioPedidos(context) }

    val valido = nombre.isNotBlank() && email.isNotBlank() && telefono.isNotBlank()
            && (entrega == EntregaLite.RETIRO || direccion.isNotBlank())

    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Checkout", style = MaterialTheme.typography.headlineSmall)

        TextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre completo") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !cargando
        )

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !cargando
        )

        TextField(
            value = telefono,
            onValueChange = { telefono = it },
            label = { Text("Teléfono") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !cargando
        )

        Text("Forma de entrega:", style = MaterialTheme.typography.bodyMedium)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            RadioButton(
                selected = entrega == EntregaLite.RETIRO,
                onClick = { entrega = EntregaLite.RETIRO },
                enabled = !cargando
            )
            Text("Retiro en local", modifier = Modifier.align(Alignment.CenterVertically))

            RadioButton(
                selected = entrega == EntregaLite.ENVIO,
                onClick = { entrega = EntregaLite.ENVIO },
                enabled = !cargando
            )
            Text("Envío a domicilio", modifier = Modifier.align(Alignment.CenterVertically))
        }

        if (entrega == EntregaLite.ENVIO) {
            TextField(
                value = direccion,
                onValueChange = { direccion = it },
                label = { Text("Dirección de envío") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !cargando
            )
        }

        HorizontalDivider()
        Text("Total: $${vm.total}", style = MaterialTheme.typography.bodyLarge)

        if (error != null) {
            Text(
                error ?: "",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = cancelar,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                enabled = !cargando,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Text("Cancelar")
            }

            Button(
                onClick = {
                    cargando = true
                    error = null

                    scope.launch {
                        try {
                            // Construir request
                            val itemsDto = vm.items.map { itc ->
                                PedidoItemDto(
                                    poleraId = itc.id.toLong(),
                                    cantidad = itc.cantidad,
                                    precioUnitario = itc.precio
                                )
                            }
                            val req = PedidoRequestDto(
                                nombreCliente = nombre,
                                email = email,
                                telefono = telefono,
                                direccion = if (entrega == EntregaLite.ENVIO) direccion else null,
                                items = itemsDto,
                                total = vm.total
                            )

                            // Llamar al backend
                            val api = providePedidosApi()
                            val resp = api.crearPedido(req)

                            // ✅ NUEVO: Guardar en Room (historial local)
                            repoPedidos.registrarPedido(vm.items, vm.total)

                            // Éxito: limpiar y finalizar
                            vm.limpiar()
                            cargando = false
                            finalizar()

                        } catch (e: Exception) {
                            cargando = false
                            error = "Error: ${e.message ?: "No se pudo crear el pedido"}"
                            e.printStackTrace()
                        }
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                enabled = valido && !cargando
            ) {
                Text(if (cargando) "Procesando..." else "Confirmar compra")
            }
        }
    }
}
