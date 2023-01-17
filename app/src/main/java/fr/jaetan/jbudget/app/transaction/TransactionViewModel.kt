package fr.jaetan.jbudget.app.transaction

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import fr.jaetan.jbudget.core.models.Budget
import fr.jaetan.jbudget.core.models.Category
import fr.jaetan.jbudget.core.models.FirebaseResponse
import fr.jaetan.jbudget.core.services.JBudget
import java.text.DecimalFormat

class TransactionViewModel: ViewModel() {
    var showCategoryInput by mutableStateOf(false)
    var showBudgetDropDown by mutableStateOf(false)
    var showCategoryDropDown by mutableStateOf(false)
    var showBudgetDialog by mutableStateOf(false)
    var currentBudget by mutableStateOf(JBudget.state.budgets.firstOrNull())
    var currentCategory by mutableStateOf(null as Category?)
    val categories = mutableStateListOf<Category>()
    var amountString by mutableStateOf("")
    var categoryName by mutableStateOf("")
    val budgets: List<Budget> get() = JBudget.state.budgets

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


    fun saveCategory() {
        showCategoryInput = false
        val category = Category(name = categoryName, budgetId = currentBudget!!.id)

        JBudget.categoryRepository.createCategory(category) { categoryId, response ->
            if (response == FirebaseResponse.Success) {
                category.id = categoryId!!
                categories.add(category)
            }
        }
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