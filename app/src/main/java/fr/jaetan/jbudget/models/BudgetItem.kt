package fr.jaetan.jbudget.models

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToOne

@Entity
data class BudgetItem(
    @Id var id: Long = 0,
    var name: String = "",
    var value: Double = 0.0,
    var cashFlow: Boolean = false
){
    lateinit var title: ToOne<BudgetTitle>;
}