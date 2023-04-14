package fr.jaetan.jbudget.core.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import fr.jaetan.jbudget.core.services.JBudget
import fr.jaetan.jbudget.core.services.extentions.roundTo2Decimal
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.Calendar
import java.util.Date

data class Budget(
    var name: String,
    val id: String = "",
    var userId: String = FirebaseAuth.getInstance().currentUser!!.uid,
    var startDate: Date = Calendar.getInstance().time,
    var endDate: Date? = null,
    var payship: Double = .0
) {
    val transactions = mutableStateListOf<Transaction>()
    var isLoadingTransactions by mutableStateOf(false)
    var transactionTotalAmount = 0.0

    val isCurrentBudget: Boolean
        get() = when {
            endDate == null -> true
            endDate!!.after(Date.from(
                LocalDate.now().atStartOfDay().toInstant(
                    ZoneOffset.UTC))) -> true
            else -> false
        }

    fun toMap(): Map<String, Any?> {
        return mapOf(
            "name" to name,
            "userId" to userId,
            "startDate" to  Timestamp(startDate),
            "endDate" to if (endDate == null) null else Timestamp(endDate!!),
            "payship" to payship
        )
    }

    fun getPercentages() :List<CategoryPercentage> {
        if (transactions.isEmpty()) return listOf()

        val percentages = mutableListOf<CategoryPercentage>()
        val categoriesGrouped = transactions.groupBy { it.categoryId }
        transactionTotalAmount = transactions.map{ it.amount }.reduce { acc, i -> acc + i}

        categoriesGrouped.mapValues { (categoryId, group) ->
            val categoryTotal = group.map{ it.amount }.reduce { acc, i -> acc + i}

            percentages.add(CategoryPercentage(
                category = JBudget.state.categories.find { it.id == categoryId },
                values = categoryTotal,
                percentage = (categoryTotal * 100) / transactionTotalAmount,
                color = JBudget.state.categories.find { it.id == categoryId }?.color
            ))
        }
        return percentages.sortedBy { it.category?.name }
    }

    companion object {
        fun fromMapList(data: List<DocumentSnapshot>): List<Budget> = data.map { fromMap(it) }

        private fun fromMap(budget: DocumentSnapshot): Budget = Budget(
            id = budget.id,
            name = budget.data?.get("name") as String,
            userId = budget.data?.get("userId") as String,
            startDate = budget.data?.get("startDate").let { (it as Timestamp).toDate() },
            endDate = budget.data?.get("endDate")?.let { (it as Timestamp).toDate() },
            payship = budget.data?.get("payship") as Double? ?: .0
        )
    }
}


data class CategoryPercentage(
    var category: Category?,
    var values: Double,
    var percentage: Double,
    var color: Color?
)

fun CategoryPercentage.toText(): String = "${category?.name ?: "Non catégorisé"} -> $values (${percentage.roundTo2Decimal()}%)"