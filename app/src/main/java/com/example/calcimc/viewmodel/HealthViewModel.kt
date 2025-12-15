package com.example.calcimc.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calcimc.datasource.Calculations
import com.example.calcimc.datasource.db.HealthRecord
import com.example.calcimc.datasource.db.HealthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

// 🔹 NÍVEL DE ATIVIDADE (OPÇÃO A — RECOMENDADA)
enum class ActivityLevel(val label: String, val factor: Double) {
    SEDENTARY("Sedentário", 1.2),
    LIGHT("Leve", 1.375),
    MODERATE("Moderado", 1.55),
    INTENSE("Intenso", 1.725)
}

class HealthViewModel(
    private val repository: HealthRepository
) : ViewModel() {

    // ====== RESULTADOS ======
    var imcResult = mutableStateOf("")
        private set

    var tmbResult = mutableStateOf("")
        private set

    var idealWeightResult = mutableStateOf("")
        private set

    var dailyCaloriesResult = mutableStateOf("")
        private set

    // ====== ESTADO DE ERRO ======
    var hasError = mutableStateOf(false)
        private set

    var errorMessage = mutableStateOf<String?>(null)
        private set

    private fun setError(message: String) {
        hasError.value = true
        errorMessage.value = message
    }

    fun clearError() {
        hasError.value = false
        errorMessage.value = null
    }

    // ====== HISTÓRICO (ROOM) ======
    val history: Flow<List<HealthRecord>> = repository.getAll()

    fun getRecordById(id: Long): Flow<HealthRecord?> =
        repository.getById(id)

    // ====== VALIDAÇÃO CENTRAL ======
    private fun validateInputs(
        height: String,
        weight: String,
        age: String
    ): Boolean {

        val h = height.toDoubleOrNull()
        val w = weight.replace(",", ".").toDoubleOrNull()
        val a = age.toIntOrNull()

        if (h == null || w == null || a == null) {
            setError("Preencha todos os campos corretamente.")
            return false
        }

        if (h <= 0 || w <= 0 || a <= 0) {
            setError("Altura, peso e idade devem ser maiores que zero.")
            return false
        }

        // 🔹 Altura irrealista
        if (h < 50 || h > 250) {
            setError("Altura fora do intervalo realista (50 a 250 cm).")
            return false
        }

        // 🔹 Idade irrealista
        if (a > 120) {
            setError("Idade fora do intervalo realista.")
            return false
        }

        clearError()
        return true
    }

    // ====== IMC ======
    fun calculateIMC(height: String, weight: String) {
        val h = height.toDoubleOrNull()
        val w = weight.replace(",", ".").toDoubleOrNull()

        if (h == null || w == null || h <= 0 || w <= 0) {
            setError("Altura e peso inválidos.")
            return
        }

        Calculations.calculateIMC(height, weight) { result, _ ->
            imcResult.value = result
        }
    }

    // ====== TMB (Mifflin-St Jeor) ======
    fun calculateTMB(
        weight: String,
        height: String,
        age: String,
        isMale: Boolean
    ) {
        if (!validateInputs(height, weight, age)) return

        val w = weight.replace(",", ".").toDouble()
        val h = height.toDouble()
        val a = age.toInt()

        val tmb = if (isMale) {
            (10 * w) + (6.25 * h) - (5 * a) + 5
        } else {
            (10 * w) + (6.25 * h) - (5 * a) - 161
        }

        tmbResult.value = "TMB: ${tmb.roundToInt()} kcal/dia"
    }

    // ====== PESO IDEAL (Devine) ======
    fun calculateIdealWeight(height: String, isMale: Boolean) {
        val h = height.toDoubleOrNull()

        if (h == null || h <= 0) {
            setError("Altura inválida.")
            return
        }

        val heightInches = h / 2.54
        val inchesAboveFiveFeet = heightInches - 60

        val idealWeight = if (isMale) {
            50 + (2.3 * inchesAboveFiveFeet)
        } else {
            45.5 + (2.3 * inchesAboveFiveFeet)
        }

        idealWeightResult.value =
            "Peso ideal: ${"%.1f".format(idealWeight)} kg"
    }

    // ====== NECESSIDADE CALÓRICA DIÁRIA ======
    fun calculateDailyCalories(activityLevel: ActivityLevel) {
        val tmbValue = tmbResult.value
            .filter { it.isDigit() }
            .toIntOrNull() ?: return

        val calories = (tmbValue * activityLevel.factor).roundToInt()

        dailyCaloriesResult.value =
            "Necessidade calórica diária: $calories kcal/dia"
    }

    // ====== SALVAR NO ROOM ======
    fun saveRecord(
        height: String,
        weight: String,
        age: String,
        isMale: Boolean,
        activityLevel: ActivityLevel
    ) {
        if (!validateInputs(height, weight, age)) return
        if (dailyCaloriesResult.value.isBlank()) return

        val record = HealthRecord(
            createdAt = System.currentTimeMillis(),
            heightCm = height.toDouble(),
            weightKg = weight.replace(",", ".").toDouble(),
            age = age.toInt(),
            isMale = isMale,
            imc = imcResult.value
                .filter { it.isDigit() || it == '.' }
                .toDouble(),
            imcClass = imcResult.value,
            tmb = tmbResult.value
                .filter { it.isDigit() }
                .toInt(),
            idealWeightKg = idealWeightResult.value
                .filter { it.isDigit() || it == '.' }
                .toDouble(),
            activityFactor = activityLevel.factor,
            dailyCalories = dailyCaloriesResult.value
                .filter { it.isDigit() }
                .toInt()
        )

        viewModelScope.launch {
            repository.insert(record)
        }
    }
}
