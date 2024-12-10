# Capital Gain Tax Calculation

Este projeto contém a implementação de um serviço que calcula o imposto sobre ganhos de capital com base em uma lista de operações financeiras de compra e venda de ações. O serviço calcula o imposto a ser pago considerando lucros e prejuízos acumulados.

## Decisões Técnicas e Arquiteturais

A solução foi desenvolvida utilizando Kotlin, um linguagem moderna e concisa que oferece segurança de tipos e alta interoperabilidade com Java. Para garantir o código limpo e de fácil manutenção, o projeto adota os seguintes princípios:

1. **Uso do padrão Singleton (Objeto)**:
   O serviço de cálculo de impostos foi implementado como um **objeto Kotlin** (`CapitalGainService`, `FileService` e `GsonService`), garantindo que a lógica seja tratada por uma única instância em toda a aplicação, o que facilita o controle do estado e evita a criação de múltiplas instâncias.

2. **Imutabilidade**:
   Usamos **val** e **data classes** para garantir que os dados não sejam alterados inesperadamente, o que ajuda a evitar erros durante o cálculo dos impostos.

3. **Responsabilidade Única**:
   A classe `OperationRequest` contém todos os dados necessários para uma operação (compra ou venda), enquanto a classe `OperationResponse` encapsula o resultado do cálculo do imposto. A lógica de cálculo foi separada em métodos específicos para manter o código limpo e organizado.

4. **Armazenamento de estado**:
   O estado do serviço, como o preço médio ponderado e os prejuízos acumulados, são armazenados em variáveis globais dentro do objeto `CapitalGainService`. Um método `resetState()` foi criado para permitir o reset do estado entre execuções de testes e/ou leituras de outros arquivos durante a execução do projeto.

## Justificativa para o Uso de Frameworks e Bibliotecas

Abaixo estão as bibliotecas e frameworks utilizados neste projeto:

- **Kotlin**: Escolhida pela sua concisão e segurança de tipos, A tipagem forte do Kotlin facilita a detecção de problemas durante o desenvolvimento, promovendo um código mais seguro e fácil de manter. Além disso, sua sintaxe expressiva contribui para uma maior produtividade no desenvolvimento, tornando o código mais legível e fácil de entender.
- **JUnit 5**: Framework de testes utilizado para garantir a qualidade e a cobertura do código. Ele permite a execução de testes parametrizados, que são ideais para testar diversos cenários de entrada (operações de compra e venda).
- **MockK**: Usado para mockar objetos e funções, permitindo isolar testes de forma eficiente, especialmente para a simulação do comportamento do sistema sem a necessidade de implementações externas.
- **Gson**: Biblioteca para conversão entre JSON e objetos Kotlin, que facilita o processamento de dados de entrada e saída.

## Notas sobre o desafio

O item que mais demandou mais tempo foi o cálculo de ganhos de capital, nunca operei com ações e não tinha muita noção de como funcionava as regras, o documento de apoio foi essencial para entender os cenários e as possibilidades. 

## Como Compilar e Executar o Projeto

### Pré-requisitos

Certifique-se de ter o **JDK 11 ou superior** instalado em sua máquina. Você pode verificar isso executando o comando:

```bash
java -version
```

### Compilando o projeto

```bash
./gradlew build
```

### Executando o projeto

```bash
./gradlew run
```

### Executando os testes

```bash
./gradlew test
```

### Gerando o JAR (Caso necessário)

```bash
./gradlew shadowJar
```

