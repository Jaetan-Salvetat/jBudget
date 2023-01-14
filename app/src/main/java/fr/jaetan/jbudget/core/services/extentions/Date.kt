package fr.jaetan.jbudget.core.services.extentions

import android.icu.text.SimpleDateFormat
import java.util.*

fun Date.toText(): String {
    val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    return dateFormatter.format(this)
}