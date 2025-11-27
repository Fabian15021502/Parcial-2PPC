package com.example.ahorrofamiliar.ui.nav

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.ahorrofamiliar.data.remote.RetrofitClient
import com.example.ahorrofamiliar.data.repository.PlanRepository
import com.example.ahorrofamiliar.ui.screens.auth.LoginScreen
import com.example.ahorrofamiliar.ui.screens.createPlan.CreatePlanScreen
import com.example.ahorrofamiliar.ui.screens.member.AddMemberScreen
import com.example.ahorrofamiliar.ui.screens.planDetail.PlanDetailScreen
import com.example.ahorrofamiliar.ui.screens.plans.PlansListScreen
import com.example.ahorrofamiliar.ui.screens.register.RegisterScreen
import com.example.ahorrofamiliar.ui.screens.payment.RegisterPaymentScreen
import com.example.ahorrofamiliar.ui.viewmodel.PaymentViewModel
import com.example.ahorrofamiliar.ui.viewmodel.PaymentViewModelFactory

@Composable
fun AppNavGraph(navController: NavHostController) {

    // Instanciamos el repository con Retrofit
    val repository = PlanRepository(
        api = RetrofitClient.instance.create(com.example.ahorrofamiliar.data.api.ApiService::class.java)
    )

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {

        // CREAR PLAN
        composable("createPlan") {
            CreatePlanScreen(
                onPlanCreated = { navController.popBackStack() },
                onBack = { navController.popBackStack() }
            )
        }

        // AGREGAR MIEMBRO
        composable("addMember/{planId}") { backStackEntry ->
            val planId = backStackEntry.arguments?.getString("planId") ?: ""
            AddMemberScreen(
                planId = planId,
                onMemberAdded = { navController.popBackStack() }
            )
        }

        // LOGIN
        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("plans") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onRegisterClick = {
                    navController.navigate("register")
                }
            )
        }

        // REGISTRO
        composable("register") {
            RegisterScreen(
                onRegisterSuccess = { navController.popBackStack() },
                onBackToLogin = { navController.popBackStack() }
            )
        }

        // LISTA DE PLANES
        composable("plans") {
            PlansListScreen(
                onPlanClick = { id ->
                    navController.navigate("planDetail/$id")
                },
                onCreatePlan = { navController.navigate("createPlan") }
            )
        }

        // DETALLE DE PLAN
        composable("planDetail/{id}") { backStackEntry ->
            val planId = backStackEntry.arguments?.getString("id") ?: ""
            PlanDetailScreen(
                planId = planId,
                navController = navController
            )
        }

        // REGISTRAR PAGO
        composable("registerPayment/{planId}/{memberId}") { backStackEntry ->
            val planId = backStackEntry.arguments?.getString("planId") ?: ""
            val memberId = backStackEntry.arguments?.getString("memberId") ?: ""

            // Crear ViewModel con Factory
            val paymentViewModel: PaymentViewModel = viewModel(
                factory = PaymentViewModelFactory(repository)
            )

            RegisterPaymentScreen(
                planId = planId,
                memberId = memberId,
                viewModel = paymentViewModel
            )
        }
    }
}
