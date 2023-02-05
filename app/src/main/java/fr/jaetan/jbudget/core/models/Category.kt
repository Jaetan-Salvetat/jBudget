package fr.jaetan.jbudget.core.models

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.google.firebase.firestore.DocumentSnapshot
import fr.jaetan.jbudget.core.services.JBudget
import kotlin.random.Random

data class Category(
    var id: String = "",
    val name: String,
    val color: Color? = Color(Random.nextInt(256), Random.nextInt(256), Random.nextInt(256), 125),
    val userId: String = JBudget.state.currentUser?.uid ?: ""
) {
    fun toMap(): Map<String, Any?> = mapOf(
        "name" to name,
        "userId" to userId,
        "color" to color?.toArgb()?.toLong()
    )

    companion object {
        fun fromMapList(data: List<DocumentSnapshot>): List<Category> = data.map { fromMap(it) }

        private fun fromMap(transaction: DocumentSnapshot): Category = Category(
            id = transaction.id,
            name = transaction.data?.get("name") as String,
            userId = transaction.data?.get("userId") as String,
            color = Color((transaction.data?.get("color") as Long))
        )
    }
}
