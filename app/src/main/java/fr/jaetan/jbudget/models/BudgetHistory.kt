package fr.jaetan.jbudget.models

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Entity
data class BudgetHistory (
    @Id var id: Long = 0,
    var value: Double = 0.0,
    var name: String = "",
    var done: Boolean = false,
    var cashFlow: Boolean = false,
    var date: String? = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),
    var time: String? = LocalTime.now().format(DateTimeFormatter.ofPattern("H:m:ss"))
)
