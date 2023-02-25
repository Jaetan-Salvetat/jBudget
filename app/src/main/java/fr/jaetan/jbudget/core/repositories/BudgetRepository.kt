package fr.jaetan.jbudget.core.repositories

import android.content.Context
import android.content.Intent
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.ktx.Firebase
import fr.jaetan.jbudget.core.models.Budget
import fr.jaetan.jbudget.core.models.FirebaseResponse
import fr.jaetan.jbudget.core.models.State
import fr.jaetan.jbudget.core.models.toText
import fr.jaetan.jbudget.core.services.JBudget

class BudgetRepository {
    private val database = Firebase.firestore.collection("budgets")

    suspend fun initListener() {
        database
            .whereEqualTo("userId", JBudget.state.currentUser?.uid)
            .snapshots()
            .collect { query ->
                val documents = query.documents
                JBudget.state.budgetsLoadingState = if (documents.isEmpty()) {
                    State.EmptyData
                } else {
                    State.None
                }

                JBudget.state.budgets.clear()
                JBudget.state.budgets.addAll(Budget.fromMapList(documents))
            }
    }

    fun createBudget(budget: Budget, callback: (String?, FirebaseResponse) -> Unit)  {
        database
            .add(budget.toMap())
            .addOnSuccessListener { documentReference ->
                callback(documentReference.id, FirebaseResponse.Success)
            }
            .addOnCanceledListener {
                callback(null, FirebaseResponse.ConnectivityError)
            }
            .addOnFailureListener {
                callback(null, FirebaseResponse.Error)
            }
    }

    fun delete(budgetId: String, callback: (FirebaseResponse) -> Unit) {
        database.document(budgetId).delete().addOnCompleteListener { task ->
            if (task.isSuccessful)
                JBudget.transactionRepository.deleteAllBy(budgetId) { transactionDeleted ->
                    if (transactionDeleted == FirebaseResponse.Success) {
                        return@deleteAllBy callback(FirebaseResponse.Success)
                    }
                    callback(FirebaseResponse.Error)
                }
        }
    }

    fun removeAll() {
        JBudget.state.budgets.forEach {
            database.document(it.id).delete()
        }
    }

    fun shareAsText(context: Context, budget: Budget) {
        var text = "total -> ${budget.transactionTotalAmount}\n"

        budget.getPercentages().forEachIndexed { index, percentage ->
            text += "\n${percentage.toText()}"
        }

        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
        }
        context.startActivity(intent)
    }
}
