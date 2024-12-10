import br.com.nubank.capitalgain.service.CapitalGainService
import br.com.nubank.capitalgain.service.FileService
import br.com.nubank.capitalgain.service.GsonService.printJson

fun main() {
    startProcessLoop()
}

fun startProcessLoop() {
    while (true) {
        val input = getInputFromUser()

        try {
            processInput(input)
        } catch (e: Exception) {
            println("Erro ao processar o arquivo: ${e.message}")
        }

        if (!shouldContinue()) {
            println("Finalizando o programa.")
            break
        }
    }
}

fun getInputFromUser(): String {
    var input: String? = null
    while (input.isNullOrEmpty()) {
        println("Digite os valores ou o caminho do arquivo e pressione Enter:")
        input = readLine()
        if (input.isNullOrEmpty()) {
            println("Entrada inválida. Por favor, insira um valor.")
        }
    }
    return input.trim()
}

fun processInput(input: String) {
    FileService.checkIsFileAndRead(input).forEach { params ->
        printJson(CapitalGainService.calculateTax(params))
    }
}

fun shouldContinue(): Boolean {
    println("Deseja processar outro arquivo ou linha? (y/n)")
    val continueProcessing = readLine()?.trim()?.uppercase()
    return continueProcessing == "Y"
}