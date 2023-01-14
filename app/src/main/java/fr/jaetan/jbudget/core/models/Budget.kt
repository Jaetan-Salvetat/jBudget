package fr.jaetan.jbudget.core.models

import com.google.firebase.auth.FirebaseAuth
import java.sql.Timestamp
import java.util.*

data class Budget(
    var id: String = "",
    var userId: String = FirebaseAuth.getInstance().currentUser!!.uid,
    var name: String = "",
    var startDate: Date = Date(),
    var endDate: Date? = null) {


    fun toMap(): Map<String, Any?> {
        return mapOf(
            "name" to name,
            "userId" to userId,
            "startDate" to Timestamp(startDate.time / 1000),
            "endDate" to if (endDate == null) null else Timestamp(endDate!!.time / 1000),
        )
    }

    companion object {
        fun fromMap(budgetId: String, data: Map<String, Any>): Budget {
            val startDate = Timestamp(data["startDate"] as Long)
            val endDate = if (data["endDate"] != null) Timestamp(data["endDate"] as Long) else null
            return Budget(
                id = budgetId,
                userId = data["userId"] as String,
                name = data["name"] as String,
                startDate = Date(startDate.time * 1000),
                endDate = if (endDate != null) Date(endDate.time * 1000) else null,
            )
        }
    }
}
