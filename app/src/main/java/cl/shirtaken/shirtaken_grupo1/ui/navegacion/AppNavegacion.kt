package cl.shirtaken.shirtaken_grupo1.ui.navegacion
import androidx.compose.runtime.remember

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import cl.shirtaken.shirtaken_grupo1.model.Polera
import cl.shirtaken.shirtaken_grupo1.ui.pantallas.PantallaCatalogo
import cl.shirtaken.shirtaken_grupo1.ui.pantallas.PantallaCarrito
import cl.shirtaken.shirtaken_grupo1.ui.pantallas.PantallaCheckoutLite
import cl.shirtaken.shirtaken_grupo1.ui.pantallas.PantallaDetalle
import cl.shirtaken.shirtaken_grupo1.ui.pantallas.PantallaInicio
import cl.shirtaken.shirtaken_grupo1.ui.pantallas.PantallaHistorial
import cl.shirtaken.shirtaken_grupo1.viewmodel.CarritoViewModel
import cl.shirtaken.shirtaken_grupo1.viewmodel.HistorialViewModel
import cl.shirtaken.shirtaken_grupo1.viewmodel.HistorialViewModelFactory
import cl.shirtaken.shirtaken_grupo1.repository.RepositorioPedidos
import cl.shirtaken.shirtaken_grupo1.data.remote.providePedidosApi

@Composable
fun AppNavegacion() {
    val nav = rememberNavController()
    val vmCarrito: CarritoViewModel = viewModel()

    // CREA el repositorio y el factory SOLO UNA VEZ usando remember
    val repoPedidos = remember { RepositorioPedidos(providePedidosApi()) }
    val vmHistorial: HistorialViewModel = viewModel(
        factory = HistorialViewModelFactory(repoPedidos)
    )

    NavHost(navController = nav, startDestination = "inicio") {

        composable("inicio") {
            PantallaInicio(
                abrirCatalogo = { nav.navigate("catalogo") },
                abrirCarrito  = { nav.navigate("carrito") }
            )
        }

        composable("catalogo") {
            PantallaCatalogo(
                abrirDetalle = { id -> nav.navigate("detalle/$id") },
                abrirCarrito = { nav.navigate("carrito") },
                abrirHistorial = { nav.navigate("historial") }
            )
        }

        composable(
            route = "detalle/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { entry ->
            val id = entry.arguments?.getInt("id") ?: 0
            PantallaDetalle(
                id = id,
                volver = { nav.popBackStack() },
                agregarAlCarrito = { p: Polera -> vmCarrito.agregar(p) },
                abrirCarrito = { nav.navigate("carrito") }
            )
        }

        composable("carrito") {
            PantallaCarrito(
                vm = vmCarrito,
                volver = { nav.popBackStack() },
                irAPago = { nav.navigate("checkout") }
            )
        }

        composable("checkout") {
            PantallaCheckoutLite(
                vm = vmCarrito,
                cancelar = { nav.popBackStack() },
                finalizar = {
                    nav.navigate("historial") {
                        popUpTo("catalogo") { inclusive = false }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable("historial") {
            PantallaHistorial(
                volver = { nav.popBackStack() },
                viewModel = vmHistorial
            )
        }
    }
}
