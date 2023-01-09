package fr.jaetan.jbudget.core.repositories

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import fr.jaetan.jbudget.core.models.Budget
import fr.jaetan.jbudget.core.models.FirebaseResponse

class BudgetRepository {
    private val database = Firebase.firestore

    fun createBudget(budgetName: String, callback: (String?, FirebaseResponse) -> Unit)  {

        val budget = Budget()
        budget.name = budgetName

        database.collection(COLLECTION_NAME)
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

    fun getBudget(budgetId: String?, callback: (Budget?, FirebaseResponse) -> Unit) {
        val document = database.collection(COLLECTION_NAME).document(budgetId ?: "")
        document.get()
            .addOnSuccessListener { response ->
                callback(Budget.fromMap(budgetId!!, response.data!!), FirebaseResponse.Success)
            }
            .addOnCanceledListener { callback(null, FirebaseResponse.Error) }
            .addOnFailureListener { callback(null, FirebaseResponse.Error) }
    }

    companion object {
        const val COLLECTION_NAME = "budgets"
    }
}