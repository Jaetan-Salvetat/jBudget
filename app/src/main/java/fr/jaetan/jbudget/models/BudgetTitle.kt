package fr.jaetan.jbudget.models

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class BudgetTitle (
    @Id var id: Long = 0,
    var name: String
)