package br.com.nubank.capitalgain.service

import br.com.nubank.capitalgain.model.OperationRequest
import br.com.nubank.capitalgain.model.OperationResponse
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.io.File
import kotlin.test.assertEquals

class CapitalGainServiceTest {

    companion object {

        private const val expectedFolder = "expected-gains/"

        @JvmStatic
        fun provideTestData(): List<Pair<List<List<OperationRequest>>, List<OperationResponse>>> {

            val filesToTest = listOf(
                readFilePath("case1.json"),
                readFilePath("case2.json"),
                readFilePath("case4.json"),
                readFilePath("case5.json"),
                readFilePath("case6.json"),
                readFilePath("case7.json"),
                readFilePath("case8.json"),
            ).map { filePath ->
                FileService.checkIsFileAndRead(filePath!!)
            }

            val expectedValues = listOf(
                readFilePath("${expectedFolder}case1.json"),
                readFilePath("${expectedFolder}case2.json"),
                readFilePath("${expectedFolder}case4.json"),
                readFilePath("${expectedFolder}case5.json"),
                readFilePath("${expectedFolder}case6.json"),
                readFilePath("${expectedFolder}case7.json"),
                readFilePath("${expectedFolder}case8.json"),
            ).map { filePath ->
                GsonService.parseJsonResponse(File(filePath).readText())
            }

            return filesToTest.zip(expectedValues)
        }

        private fun readFilePath(fileName: String): String? {
            return this::class.java.classLoader.getResource(fileName)?.path
        }
    }

    @ParameterizedTest
    @MethodSource("provideTestData")
    fun testManyCaseScenarios(args: Pair<List<List<OperationRequest>>, List<OperationResponse>>) {
        val operationRequests = args.first
        val expectedResponses = args.second

        val result = CapitalGainService.calculateTax(operationRequests.first())

        assertEquals(expectedResponses, result)

    }

    @Test
    fun `calculate non valid json file (txt file with many lines)`() {

        val filePath = readFilePath("case3-case1-plus-case2.txt")
        val expectedFilePath = readFilePath("${expectedFolder}case3-case1-plus-case2.txt")
        val expectedResponse =
            GsonService.splitArrays(File(expectedFilePath).readText()).map { GsonService.parseJsonResponse(it) }

        val request = FileService.checkIsFileAndRead(filePath!!)
        request.forEachIndexed { index, value ->
            val response = CapitalGainService.calculateTax(value)
            assertEquals(response, expectedResponse[index])
        }

    }

}
