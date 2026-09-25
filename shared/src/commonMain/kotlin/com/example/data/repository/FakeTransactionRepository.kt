package com.example.data.repository

import com.example.model.Category
import com.example.model.Transaction
import com.example.model.YearMonth
import com.example.utils.generateUUID
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant


class FakeTransactionRepository : TransactionRepository {

    private val transactions = mutableListOf<Transaction>()

    init {
        val now = YearMonth.current()
        val prev = now.previous()
        val next = now.next()

        // Remplace l'ancien Calendar.set(...) : construit un timestamp avec kotlinx-datetime
        fun time(ym: YearMonth, day: Int, hour: Int): Long =
            LocalDateTime(ym.year, ym.month, day, hour, 0)   // month est déjà en 1..12
                .toInstant(TimeZone.currentSystemDefault())
                .toEpochMilliseconds()

        transactions += listOf(
            // Mois actuel
            Transaction(generateUUID(), "Supermarché Bio", 45000.0, time(now, 22, 14), Category.ALIMENTATION),
            Transaction(generateUUID(), "Session Tennis", 12000.0, time(now, 20, 10), Category.LOISIRS),
            Transaction(generateUUID(), "Ticket de Bus Express", 2500.0, time(now, 18, 8), Category.TRANSPORT),
            Transaction(generateUUID(), "Loyer Mensuel", 250000.0, time(now, 5, 9), Category.LOGEMENT),
            Transaction(generateUUID(), "Boulangerie & Pâtisserie", 4800.0, time(now, 15, 16), Category.ALIMENTATION),
            Transaction(generateUUID(), "Recharge Vélo Électrique", 3500.0, time(now, 12, 11), Category.TRANSPORT),
            Transaction(generateUUID(), "Facture Électricité", 48000.0, time(now, 8, 15), Category.LOGEMENT),
            // Mois précédent
            Transaction(generateUUID(), "Loyer Mois Précédent", 250000.0, time(prev, 5, 9), Category.LOGEMENT),
            Transaction(generateUUID(), "Courses du mois", 65000.0, time(prev, 10, 15), Category.ALIMENTATION),
            Transaction(generateUUID(), "Abonnement Transport", 35000.0, time(prev, 2, 8), Category.TRANSPORT),
            Transaction(generateUUID(), "Sortie Restaurant", 22000.0, time(prev, 20, 20), Category.LOISIRS),
            // Mois suivant
            Transaction(generateUUID(), "Avance Loyer Prévue", 250000.0, time(next, 1, 9), Category.LOGEMENT),
            Transaction(generateUUID(), "Abonnement Salle de Sport", 20000.0, time(next, 3, 10), Category.LOISIRS)
        )
    }

    override suspend fun getTransactions(): List<Transaction> = transactions.toList()

    override suspend fun addTransaction(transaction: Transaction): Transaction {
        transactions.add(0, transaction)
        return transaction
    }

    override suspend fun updateTransaction(transaction: Transaction) {
        val index = transactions.indexOfFirst { it.id == transaction.id }
        if (index >= 0) transactions[index] = transaction
    }

    override suspend fun deleteTransaction(id: String) {
        transactions.removeAll { it.id == id }
    }
}