package br.com.nubank.capitalgain.service

import br.com.nubank.capitalgain.gson.BigDecimalSerializer
import br.com.nubank.capitalgain.model.OperationRequest
import br.com.nubank.capitalgain.model.OperationResponse
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.math.BigDecimal

object GsonService {
    private val gson: Gson = GsonBuilder()
        .registerTypeAdapter(BigDecimal::class.java, BigDecimalSerializer())
        .create()
    private val type = object : TypeToken<List<OperationRequest>>() {}.type
    private val typeResponse = object : TypeToken<List<OperationResponse>>() {}.type
    private val regex = "\\[[^]]*\\]".toRegex()

    fun parseJson(line: String): List<List<OperationRequest>> =
        splitArrays(line).map { gson.fromJson(it, type) as List<OperationRequest> }

    fun splitArrays(line: String) = regex.findAll(line).map { "${it.value}" }.toList()

    fun parseJsonResponse(line: String): List<OperationResponse> =
        gson.fromJson(line, typeResponse) as List<OperationResponse>

    fun printJson(values: Any) {
        println(gson.toJson(values))
    }
}