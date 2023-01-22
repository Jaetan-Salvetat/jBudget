package fr.jaetan.jbudget.core.models

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.google.firebase.firestore.DocumentSnapshot

data class Category(
    var id: String = "",
    val name: String,
    val budgetId: String,
    val color: Color?
) {
    fun toMap(): Map<String, Any?> = mapOf(
        "name" to name,
        "budgetId" to budgetId,
        "color" to color?.toArgb()?.toLong()
    )

    companion object {
        fun fromMapList(data: List<DocumentSnapshot>): List<Category> = data.map { fromMap(it) }

        fun fromMap(transaction: DocumentSnapshot): Category = Category(
            id = transaction.id,
            name = transaction.data?.get("name") as String,
            budgetId = transaction.data?.get("budgetId") as String,
            color = Color((transaction.data?.get("color") as Long))
        )
    }
}
