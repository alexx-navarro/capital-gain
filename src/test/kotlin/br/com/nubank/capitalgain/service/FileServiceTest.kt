package br.com.nubank.capitalgain.service

import br.com.nubank.capitalgain.model.OperationRequest
import br.com.nubank.capitalgain.model.OperationType
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class FileServiceTest {

    private val filePath = this::class.java.classLoader.getResource("case1.json")?.path
    private val directoryPath = this::class.java.classLoader.getResource("")?.path

    @BeforeEach
    fun setUp() {
        mockkObject(GsonService)
    }

    @Test
    fun `test checkIsFileAndRead when file exists and is a file`() {

        val operationRequestMock = OperationRequest(
            operation = OperationType.BUY,
            unitCost = BigDecimal(10.0),
            quantity = 5
        )

        every { GsonService.parseJson(any()) } returns listOf(listOf(operationRequestMock))

        val result = FileService.checkIsFileAndRead(filePath!!)

        verify { GsonService.parseJson(any()) }
        assertNotNull(result)
        assertEquals(1, result.size)
        assertEquals(5, result[0].first().quantity)
    }

    @Test
    fun `test checkIsFileAndRead when file exists but is a directory`() {
        val exception = assertThrows<Exception> {
            FileService.checkIsFileAndRead(directoryPath!!)
        }
        assert(exception.message?.contains("O caminho é um diretório.") == true)
    }

    @Test
    fun `test checkIsFileAndRead when file does not exist`() {

        val filePath = "path/to/nonexistent_file.json"
        val operationRequestMock = OperationRequest(
            operation = OperationType.BUY,
            unitCost = BigDecimal(10.0),
            quantity = 5
        )

        every { GsonService.parseJson(filePath) } returns listOf(listOf(operationRequestMock))

        val result = FileService.checkIsFileAndRead(filePath)

        verify { GsonService.parseJson(filePath) }
        assertNotNull(result)
        assertEquals(1, result.size)
        assertEquals(5, result[0].first().quantity)
    }

    @Test
    fun `test checkIsFileAndRead when filePath is quoted`() {

        val filePathQuoted = "\"$filePath\""
        val operationRequestMock =
            OperationRequest(operation = OperationType.SELL, unitCost = BigDecimal(20.0), quantity = 10)

        every { GsonService.parseJson(any<String>()) } returns listOf(listOf(operationRequestMock))

        val result = FileService.checkIsFileAndRead(filePathQuoted)

        assertNotNull(result)
        assertEquals(10, result[0].first().quantity)
    }

}
