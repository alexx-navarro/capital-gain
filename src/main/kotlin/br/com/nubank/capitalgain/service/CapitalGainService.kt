package br.com.nubank.capitalgain.service

import br.com.nubank.capitalgain.model.OperationRequest
import br.com.nubank.capitalgain.model.OperationResponse
import br.com.nubank.capitalgain.model.OperationType
import br.com.nubank.capitalgain.model.operationTotalCost
import java.math.BigDecimal
import java.math.RoundingMode

object CapitalGainService {

    private val MAX_TAX_LIMIT_VALUE = BigDecimal(20000)
    private val TAX_PERCENT = BigDecimal("0.20")
    private var weightedAveragePrice = BigDecimal.ZERO
    private var accumulatedLoss = BigDecimal.ZERO
    private var totalShares = 0

    fun calculateTax(operations: List<OperationRequest>): List<OperationResponse> {
        resetState()
        return operations.map { operation ->
            when (operation.operation) {
                OperationType.BUY -> {
                    updateWeightedAverage(operation)
                    OperationResponse(BigDecimal.ZERO.toScale())
                }
                OperationType.SELL -> {
                    OperationResponse(calculateSellTax(operation).toScale())
                }
            }
        }
    }

    private fun calculateSellTax(operation: OperationRequest): BigDecimal {
        val profit = calculateProfit(operation)

        return if (profit > BigDecimal.ZERO) {
            val taxableProfit = deductAccumulatedLoss(profit)
            totalShares -= operation.quantity
            if (taxableProfit > BigDecimal.ZERO && operation.operationTotalCost() > MAX_TAX_LIMIT_VALUE) {
                taxableProfit.multiply(TAX_PERCENT)
            } else {
                BigDecimal.ZERO
            }
        } else {
            accumulateLoss(profit)
            totalShares -= operation.quantity
            BigDecimal.ZERO
        }
    }

    private fun calculateProfit(operation: OperationRequest): BigDecimal {
        return operation.operationTotalCost().subtract(weightedAveragePrice.multiply(operation.quantity.toBigDecimal()))
    }

    private fun deductAccumulatedLoss(profit: BigDecimal): BigDecimal {
        return if (accumulatedLoss >= profit) {
            accumulatedLoss -= profit
            BigDecimal.ZERO
        } else {
            val remainingProfit = profit - accumulatedLoss
            accumulatedLoss = BigDecimal.ZERO
            remainingProfit
        }
    }

    private fun accumulateLoss(profit: BigDecimal) {
        accumulatedLoss += profit.abs()
    }

    private fun updateWeightedAverage(operation: OperationRequest) {
        val totalCost = weightedAveragePrice.multiply(totalShares.toBigDecimal())
            .add(operation.operationTotalCost())
        totalShares += operation.quantity
        weightedAveragePrice = if (totalShares > 0) {
            totalCost.divide(totalShares.toBigDecimal(), 2, RoundingMode.HALF_UP)
        } else {
            BigDecimal.ZERO
        }
    }

    private fun resetState() {
        weightedAveragePrice = BigDecimal.ZERO
        accumulatedLoss = BigDecimal.ZERO
        totalShares = 0
    }
}

fun BigDecimal.toScale(): BigDecimal = this.setScale(2, RoundingMode.HALF_UP)
