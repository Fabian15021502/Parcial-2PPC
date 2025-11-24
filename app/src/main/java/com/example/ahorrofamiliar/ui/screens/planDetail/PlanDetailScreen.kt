package com.example.ahorrofamiliar.ui.screens.planDetail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ahorrofamiliar.data.model.CreatePaymentRequest
import com.example.ahorrofamiliar.data.repository.RepoProvider
import com.example.ahorrofamiliar.viewmodel.PlanDetailViewModel
import com.example.ahorrofamiliar.viewmodel.PlanDetailViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanDetailScreen(
    planId: Long,
) {
    val vm: PlanDetailViewModel = viewModel(
        factory = PlanDetailViewModelFactory(RepoProvider.planRepo)
    )

    val plan by vm.plan.collectAsState()
    val payments by vm.payments.collectAsState()
    val msg by vm.message.collectAsState()

    LaunchedEffect(planId) {
        vm.loadPlan(planId)
        vm.loadPayments(planId)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Detalle del Plan") }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {

            plan?.let { p ->

                Text(
                    text = "Nombre: ${p.nombre}",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(Modifier.height(6.dp))

                Text(
                    text = "Meta: ${p.meta}",
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = "Meses: ${p.meses}",
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = "Motivo: ${p.motivo}",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(Modifier.height(20.dp))

                Button(
                    onClick = {
                        val req = CreatePaymentRequest(
                            planId = p.id,
                            memberId = p.integrantes.first().id, // ejemplo
                            monto = 50000.0,
                            fecha = "2025-02-01"
                        )
                        vm.registerPayment(req)
                    }
                ) {
                    Text("Registrar Pago (Ejemplo)")
                }

                Spacer(Modifier.height(20.dp))

                Text(
                    text = "Pagos Registrados:",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(Modifier.height(10.dp))

                LazyColumn {
                    items(payments) { pay ->
                        Text("• ${pay.fecha} — ${pay.monto}")
                    }
                }
            }
        }
    }
}
