package com.example.model

import androidx.compose.runtime.Immutable
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime

/**
 * Modèle immuable représentant un mois spécifique pour la navigation budgétaire.
 *
 * @property year Année (ex: 2026).
 * @property month Index du mois de 1 (Janvier) à 12 (Décembre).
 */
@Immutable
data class YearMonth(
    val year: Int,
    val month: Int
) {
    /**
     * Bornes temporelles (timestamps millisecondes) précalculées pour ce mois.
     */
    val startTimestamp: Long by lazy {
        LocalDate(year, month, 1)
            .atStartOfDayIn(TimeZone.currentSystemDefault())
            .toEpochMilliseconds()
    }

    val endTimestamp: Long by lazy {
        // Calcule le 1er jour du mois suivant à minuit, et recule d'une milliseconde
        LocalDate(year, month, 1)
            .plus(1, DateTimeUnit.MONTH)
            .atStartOfDayIn(TimeZone.currentSystemDefault())
            .toEpochMilliseconds() - 1
    }

    /**
     * Libellé formaté en français (ex: "Août 2026").
     */
    val displayLabel: String by lazy {
        val monthNames = listOf(
            "Janvier", "Février", "Mars", "Avril", "Mai", "Juin",
            "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"
        )
        "${monthNames[month - 1]} $year"
    }

    /**
     * Retourne le YearMonth précédent.
     */
    fun previous(): YearMonth {
        return if (month == 1) {
            YearMonth(year - 1, 12)
        } else {
            YearMonth(year, month - 1)
        }
    }

    /**
     * Retourne le YearMonth suivant.
     */
    fun next(): YearMonth {
        return if (month == 12) {
            YearMonth(year + 1, 1)
        } else {
            YearMonth(year, month + 1)
        }
    }

    /**
     * Vérifie si un timestamp millisecondes appartient à ce mois précis.
     * Effectue une comparaison O(1) ultra-rapide sans réinstancier d'objets temporels.
     */
    fun containsTimestamp(timestamp: Long): Boolean {
        return timestamp in startTimestamp..endTimestamp
    }

    companion object {
        /**
         * Crée le YearMonth courant.
         */
        fun current(): YearMonth {
            val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            return YearMonth(
                year = now.year,
                month = now.monthNumber
            )
        }

        /**
         * Crée le YearMonth correspondant à un timestamp.
         */
        fun fromTimestamp(timestamp: Long): YearMonth {
            val instant = Instant.fromEpochMilliseconds(timestamp)
            val dateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
            return YearMonth(
                year = dateTime.year,
                month = dateTime.monthNumber
            )
        }
    }
}