package br.com.nubank.capitalgain.model

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class OperationRequest(
    val operation: OperationType,
    @SerializedName("unit-cost")
    val unitCost: BigDecimal,
    val quantity: Int
)

fun OperationRequest.operationTotalCost(): BigDecimal {
    return unitCost.multiply(quantity.toBigDecimal())
}

enum class OperationType {
    @SerializedName("buy")
    BUY,
    @SerializedName("sell")
    SELL
}