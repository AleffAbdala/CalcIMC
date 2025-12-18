package com.example.calcimc.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calcimc.ui.theme.Blue
import com.example.calcimc.ui.theme.White
import com.example.calcimc.viewmodel.ActivityLevel
import com.example.calcimc.viewmodel.HealthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(
    viewModel: HealthViewModel,
    onGoToHistory: () -> Unit
) {

    // ===== ESTADOS DA VIEW =====
    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var isMale by remember { mutableStateOf(true) }

    // ===== ATIVIDADE =====
    var expanded by remember { mutableStateOf(false) }
    var selectedActivity by remember { mutableStateOf(ActivityLevel.SEDENTARY) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Calculadora de Saúde") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Blue,
                    titleContentColor = White
                )
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(White)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {

            // ===== ALTURA =====
            OutlinedTextField(
                value = height,
                onValueChange = { value ->
                    if (!value.contains("-")) {
                        val number = value.toIntOrNull()
                        if (number == null || number <= 250) {
                            height = value
                            viewModel.clearError()
                        }}
                },
                label = { Text("Altura (cm)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = viewModel.hasError.value,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // ===== PESO =====
            OutlinedTextField(
                value = weight,
                onValueChange = { value ->
                    if (!value.contains("-") && value.length <= 7) {
                        weight = value
                        viewModel.clearError()
                    }
                },
                label = { Text("Peso (kg)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                isError = viewModel.hasError.value,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // ===== IDADE =====
            OutlinedTextField(
                value = age,
                onValueChange = { value ->
                    if (!value.contains("-") && value.length <= 3) {
                        age = value
                        viewModel.clearError()
                    }
                },
                label = { Text("Idade") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = viewModel.hasError.value,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // ===== SEXO =====
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = isMale,
                    onClick = {
                        isMale = true
                        viewModel.clearError()
                    }
                )
                Text("Masculino")

                Spacer(modifier = Modifier.width(16.dp))

                RadioButton(
                    selected = !isMale,
                    onClick = {
                        isMale = false
                        viewModel.clearError()
                    }
                )
                Text("Feminino")
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ===== NÍVEL DE ATIVIDADE =====
            Text(
                text = "Nível de atividade",
                fontWeight = FontWeight.Bold
            )

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {

                OutlinedTextField(
                    value = selectedActivity.label,
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = null
                        )
                    }
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    ActivityLevel.values().forEach { level ->
                        DropdownMenuItem(
                            text = { Text(level.label) },
                            onClick = {
                                selectedActivity = level
                                expanded = false
                                viewModel.clearError()
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ===== MENSAGEM DE ERRO =====
            if (viewModel.hasError.value) {
                Text(
                    text = "Dados inválidos. Verifique valores positivos e altura realista.",
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))
            }

            // ===== BOTÃO CALCULAR =====
            Button(
                onClick = {
                    viewModel.calculateIMC(height, weight)
                    viewModel.calculateTMB(weight, height, age, isMale)
                    viewModel.calculateIdealWeight(height, isMale)
                    viewModel.calculateDailyCalories(selectedActivity)

                    viewModel.saveRecord(
                        height = height,
                        weight = weight,
                        age = age,
                        isMale = isMale,
                        activityLevel = selectedActivity
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = Blue),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "CALCULAR",
                    color = White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }

            // ===== BOTÃO HISTÓRICO =====
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = onGoToHistory,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("VER HISTÓRICO")
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ===== RESULTADOS =====
            if (viewModel.imcResult.value.isNotBlank()) {
                Text(
                    text = viewModel.imcResult.value,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Blue,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            if (viewModel.tmbResult.value.isNotBlank()) {
                Text(
                    text = viewModel.tmbResult.value,
                    fontSize = 16.sp,
                    color = Blue,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            if (viewModel.idealWeightResult.value.isNotBlank()) {
                Text(
                    text = viewModel.idealWeightResult.value,
                    fontSize = 16.sp,
                    color = Blue,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            if (viewModel.dailyCaloriesResult.value.isNotBlank()) {
                Text(
                    text = viewModel.dailyCaloriesResult.value,
                    fontSize = 16.sp,
                    color = Blue,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
