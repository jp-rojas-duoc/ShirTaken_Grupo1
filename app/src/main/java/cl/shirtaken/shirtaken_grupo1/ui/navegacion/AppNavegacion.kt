package cl.shirtaken.shirtaken_grupo1.ui.navegacion
import androidx.compose.runtime.Applier
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import cl.shirtaken.shirtaken_grupo1.ui.pantallas.PantallaCatalogo
import cl.shirtaken.shirtaken_grupo1.ui.pantallas.PantallaDetalle
import cl.shirtaken.shirtaken_grupo1.ui.pantallas.PantallaInicio

@Composable
fun AppNavegacion () {
    val nav = rememberNavController()
    NavHost(navController = nav , startDestination = "inicio"){
        composable ("inicio") {
            PantallaInicio(abrirCatalogo = {nav.navigate("catalogo")})
        }
        composable ("catalogo") {
            PantallaCatalogo(abrirDetalle = {id -> nav.navigate("detalle/$id")})
        }
        composable (
            "detalle/{id}",
            arguments = listOf(navArgument("id") {type = NavType.IntType})
        ) {back ->
            val id = back.arguments?.getInt("id") ?: 0
            PantallaDetalle(id =id,volver ={nav.popBackStack() })
        }
    }
}