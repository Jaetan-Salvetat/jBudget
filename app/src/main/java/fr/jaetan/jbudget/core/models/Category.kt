package fr.jaetan.jbudget.core.models

data class Category(
    var id: String = "",
    val name: String,
    val budgetId: String
) {
    fun toMap(): Map<String, String> = mapOf(
        "name" to name,
        "budgetId" to budgetId
    )
}
