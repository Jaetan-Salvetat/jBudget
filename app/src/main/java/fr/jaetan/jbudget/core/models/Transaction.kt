package fr.jaetan.jbudget.core.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import java.util.*

data class Transaction(
    var id: String,
    var date: Date,
    var amount: Double,
    var categoryId: String? = null,
) {

    fun toMap(): Map<String, Any?> {
        return mapOf(
            "string" to id,
            "date" to Timestamp(date),
            "amount" to amount,
            "categoryId" to categoryId,
        )
    }
    companion object {
        fun fromMapList(data: List<DocumentSnapshot>): List<Transaction> = data.map { fromMap(it) }

        private fun fromMap(transaction: DocumentSnapshot): Transaction = Transaction(
            id = transaction.id,
            date = transaction.data?.get("date").let { (it as Timestamp).toDate() },
            amount = transaction.data?.get("amount") as Double,
            categoryId = transaction.data?.get("categoryId") as String?,
        )
    }
}