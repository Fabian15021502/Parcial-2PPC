package com.example.ahorrofamiliar.ui.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.ahorrofamiliar.ui.screens.plans.PlansListScreen
import com.example.ahorrofamiliar.ui.screens.planDetail.PlanDetailScreen

@Composable
fun AppNavGraph(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = "plans"
    ) {
        composable("plans") {
            PlansListScreen(
                onPlanClick = { id ->
                    navController.navigate("planDetail/$id")
                }
            )
        }

        composable("planDetail/{id}") { backStack ->
            val id = backStack.arguments?.getString("id")?.toLong()!!
            PlanDetailScreen(planId = id)
        }
    }
}
