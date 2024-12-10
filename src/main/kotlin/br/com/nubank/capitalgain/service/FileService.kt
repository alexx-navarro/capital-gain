package br.com.nubank.capitalgain.service

import br.com.nubank.capitalgain.model.OperationRequest
import br.com.nubank.capitalgain.service.GsonService.parseJson
import java.io.File

object FileService {

    fun checkIsFileAndRead(filePathOrLine: String): List<List<OperationRequest>> {
        val file = File(filePathOrLine.removeSurrounding("\""))

        return when {
            file.exists() && file.isFile -> readLines(file)
            file.exists() && file.isDirectory -> throw Exception("$filePathOrLine | O caminho é um diretório.")
            else -> parseJson(filePathOrLine)
        }
    }

    private fun readLines(file: File): List<List<OperationRequest>> {
        return parseJson(file.readText())
    }
}