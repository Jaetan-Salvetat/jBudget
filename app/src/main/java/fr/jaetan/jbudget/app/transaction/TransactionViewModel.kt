package fr.jaetan.jbudget.app.transaction

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import fr.jaetan.jbudget.core.models.Budget
import fr.jaetan.jbudget.core.models.Category
import java.text.DecimalFormat

class TransactionViewModel: ViewModel() {
    var showBudgetDropDown by mutableStateOf(false)
    var showCategoryDropDown by mutableStateOf(false)
    var currentBudget by mutableStateOf(null as Budget?)
    var currentCategory by mutableStateOf(null as Category?)
    val categories = mutableStateListOf(
        Category(name = "Category 1", budgetId = "flI6koWXohaxwNLjeftg"),
        Category(name = "Category 2", budgetId = "flI6koWXohaxwNLjeftg"),
        Category(name = "Category 3", budgetId = "flI6koWXohaxwNLjeftg")
    )
    val budgets = mutableStateListOf(
        Budget(name = "name 1"),
        Budget(name = "name 2"),
        Budget(name = "name 3"),
    )
    var amountString by mutableStateOf("")

    fun updateAmount(value: String) {
        val decimalFormatter = DecimalFormat()
        val mutableString = value.replace(".", ",")

        if (mutableString.count { it == ",".first() } > 1) return

        if (mutableString.isEmpty() || mutableString.last().toString() == ",") {
            amountString = mutableString
            return
        }

        decimalFormatter.parse(mutableString)?.let {
            if (it.toDouble() > Double.MAX_VALUE || it.toDouble() < Double.MIN_VALUE) {
                return
            }
            amountString = it.toString().replace(".", ",")
        }
    }

    init {
        currentBudget = budgets[0]
    }


    fun save() {

    }

    /*private fun Double.toPriceFormat(): String {
        val nf = NumberFormat.getCurrencyInstance()
        nf.maximumFractionDigits = 2
        nf.currency = Currency.getInstance("EUR") // Euro for now. Will change when StockPro scales internationally.
        return nf.format(this)
    }*/
}