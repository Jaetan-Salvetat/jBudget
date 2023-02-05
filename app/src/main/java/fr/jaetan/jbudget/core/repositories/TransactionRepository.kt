package fr.jaetan.jbudget.core.repositories

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.ktx.Firebase
import fr.jaetan.jbudget.core.models.FirebaseResponse
import fr.jaetan.jbudget.core.models.Transaction
import fr.jaetan.jbudget.core.services.JBudget

class TransactionRepository {
    private val database = Firebase.firestore.collection("transactions")

    suspend fun getAll(budgetId: String?) {
        database
            .whereEqualTo("budgetId", budgetId)
            .snapshots()
            .collect { query ->
                JBudget.state.budgets.find { it.id == budgetId }?.transactions?.clear()
                JBudget.state.budgets.find { it.id == budgetId }?.transactions?.addAll(Transaction.fromMapList(query.documents))
            }
    }

    fun findById(id: String, callback: (Transaction?, FirebaseResponse) -> Unit) {
        database.document(id).get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    callback(Transaction.fromMap(it.result), FirebaseResponse.Success)
                    return@addOnCompleteListener
                }
                callback(null, FirebaseResponse.Error)
            }
    }

    fun createTransaction(transaction: Transaction, callback: (Transaction?, FirebaseResponse) -> Unit) {
        database.add(transaction.toMap())
            .addOnSuccessListener { documentReference ->
                callback(transaction.copy(id = documentReference.id), FirebaseResponse.Success) }
            .addOnCanceledListener {
                callback(null, FirebaseResponse.ConnectivityError)
            }
            .addOnFailureListener {
                callback(null, FirebaseResponse.Error)
            }
    }

    fun updateTransaction(transaction: Transaction, callback: (FirebaseResponse) -> Unit) {
        database.document(transaction.id).set(transaction.toMap())
            .addOnSuccessListener {
                callback(FirebaseResponse.Success)
            }
            .addOnCanceledListener {
                callback(FirebaseResponse.Error)
            }
            .addOnFailureListener {
                callback(FirebaseResponse.Error)
            }
    }

    fun removeTransaction(transactionId: String) {
        database.document(transactionId).delete()
    }

    fun deleteAllBy(budgetId: String, callback: (FirebaseResponse) -> Unit) {
        database.whereEqualTo("budgetId", budgetId).get().addOnCompleteListener {
            if (it.result.documents.isEmpty()) {
                callback(FirebaseResponse.Success)
                return@addOnCompleteListener
            }

            it.result.documents.forEach { doc ->
                doc.reference.delete().addOnCompleteListener { task ->
                    if (!task.isSuccessful) callback(FirebaseResponse.Error)
                }
                callback(FirebaseResponse.Success)
            }
        }
    }
}