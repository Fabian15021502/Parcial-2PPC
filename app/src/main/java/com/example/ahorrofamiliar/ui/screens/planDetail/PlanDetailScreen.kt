package com.example.ahorrofamiliar.ui.screens.planDetail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ahorrofamiliar.data.model.*
import com.example.ahorrofamiliar.data.repository.RepoProvider
import com.example.ahorrofamiliar.viewmodel.PlanDetailViewModel
import com.example.ahorrofamiliar.viewmodel.PlanDetailViewModelFactory
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanDetailScreen(
    planId: String,
    navController: NavController
) {
    val vm: PlanDetailViewModel = viewModel(
        factory = PlanDetailViewModelFactory(RepoProvider.planRepo)
    )

    val plan by vm.plan.collectAsState()
    val payments by vm.payments.collectAsState()
    val members by vm.members.collectAsState()
    val msg by vm.message.collectAsState()
    val loading by vm.loading.collectAsState()

    val MY_USER_ID = "mi_id_unico_constante"
    val MY_USER_NAME = "Yo"

    var selectedMemberId by remember { mutableStateOf(MY_USER_ID) }
    var amountInput by remember { mutableStateOf("") }

    val memberOptions = if (members.isEmpty()) {
        listOf(Member(id = MY_USER_ID, name = MY_USER_NAME))
    } else {
        members
    }

    LaunchedEffect(memberOptions) {
        if (memberOptions.isNotEmpty() && selectedMemberId.isEmpty()) {
            selectedMemberId = memberOptions.first().id ?: MY_USER_ID
        }
    }

    LaunchedEffect(planId) {
        vm.loadPlanDetails(planId)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Detalle del Plan",
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            when {
                loading -> CircularProgressIndicator(Modifier.align(Alignment.Center))
                plan == null -> {
                    Text(
                        "No se pudo cargar el plan",
                        modifier = Modifier.align(Alignment.Center),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                else -> {
                    val currentPlan = plan!!

                    Column(
                        Modifier
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {

                        // INFO DEL PLAN
                        ElevatedCard(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(Modifier.padding(20.dp)) {
                                Text(
                                    currentPlan.name ?: "Sin nombre",
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )

                                Divider(Modifier.padding(vertical = 12.dp))

                                InfoRow("Meta", "$${currentPlan.targetAmount ?: 0.0}")
                                InfoRow("Duración", "${currentPlan.months ?: 0} meses")
                                InfoRow("Motivo", currentPlan.motive ?: "Sin motivo")
                                InfoRow("Fecha Inicio", currentPlan.createdAt ?: "N/A")
                            }
                        }

                        // BOTÓN AGREGAR MIEMBRO
                        Button(
                            onClick = { navController.navigate("addMember/$planId") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.Person, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text("Agregar Integrante", fontWeight = FontWeight.SemiBold)
                        }

                        // REGISTRAR PAGO
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(Modifier.padding(16.dp)) {
                                Text(
                                    "Registrar Pago",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(Modifier.height(12.dp))

                                Text(
                                    "Selecciona el integrante:",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                                )

                                Spacer(Modifier.height(8.dp))

                                // Selección de miembro
                                memberOptions.forEach { member ->
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp)
                                            .clickable { selectedMemberId = member.id ?: MY_USER_ID },
                                        colors = if (selectedMemberId == member.id) {
                                            CardDefaults.cardColors(
                                                containerColor = MaterialTheme.colorScheme.primary
                                            )
                                        } else {
                                            CardDefaults.cardColors()
                                        },
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text(
                                            text = member.name ?: MY_USER_NAME,
                                            modifier = Modifier.padding(12.dp),
                                            color = if (selectedMemberId == member.id)
                                                MaterialTheme.colorScheme.onPrimary
                                            else
                                                MaterialTheme.colorScheme.onSurface,
                                            fontWeight = if (selectedMemberId == member.id)
                                                FontWeight.Bold
                                            else
                                                FontWeight.Normal
                                        )
                                    }
                                }

                                Spacer(Modifier.height(12.dp))

                                OutlinedTextField(
                                    value = amountInput,
                                    onValueChange = { amountInput = it },
                                    label = { Text("Monto del pago") },
                                    singleLine = true,
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp)
                                )

                                Spacer(Modifier.height(12.dp))

                                Button(
                                    onClick = {
                                        val monto = amountInput.toDoubleOrNull()
                                        if (monto != null && monto > 0 && selectedMemberId.isNotEmpty()) {
                                            val request = CreatePaymentRequest(
                                                planId = currentPlan.id ?: return@Button,
                                                memberId = selectedMemberId,
                                                amount = monto,
                                                date = LocalDate.now().toString()
                                            )
                                            vm.registerPayment(request)
                                            amountInput = ""
                                        }
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text("Registrar Pago", fontWeight = FontWeight.SemiBold)
                                }
                            }
                        }

                        // INTEGRANTES
                        Text(
                            "Integrantes",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )

                        if (members.isEmpty()) {
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    "No hay integrantes. (Plan personal)",
                                    modifier = Modifier.padding(16.dp),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        } else {
                            MembersList(members)
                        }

                        // PAGOS
                        Text(
                            "Pagos Registrados",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )

                        if (payments.isEmpty()) {
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    "No hay pagos registrados",
                                    modifier = Modifier.padding(16.dp),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        } else {
                            PaymentsList(payments)
                        }

                        if (msg.isNotEmpty()) {
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(msg, Modifier.padding(16.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun MembersList(members: List<Member>) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        members.forEach { member ->
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            member.name ?: "Sin nombre",
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Aporte: $${member.contributionPerMonth ?: 0.0}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
private fun PaymentsList(payments: List<Payment>) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        payments.forEach { pay ->
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            pay.date ?: "Fecha N/A",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            "ID: ${pay.memberId?.take(8) ?: "N/A"}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Text(
                        "$${pay.amount ?: 0.0}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}