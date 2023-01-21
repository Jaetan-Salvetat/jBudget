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
}