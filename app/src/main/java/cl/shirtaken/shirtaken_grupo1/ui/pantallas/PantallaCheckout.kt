@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package cl.shirtaken.shirtaken_grupo1.ui.pantallas

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cl.shirtaken.shirtaken_grupo1.viewmodel.CarritoViewModel
import coil.compose.AsyncImage
import kotlinx.coroutines.launch

enum class MetodoPago { DEBITO, CREDITO, EFECTIVO }
enum class Entrega { RETIRO, ENVIO }

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
    var verResumen by remember { mutableStateOf(true) }
    var cupon by remember { mutableStateOf("") }
    var descuento by remember { mutableStateOf(0) }
    var metodoPago by remember { mutableStateOf(MetodoPago.DEBITO) }
    var entrega by remember { mutableStateOf(Entrega.ENVIO) }

    val snackbarHost = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val subtotal = vm.total
    val costoDespacho = if (entrega == Entrega.ENVIO && subtotal < 30000) 2990 else 0
    val total = (subtotal + costoDespacho - descuento).coerceAtLeast(0)

    val eNombre = if (nombre.trim().length < 3) "Mínimo 3 caracteres" else null
    val eEmail = if (!email.contains("@") || !email.contains(".")) "Email inválido" else null
    val eTelefono = if (telefono.filter { it.isDigit() }.length < 8) "Teléfono inválido" else null
    val eDireccion = if (entrega == Entrega.ENVIO && direccion.trim().length < 5) "Dirección muy corta" else null
    val valido = listOf(eNombre, eEmail, eTelefono, eDireccion).all { it == null } && vm.items.isNotEmpty()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Pago") },
                navigationIcon = { TextButton(onClick = cancelar) { Text("Volver") } },
                actions = { TextButton(onClick = { verResumen = !verResumen }) { Text(if (verResumen) "Ocultar" else "Ver resumen") } }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHost) }
    ) { p ->
        Column(Modifier.fillMaxSize().padding(p).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {

            Text("Resumen: ${vm.items.size} productos · Subtotal $${subtotal}", style = MaterialTheme.typography.titleMedium)

            AnimatedVisibility(visible = verResumen) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    vm.items.forEach { i ->
                        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            AsyncImage(
                                model = i.urlImagen, // CORREGIDO: usa tu campo real
                                contentDescription = i.nombre,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                            Column(Modifier.weight(1f)) {
                                Text(i.nombre, style = MaterialTheme.typography.bodyMedium)
                                Text("x${i.cantidad} • $${i.precio}")
                            }
                            Text("$${i.cantidad * i.precio}", textAlign = TextAlign.End)
                        }
                    }
                }
            }

            Text("Entrega", style = MaterialTheme.typography.titleSmall)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = entrega == Entrega.RETIRO,
                    onClick = { entrega = Entrega.RETIRO },
                    label = { Text("Retiro en tienda") }
                )
                FilterChip(
                    selected = entrega == Entrega.ENVIO,
                    onClick = { entrega = Entrega.ENVIO },
                    label = { Text("Envío a domicilio") }
                )
            }

            if (entrega == Entrega.ENVIO) {
                OutlinedTextField(
                    value = direccion,
                    onValueChange = { direccion = it },
                    label = { Text("Dirección de envío") },
                    isError = eDireccion != null,
                    supportingText = { Text(eDireccion ?: " ") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !cargando
                )
            }

            OutlinedTextField(
                value = nombre, onValueChange = { nombre = it },
                label = { Text("Nombre completo") }, isError = eNombre != null,
                supportingText = { Text(eNombre ?: " ") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !cargando
            )
            OutlinedTextField(
                value = email, onValueChange = { email = it },
                label = { Text("Email") }, isError = eEmail != null,
                supportingText = { Text(eEmail ?: " ") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !cargando
            )
            OutlinedTextField(
                value = telefono, onValueChange = { telefono = it.filter { c -> c.isDigit() }.take(12) },
                label = { Text("Teléfono") }, isError = eTelefono != null,
                supportingText = { Text(eTelefono ?: " ") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !cargando
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = cupon,
                    onValueChange = { cupon = it.uppercase() },
                    label = { Text("Cupón de descuento") },
                    modifier = Modifier.weight(1f),
                    enabled = !cargando
                )
                OutlinedButton(
                    onClick = {
                        val code = cupon.trim().uppercase()
                        val nuevoDesc = when {
                            code == "SHIRT10" -> (subtotal * 0.10).toInt()
                            code == "ENVIOFREE" -> costoDespacho
                            else -> 0
                        }
                        descuento = nuevoDesc
                        scope.launch { snackbarHost.showSnackbar(if (nuevoDesc > 0) "Cupón aplicado" else "Cupón inválido") }
                    },
                    enabled = cupon.isNotBlank() && !cargando
                ) { Text("Aplicar") }
            }

            Text("Método de pago", style = MaterialTheme.typography.titleSmall)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(selected = metodoPago == MetodoPago.DEBITO, onClick = { metodoPago = MetodoPago.DEBITO }, label = { Text("Débito") })
                FilterChip(selected = metodoPago == MetodoPago.CREDITO, onClick = { metodoPago = MetodoPago.CREDITO }, label = { Text("Crédito") })
                FilterChip(selected = metodoPago == MetodoPago.EFECTIVO, onClick = { metodoPago = MetodoPago.EFECTIVO }, label = { Text("Efectivo") })
            }

            HorizontalDivider() // CORREGIDO

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Subtotal"); Text("$${subtotal}")
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Despacho"); Text(if (costoDespacho == 0) "Gratis" else "$${costoDespacho}")
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Descuento"); Text("-$${descuento}")
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Total a pagar", style = MaterialTheme.typography.titleMedium)
                Text("$${total}", style = MaterialTheme.typography.titleMedium) // CORREGIDO: typography
            }

            if (error != null) {
                Text(error!!, color = MaterialTheme.colorScheme.error)
            }

            Button(
                onClick = {
                    cargando = true
                    error = null
                    vm.confirmarCompra(
                        onOk = {
                            cargando = false
                            scope.launch { snackbarHost.showSnackbar("Compra realizada") }
                            finalizar()
                        },
                        onError = { msg ->
                            cargando = false
                            error = msg
                            scope.launch { snackbarHost.showSnackbar(msg) }
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
