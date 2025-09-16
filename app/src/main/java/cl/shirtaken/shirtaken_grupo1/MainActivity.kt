package cl.shirtaken.shirtaken_grupo1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import cl.shirtaken.shirtaken_grupo1.ui.screens.HomeScreen
import cl.shirtaken.shirtaken_grupo1.ui.screens.PlaceholderScreen
import cl.shirtaken.shirtaken_grupo1.ui.theme.ShirTaken_grupo1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShirTaken_grupo1Theme {
                MaterialTheme {
                    HomeScreen()
                }
            }
        }
    }
}
