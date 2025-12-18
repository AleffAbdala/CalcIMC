Calculadora de IMC e Outras Métricas de Saúde
Descrição do Projeto

Este aplicativo Android permite calcular o Índice de Massa Corporal (IMC) e outras métricas de saúde, como a Taxa Metabólica Basal (TMB), o percentual de gordura e o peso ideal, utilizando o Jetpack Compose para a interface. O aplicativo também oferece funcionalidades como persistência de dados usando Room, permitindo salvar o histórico das medições.

O projeto foi desenvolvido em Kotlin e segue a arquitetura MVVM para a separação de responsabilidades entre a camada de apresentação, lógica de negócios e dados.

Funcionalidades

Cálculo do IMC: Calcula o IMC a partir do peso e altura do usuário e classifica o resultado.

Taxa Metabólica Basal (TMB): Calcula a TMB utilizando a fórmula de Mifflin-St Jeor.

Percentual de Gordura Corporal: Estima o percentual de gordura com base em medidas como cintura, pescoço e quadril.

Peso Ideal: Calcula o peso ideal com base em fórmulas como a de Devine.

Necessidade Calórica Diária: Estima a necessidade calórica do usuário considerando sua TMB e o nível de atividade física.

Persistência de Dados: Armazena o histórico das medições em um banco de dados local utilizando Room.

Interface: Desenvolvida inteiramente com Jetpack Compose, com telas para entrada de dados, histórico e detalhes das medições.

Tecnologias Utilizadas

Kotlin

Jetpack Compose

Room Database (Persistência de Dados)

MVVM (Model-View-ViewModel)

Estrutura do Projeto
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/calcimc/
│   │   │   │   ├── MainActivity.kt
│   │   │   │   ├── HealthViewModel.kt
│   │   │   │   ├── HealthRepository.kt
│   │   │   ├── res/
│   │   │   │   ├── drawable/
│   │   │   │   ├── mipmap/
│   │   ├── androidTest/
│   │   ├── unitTest/
├── gradle/
├── .gitignore
├── build.gradle

Como Usar

Clonar o repositório:
Clone este repositório para o seu computador local:

git clone <URLdoRepositório>


Abrir o projeto no Android Studio:
Abra o Android Studio e importe o projeto clonado.

Rodar o aplicativo:
Para rodar o aplicativo, basta executar no emulador ou no dispositivo físico. A interface está dividida em telas de entrada de dados, histórico e detalhes de medições.

Requisitos

Android Studio (última versão recomendada)

Android SDK

Persistência de Dados

O aplicativo utiliza o Room para persistir o histórico das medições. A estrutura do banco de dados é definida em classes como HealthDatabase, HealthDao e HealthRepository.

Fórmulas Utilizadas

IMC (Índice de Massa Corporal):

IMC = peso / altura²


Taxa Metabólica Basal (TMB) - Mifflin-St Jeor:

Homens:

TMB = 10 * peso + 6.25 * altura - 5 * idade + 5


Mulheres:

TMB = 10 * peso + 6.25 * altura - 5 * idade - 161


Percentual de Gordura Corporal:
Fórmula baseada em medidas de cintura, pescoço e quadril.

Peso Ideal:
Fórmulas como Devine para calcular o peso ideal.

Documentação e Decisões de Arquitetura

A arquitetura foi baseada no padrão MVVM para separar claramente a camada de View (Composables) da ViewModel e da camada de dados. Usamos o StateFlow para gerenciamento de estado e Room para persistência de dados, garantindo uma experiência de usuário fluida e eficiente.

Melhorias Futuras

Integração com APIs externas de saúde: Para enriquecer os cálculos com dados de fontes confiáveis.

Autenticação de usuário: Para salvar dados pessoais e medições de forma mais segura.

Notificações: Lembretes para o usuário inserir medições periodicamente.
