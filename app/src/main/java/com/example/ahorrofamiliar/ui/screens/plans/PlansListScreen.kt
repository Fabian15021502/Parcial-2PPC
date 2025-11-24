package com.example.ahorrofamiliar.ui.screens.plans

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ahorrofamiliar.data.repository.RepoProvider
import com.example.ahorrofamiliar.viewmodel.PlansViewModel
import com.example.ahorrofamiliar.viewmodel.PlansViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlansListScreen(
    onPlanClick: (String) -> Unit, // Cambiado de Long a String
) {
    val vm: PlansViewModel = viewModel(
        factory = PlansViewModelFactory(RepoProvider.planRepo)
    )

    val plans by vm.plans.collectAsState()
    val loading by vm.loading.collectAsState()
    val error by vm.error.collectAsState()

    LaunchedEffect(Unit) {
        vm.loadPlans()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Planes de Ahorro") }
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

                error.isNotEmpty() -> {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = error,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { vm.loadPlans() }) {
                            Text("Reintentar")
                        }
                    }
                }

                plans.isEmpty() -> {
                    Text(
                        text = "No hay planes disponibles",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                else -> {
                    LazyColumn(modifier = Modifier.padding(16.dp)) {
                        items(
                            items = plans,
                            key = { plan -> plan.id ?: "" } // Cambiado de 0L a ""
                        ) { plan ->
                            ElevatedCard(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                                    .clickable {
                                        plan.id?.let { id -> onPlanClick(id) }
                                    }
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = plan.nombre ?: "Plan sin nombre",
                                        style = MaterialTheme.typography.titleLarge
                                    )

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Text(
                                        text = "Meta: $${plan.meta ?: 0.0}",
                                        style = MaterialTheme.typography.bodyMedium
                                    )

                                    Text(
                                        text = "Meses: ${plan.meses ?: 0}",
                                        style = MaterialTheme.typography.bodyMedium
                                    )

                                    val motivo = plan.motivo
                                    if (motivo != null && motivo.isNotBlank()) {
                                        Text(
                                            text = "Motivo: $motivo",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}