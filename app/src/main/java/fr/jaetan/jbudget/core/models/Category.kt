package fr.jaetan.jbudget.core.models

import com.google.firebase.firestore.DocumentSnapshot

data class Category(
    var id: String = "",
    val name: String,
    val budgetId: String
) {
    fun toMap(): Map<String, String> = mapOf(
        "name" to name,
        "budgetId" to budgetId
    )

    companion object {
        fun fromMapList(data: List<DocumentSnapshot>): List<Category> = data.map { fromMap(it) }

        fun fromMap(transaction: DocumentSnapshot): Category = Category(
            id = transaction.id,
            name = transaction.data?.get("name") as String,
            budgetId = transaction.data?.get("budgetId") as String
        )
    }
}
