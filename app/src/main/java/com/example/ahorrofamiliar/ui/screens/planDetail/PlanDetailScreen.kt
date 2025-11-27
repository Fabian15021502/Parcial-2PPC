package com.example.ahorrofamiliar.ui.screens.planDetail

import Member
import Payment
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ahorrofamiliar.data.model.*
import com.example.ahorrofamiliar.data.repository.RepoProvider
import com.example.ahorrofamiliar.viewmodel.PlanDetailViewModel
import com.example.ahorrofamiliar.viewmodel.PlanDetailViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanDetailScreen(planId: String) {
    val vm: PlanDetailViewModel = viewModel(
        factory = PlanDetailViewModelFactory(RepoProvider.planRepo)
    )

    val plan by vm.plan.collectAsState()
    val payments by vm.payments.collectAsState()
    val members by vm.members.collectAsState() // NUEVO estado para miembros
    val msg by vm.message.collectAsState()
    val loading by vm.loading.collectAsState()

    LaunchedEffect(planId) {
        // Usar el nuevo método que carga todo
        vm.loadPlanDetails(planId)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Detalle del Plan") }
            )
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            when {
                loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                plan == null -> {
                    Text(
                        text = "No se pudo cargar el plan",
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp)
                    )
                }

                else -> {
                    val currentPlan = plan!!

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Nombre: ${currentPlan.nombre ?: "Sin nombre"}",
                            style = MaterialTheme.typography.titleLarge
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Meta: $${currentPlan.meta ?: 0.0}",
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Text(
                            text = "Meses: ${currentPlan.meses ?: 0}",
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Text(
                            text = "Motivo: ${currentPlan.motivo ?: "Sin motivo"}",
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Text(
                            text = "Fecha Inicio: ${currentPlan.fechaInicio ?: "N/A"}",
                            style = MaterialTheme.typography.bodySmall
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        // SECCIÓN DE INTEGRANTES - USANDO LOS MEMBERS CARGADOS
                        Text(
                            text = "Integrantes:",
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        if (members.isEmpty()) {
                            Text(
                                text = "No hay integrantes en este plan",
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        } else {
                            MembersList(members = members)
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // Botón de ejemplo para registrar pago (actualizado)
                        if (members.isNotEmpty()) {
                            val firstMember = members[0]
                            Button(
                                onClick = {
                                    val memberId = firstMember.id ?: ""
                                    val planId = currentPlan.id ?: ""

                                    if (memberId.isNotEmpty() && planId.isNotEmpty()) {
                                        val req = CreatePaymentRequest(
                                            planId = planId,
                                            memberId = memberId,
                                            monto = 50000.0,
                                            fecha = "2025-02-01"
                                        )
                                        vm.registerPayment(req)
                                    }
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Registrar Pago (Ejemplo)")
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            text = "Pagos Registrados:",
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        if (payments.isEmpty()) {
                            Text(
                                text = "No hay pagos registrados",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        } else {
                            PaymentsList(payments = payments)
                        }

                        if (msg.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer
                                )
                            ) {
                                Text(
                                    text = msg,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// NUEVO Composable para lista de miembros
@Composable
private fun MembersList(members: List<Member>) {
    LazyColumn {
        items(count = members.size) { index ->
            val member = members[index]
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = member.nombre ?: "Sin nombre",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Aporte mensual: $${member.aporteMensual ?: 0.0}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun PaymentsList(payments: List<Payment>) {
    LazyColumn {
        items(count = payments.size) { index ->
            val pay = payments[index]
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = pay.fecha ?: "Fecha N/A",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "ID Miembro: ${pay.memberId?.take(8) ?: "N/A"}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Text(
                        text = "$${pay.monto ?: 0.0}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}