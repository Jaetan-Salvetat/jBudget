package fr.jaetan.jbudget.core.models

import com.google.firebase.auth.FirebaseAuth
import java.sql.Timestamp
import java.util.*

data class Budget(
    var id: String = "",
    var userId: String = FirebaseAuth.getInstance().currentUser!!.uid,
    var name: String = "",
    var startDate: Date? = null,
    var endDate: Date? = null) {


    fun toMap(): Map<String, Any?> {
        return mapOf(
            "name" to name,
            "userId" to userId,
            "startDate" to if (startDate == null) null else Timestamp(startDate!!.time / 1000),
            "endDate" to if (endDate == null) null else Timestamp(endDate!!.time / 1000),
        )
    }

    companion object {
        fun fromMap(budgetId: String, data: Map<String, Any>): Budget {
            val startDate = if (data["startDate"] != null) Timestamp(data["startDate"] as Long) else null
            val endDate = if (data["endDate"] != null) Timestamp(data["endDate"] as Long) else null
            return Budget(
                id = budgetId,
                userId = data["userId"] as String,
                name = data["name"] as String,
                startDate = if (startDate != null) Date(startDate.time * 1000) else null,
                endDate = if (endDate != null) Date(endDate.time * 1000) else null,
            )
        }
    }
}
