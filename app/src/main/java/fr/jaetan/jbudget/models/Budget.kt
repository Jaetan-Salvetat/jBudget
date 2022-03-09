package fr.jaetan.jbudget.models

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToMany

@Entity
data class Budget(
    @Id var id: Long = 0,
    var title: String = "",
    var total: Double = 0.0,
    var totalSpent: Double = 0.0
){
    lateinit var items: ToMany<BudgetItem>
    lateinit var history: ToMany<BudgetHistory>
}