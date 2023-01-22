package fr.jaetan.jbudget.core.repositories

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import fr.jaetan.jbudget.core.models.Budget
import fr.jaetan.jbudget.core.models.FirebaseResponse
import fr.jaetan.jbudget.core.services.JBudget

class BudgetRepository {
    private val database = Firebase.firestore.collection("budgets")

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

    fun getAll(callback: (List<Budget>, FirebaseResponse) -> Unit) {
        database
            .whereEqualTo("userId", JBudget.state.currentUser?.uid).get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    callback(Budget.fromMapList(it.result.documents), FirebaseResponse.Success)
                    return@addOnCompleteListener
                }
                callback(listOf(), FirebaseResponse.Error)
            }
    }

    fun delete(budgetId: String, callback: (FirebaseResponse) -> Unit) {
        database.document(budgetId).delete().addOnCompleteListener { task ->
            if (task.isSuccessful)
                JBudget.transactionRepository.deleteAllBy(budgetId) { transactionDeleted ->
                    if (transactionDeleted == FirebaseResponse.Success) {
                        JBudget.categoryRepository.deleteAllBy(budgetId) { categoryDeleted ->
                            if (categoryDeleted == FirebaseResponse.Success) {
                                callback(FirebaseResponse.Success)
                                JBudget.state.budgets.removeIf { it.id == budgetId }
                            } else {
                                callback(FirebaseResponse.Error)
                            }
                        }
                    }
                }
        }
    }
}
