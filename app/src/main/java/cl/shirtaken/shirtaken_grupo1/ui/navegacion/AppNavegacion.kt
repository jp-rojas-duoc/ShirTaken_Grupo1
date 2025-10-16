package cl.shirtaken.shirtaken_grupo1.ui.navegacion

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
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
import cl.shirtaken.shirtaken_grupo1.ui.pantallas.PantallaCheckout
import cl.shirtaken.shirtaken_grupo1.ui.pantallas.PantallaDetalle
import cl.shirtaken.shirtaken_grupo1.ui.pantallas.PantallaInicio
import cl.shirtaken.shirtaken_grupo1.viewmodel.CarritoViewModel

@Composable
fun AppNavegacion() {
    val nav = rememberNavController()
    val vmCarrito: CarritoViewModel = viewModel()

    val duracionAnimacion = 300

    NavHost(navController = nav, startDestination = "inicio") {

        composable("inicio") {
            PantallaInicio(
                abrirCatalogo = { nav.navigate("catalogo") },
                abrirCarrito = { nav.navigate("carrito") }
            )
        }

        composable(
            route = "catalogo",
            enterTransition = { slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(duracionAnimacion)) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(duracionAnimacion)) },
            popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(duracionAnimacion)) },
            popExitTransition = { slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(duracionAnimacion)) }
        ) {
            PantallaCatalogo(
                abrirDetalle = { id -> nav.navigate("detalle/$id") },
                abrirCarrito = { nav.navigate("carrito") }
            )
        }

        composable(
            route = "detalle/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType }),
            enterTransition = { slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(duracionAnimacion)) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(duracionAnimacion)) },
            popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(duracionAnimacion)) },
            popExitTransition = { slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(duracionAnimacion)) }
        ) { entry ->
            val id = entry.arguments?.getInt("id") ?: 0
            PantallaDetalle(
                id = id,
                volver = { nav.popBackStack() },
                agregarAlCarrito = { p: Polera -> vmCarrito.agregar(p) },
                abrirCarrito = { nav.navigate("carrito") }
            )
        }

        composable(
            route = "carrito",
            enterTransition = { slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(duracionAnimacion)) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(duracionAnimacion)) },
            popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(duracionAnimacion)) },
            popExitTransition = { slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(duracionAnimacion)) }
        ) {
            PantallaCarrito(
                vm = vmCarrito,
                volver = { nav.popBackStack() },
                irAPago = { nav.navigate("checkout") }
            )
        }

        composable(
            route = "checkout",
            enterTransition = { slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(duracionAnimacion)) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(duracionAnimacion)) },
            popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(duracionAnimacion)) },
            popExitTransition = { slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(duracionAnimacion)) }
        ) {
            PantallaCheckout(
                vm = vmCarrito,
                cancelar = { nav.popBackStack() },
                finalizar = {
                    nav.navigate("catalogo") {
                        popUpTo("inicio")
                    }
                }
            )
        }
    }
}