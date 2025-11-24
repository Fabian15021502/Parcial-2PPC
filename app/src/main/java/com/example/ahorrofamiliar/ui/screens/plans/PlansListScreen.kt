package com.example.ahorrofamiliar.ui.screens.plans

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*   // Material 3 correcto
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ahorrofamiliar.data.repository.RepoProvider
import com.example.ahorrofamiliar.viewmodel.PlansViewModel
import com.example.ahorrofamiliar.viewmodel.PlansViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlansListScreen(
    onPlanClick: (Long) -> Unit,
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
            // SmallTopAppBar fue eliminado en Material 3 recientes
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

            if (loading) {
                CircularProgressIndicator(modifier = Modifier.padding(20.dp))
            } else {

                LazyColumn(modifier = Modifier.padding(16.dp)) {
                    items(plans) { plan ->

                        ElevatedCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .clickable { onPlanClick(plan.id) }
                        ) {

                            Column(modifier = Modifier.padding(16.dp)) {

                                Text(
                                    text = plan.nombre,
                                    style = MaterialTheme.typography.titleLarge
                                )
                                Text(
                                    text = "Meta: ${plan.meta}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = "Meses: ${plan.meses}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
