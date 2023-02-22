package fr.jaetan.jbudget.core.services.extentions

import java.text.DecimalFormat

fun Double.roundTo2Decimal(): String = DecimalFormat("#.##").format(this)