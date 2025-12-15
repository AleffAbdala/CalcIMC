package com.example.calcimc.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calcimc.datasource.db.HealthRecord
import com.example.calcimc.ui.theme.Blue
import com.example.calcimc.viewmodel.HealthViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    viewModel: HealthViewModel,
    onBack: () -> Unit,
    onGoToDetail: (Long) -> Unit
) {
    val history by viewModel.history.collectAsState(
        initial = emptyList()
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Histórico de Medições") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {

            if (history.isEmpty()) {
                Text(text = "Nenhuma medição registrada ainda.")
                return@Column
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(history) { record ->
                    HistoryItem(
                        record = record,
                        onClick = {
                            onGoToDetail(record.id)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun HistoryItem(
    record: HealthRecord,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {

            Text(
                text = formatDate(record.createdAt),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Blue
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(text = "IMC: ${"%.2f".format(record.imc)}")
            Text(text = record.imcClass)

            Spacer(modifier = Modifier.height(6.dp))

            Text(text = "TMB: ${record.tmb} kcal/dia")
            Text(text = "Peso ideal: ${"%.1f".format(record.idealWeightKg)} kg")

            // ✅ NOVO — calorias diárias
            Text(text = "Calorias/dia: ${record.dailyCalories} kcal")
        }
    }
}

private fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}
