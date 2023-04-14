package fr.jaetan.jbudget.app.transaction

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import fr.jaetan.jbudget.core.models.*
import fr.jaetan.jbudget.core.services.JBudget
import java.text.DecimalFormat
import java.util.*

class TransactionViewModel(
    budget: Budget? = null,
    val navController: NavHostController,
    private val transactionId: String? = null
): ViewModel() {
    var showCategoryDialog by mutableStateOf(false)
    var showBudgetDropDown by mutableStateOf(false)
    var showCategoryDropDown by mutableStateOf(false)
    var showBudgetDialog by mutableStateOf(false)
    var currentBudget by mutableStateOf(budget)
    val budgets: List<Budget> get() = JBudget.state.budgets.filter { it.isCurrentBudget }
    var currentCategory by mutableStateOf(null as Category?)
    var amountString by mutableStateOf("")
    var loadingState by mutableStateOf(State.None)
    var isInUpdateMode by mutableStateOf(false)

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


    fun changeCurrentBudget(budget: Budget?, categoryId: String? = null) {
        showBudgetDropDown = false
        currentBudget = budget
        currentCategory = JBudget.state.categories.find { it.id == categoryId }
    }

    fun save() {
        loadingState = State.Loading

        val transaction = Transaction(
            id = transactionId ?: "",
            date = Calendar.getInstance().time,
            amount = amountString.replace(",", ".").toDouble(),
            categoryId = currentCategory?.id,
            budgetId = currentBudget!!.id
        )


        if (isInUpdateMode) {
            JBudget.transactionRepository.updateTransaction(transaction) { response ->
                if (response == FirebaseResponse.Success) navController.popBackStack()
                loadingState = State.Error
            }
            return
        }

        JBudget.transactionRepository.createTransaction(transaction) { _, response ->
            if (response == FirebaseResponse.Success) navController.popBackStack()
            loadingState = State.Error
        }
    }

    init {
        Log.d("testt", transactionId.toString())
        Log.d("testt", budget?.name.toString())
        if (transactionId != null && budget == null) {
            isInUpdateMode = true
            JBudget.transactionRepository.findById(transactionId) { transaction, _ ->
                changeCurrentBudget(budgets.find { it.id == transaction?.budgetId }, transaction?.categoryId)
                amountString = transaction?.amount?.toString().orEmpty()
            }
        } else if (budget == null) {
            changeCurrentBudget(JBudget.state.budgets.firstOrNull())
        }
    }

    /*private fun Double.toPriceFormat(): String {
        val nf = NumberFormat.getCurrencyInstance()
        nf.maximumFractionDigits = 2
        nf.currency = Currency.getInstance("EUR") // Euro for now. Will change when StockPro scales internationally.
        return nf.format(this)
    }*/
}