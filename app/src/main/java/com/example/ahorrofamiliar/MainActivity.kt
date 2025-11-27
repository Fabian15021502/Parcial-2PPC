package com.example.ahorrofamiliar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.ahorrofamiliar.ui.nav.AppNavGraph
import com.example.ahorrofamiliar.ui.theme.AhorroFamiliarTheme
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

        setContent {
            val navController = rememberNavController()

            AhorroFamiliarTheme {
                AppNavGraph(navController)
            }
        }
    }
}
