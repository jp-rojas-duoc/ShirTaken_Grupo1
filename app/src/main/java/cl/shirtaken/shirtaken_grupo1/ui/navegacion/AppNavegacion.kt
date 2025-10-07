package cl.shirtaken.shirtaken_grupo1.ui.navegacion

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import cl.shirtaken.shirtaken_grupo1.ui.pantallas.*
import cl.shirtaken.shirtaken_grupo1.viewmodel.CarritoViewModel

// Rutas centralizadas
private sealed class Ruta(val valor: String) {
    data object Inicio   : Ruta("inicio")
    data object Catalogo: Ruta("catalogo")
    data object Carrito : Ruta("carrito")
    data object Pago    : Ruta("pago")
    data object Detalle : Ruta("detalle/{id}") {
        fun crear(id: Int) = "detalle/$id"
        const val ARG_ID = "id"
    }
}

@Composable
fun AppNavegacion() {
    val nav = rememberNavController()
    // VM del carrito compartido entre pantallas
    val vmCarrito = remember { CarritoViewModel() }

    NavHost(navController = nav, startDestination = Ruta.Inicio.valor) {

        composable(Ruta.Inicio.valor) {
            PantallaInicio(
                abrirCatalogo = { nav.navigateSingleTop(Ruta.Catalogo.valor) },
                abrirCarrito  = { nav.navigateSingleTop(Ruta.Carrito.valor) }
            )
        }

        composable(Ruta.Catalogo.valor) {
            PantallaCatalogo(
                abrirDetalle = { id -> nav.navigateSingleTop(Ruta.Detalle.crear(id)) },
                abrirCarrito = { nav.navigateSingleTop(Ruta.Carrito.valor) }
            )
        }

        composable(
            route = Ruta.Detalle.valor,
            arguments = listOf(navArgument(Ruta.Detalle.ARG_ID) { type = NavType.IntType })
        ) { back ->
            val id = back.arguments?.getInt(Ruta.Detalle.ARG_ID) ?: 0
            PantallaDetalle(
                id = id,
                volver = { nav.popBackStack() },
                agregarAlCarrito = { polera -> vmCarrito.agregar(polera) }, // ✅ agrega
                abrirCarrito = { nav.navigateSingleTop(Ruta.Carrito.valor) } // ✅ navega
            )
        }

        composable(Ruta.Carrito.valor) {
            PantallaCarrito(
                vm = vmCarrito,
                volver = { nav.popBackStack() },
                irAPago = { nav.navigateSingleTop(Ruta.Pago.valor) }
            )
        }

        composable(Ruta.Pago.valor) {
            PantallaCheckout(
                vm = vmCarrito,
                cancelar = { nav.popBackStack() },
                finalizar = {
                    vmCarrito.limpiar()
                    nav.navigate(Ruta.Inicio.valor) {
                        popUpTo(Ruta.Inicio.valor) { inclusive = false }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}

// Extensión para no duplicar pantallas al navegar
private fun androidx.navigation.NavController.navigateSingleTop(ruta: String) {
    this.navigate(ruta) { launchSingleTop = true }
}
