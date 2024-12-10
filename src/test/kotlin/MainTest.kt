import br.com.nubank.capitalgain.model.OperationRequest
import br.com.nubank.capitalgain.model.OperationResponse
import br.com.nubank.capitalgain.model.OperationType
import br.com.nubank.capitalgain.service.CapitalGainService
import br.com.nubank.capitalgain.service.FileService
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream
import java.io.File
import java.io.InputStream
import java.math.BigDecimal

class MainTest {

    private val filePath = this::class.java.classLoader.getResource("case1.json")?.path

    @Nested
    inner class InputFromUser {
        @Test
        fun `test getInputFromUser with valid input`() {

            val inputStream: InputStream = ByteArrayInputStream(filePath!!.toByteArray())
            System.setIn(inputStream)

            val result = getInputFromUser()
            assertEquals(filePath, result)
        }
    }

    @Nested
    inner class ProcessInput {

        private val operationLines = listOf(
            OperationRequest(operation = OperationType.BUY, unitCost = BigDecimal(10.00), quantity = 100),
            OperationRequest(operation = OperationType.SELL, unitCost = BigDecimal(15.00), quantity = 50),
            OperationRequest(operation = OperationType.SELL, unitCost = BigDecimal(15.00), quantity = 50)
        )
        private val taxLines =
            listOf(
                OperationResponse(tax = BigDecimal.ZERO),
                OperationResponse(tax = BigDecimal.ZERO),
                OperationResponse(tax = BigDecimal.ZERO)
            )

        @Test
        fun `test processInput with valid file line`() {

            mockkObject(FileService)
            mockkObject(CapitalGainService)

            val input = readFile(filePath!!)
            every { FileService.checkIsFileAndRead(any()) } returns listOf(operationLines)
            every { CapitalGainService.calculateTax(any()) } returns taxLines

            processInput(input)

            verify { FileService.checkIsFileAndRead(any()) }
            verify { CapitalGainService.calculateTax(any()) }

            unmockkAll()
        }
    }

    private fun readFile(path: String): String {
        return File(path).readText()
    }
}