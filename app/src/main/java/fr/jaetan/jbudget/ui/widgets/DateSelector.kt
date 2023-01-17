package fr.jaetan.jbudget.ui.widgets

import android.app.DatePickerDialog
import android.content.Context
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.*

fun dateSelector(context: Context, date: Date, onChange: (Date) -> Unit) {
    val calendar = Calendar.getInstance()
    calendar.time = date

    DatePickerDialog(
        context,
        { _, year, month, day ->
            onChange(
                Date.from(
                    LocalDate.of(year, month + 1, day)
                        .atStartOfDay()
                        .toInstant(ZoneOffset.UTC)
                )
            )
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH),
    ).show()
}