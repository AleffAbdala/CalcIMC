package com.example.calcimc.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calcimc.ui.theme.Blue
import com.example.calcimc.viewmodel.HealthViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryDetailScreen(
    recordId: Long,
    viewModel: HealthViewModel,
    onBack: () -> Unit
) {
    val record by viewModel
        .getRecordById(recordId)
        .collectAsState(initial = null)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalhe da Medição") },
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
    ) { padding ->

        if (record == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            Text(
                text = formatDate(record!!.createdAt),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Blue
            )

            HorizontalDivider()

            Text("Altura: ${record!!.heightCm} cm")
            Text("Peso: ${record!!.weightKg} kg")
            Text("Idade: ${record!!.age}")
            Text("Sexo: ${if (record!!.isMale) "Masculino" else "Feminino"}")

            HorizontalDivider()

            Text("IMC: ${"%.2f".format(record!!.imc)}")
            Text(record!!.imcClass)

            Spacer(modifier = Modifier.height(8.dp))

            Text("TMB: ${record!!.tmb} kcal/dia")
            Text("Peso ideal: ${"%.1f".format(record!!.idealWeightKg)} kg")

            Text("TMB: ${record!!.tmb} kcal/dia")
            Text("Peso ideal: ${"%.1f".format(record!!.idealWeightKg)} kg")
            Text("Calorias diárias: ${record!!.dailyCalories} kcal/dia")

        }
    }
}

private fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}
