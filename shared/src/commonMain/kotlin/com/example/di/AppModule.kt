package com.example.di

import com.example.data.repository.FakeTransactionRepository
import com.example.data.repository.NetworkTransactionRepository
import com.example.data.repository.TransactionRepository
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object AppModule {
    private const val STUDENT_ID = "test02"

    private const val USE_FAKE_DATA = true

    private val httpClient by lazy {
        HttpClient {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }
    }

    val transactionRepository: TransactionRepository by lazy {
        if (USE_FAKE_DATA) FakeTransactionRepository()
        else NetworkTransactionRepository(httpClient, STUDENT_ID)
    }
}