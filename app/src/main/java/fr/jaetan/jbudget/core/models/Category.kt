package fr.jaetan.jbudget.core.models

data class Category(
    var id: String = "",
    val name: String,
    val budgetId: String
)
