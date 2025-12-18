# Trabalho 2 — IMC e outras métricas de saúde (Relatório técnico breve)

## 1. Objetivo
O projeto implementa uma calculadora de saúde em Android (Kotlin + Jetpack Compose) com cálculo de IMC e métricas adicionais, seguindo arquitetura MVVM e persistindo medições em banco local (Room). O foco foi manter a lógica de negócio fora da interface e permitir histórico e consulta detalhada de medições.

## 2. Fórmulas utilizadas
**IMC (Índice de Massa Corporal):**  
IMC = peso_kg / (altura_m²).

**TMB (Taxa Metabólica Basal) — Mifflin–St Jeor:**  
Masculino: 10*peso + 6,25*altura - 5*idade + 5.  
Feminino: 10*peso + 6,25*altura - 5*idade - 161.  
(altura em cm e peso em kg)

**Peso ideal — Devine:**  
Altura convertida para polegadas; considera 5 pés como base (60 polegadas).  
Masculino: 50 + 2,3*(polegadas_acima_de_60).  
Feminino: 45,5 + 2,3*(polegadas_acima_de_60).

**Necessidade calórica diária:**  
Calorias/dia = TMB × fator de atividade.  
Fatores usados: sedentário (1,2), leve (1,375), moderado (1,55), intenso (1,725).

## 3. Modelo de dados e persistência (Room)
A aplicação salva um histórico de medições em banco local usando Room. Cada medição contém:
- id (autogerado)
- createdAt (data/hora em timestamp)
- heightCm, weightKg, age, isMale
- imc e classificação de IMC
- tmb
- idealWeightKg
- activityFactor e dailyCalories

A persistência foi implementada com:
- Entity: `HealthRecord`
- DAO: `HealthDao` (insert, listagem ordenada por data, busca por id)
- Database: `HealthDatabase`
- Repository: `HealthRepository`
- ViewModel: `HealthViewModel` (orquestra cálculos e gravação)

## 4. Organização e arquitetura (MVVM)
- A interface é 100% Jetpack Compose e utiliza navegação entre telas.
- O ViewModel centraliza validação, cálculos e interação com o repositório.
- O Room concentra a camada de dados, mantendo separação de responsabilidades.

## 5. Validação
Foram aplicadas validações para garantir consistência:
- campos obrigatórios
- valores positivos
- altura em faixa realista (50 cm a 250 cm)
- idade em faixa realista (até 120 anos)

## 6. Melhorias futuras
Como continuidade, o aplicativo poderia incluir:
- gráficos de evolução do IMC/TMB
- exportação do histórico para CSV/PDF
- notificações para lembrete de medições periódicas
- integração com APIs de saúde e autenticação
