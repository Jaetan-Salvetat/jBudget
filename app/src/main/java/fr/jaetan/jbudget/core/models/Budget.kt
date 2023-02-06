package fr.jaetan.jbudget.core.models

import androidx.compose.runtime.mutableStateListOf
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.*

data class Budget(
    var name: String,
    val id: String = "",
    var userId: String = FirebaseAuth.getInstance().currentUser!!.uid,
    var startDate: Date = Calendar.getInstance().time,
    var endDate: Date? = null
) {
    val transactions = mutableStateListOf<Transaction>()

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
            "endDate" to if (endDate == null) null else Timestamp(endDate!!)
        )
    }

    companion object {
        fun fromMapList(data: List<DocumentSnapshot>): List<Budget> = data.map { fromMap(it) }

        fun fromMap(budget: DocumentSnapshot): Budget = Budget(
            id = budget.id,
            name = budget.data?.get("name") as String,
            userId = budget.data?.get("userId") as String,
            startDate = budget.data?.get("startDate").let { (it as Timestamp).toDate() },
            endDate = budget.data?.get("endDate")?.let { (it as Timestamp).toDate() }
        )
    }
}
