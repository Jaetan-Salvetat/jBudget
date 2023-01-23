package fr.jaetan.jbudget.core.repositories

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import fr.jaetan.jbudget.core.models.FirebaseResponse
import fr.jaetan.jbudget.core.models.Transaction
import fr.jaetan.jbudget.core.services.JBudget

class TransactionRepository {
    private val database = Firebase.firestore.collection("transactions")

    fun getAll(budgetId: String?, callback: (List<Transaction>, FirebaseResponse) -> Unit) {
        database
            .whereEqualTo("budgetId", budgetId).get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    callback(Transaction.fromMapList(it.result.documents), FirebaseResponse.Success)
                    return@addOnCompleteListener
                }
                callback(listOf(), FirebaseResponse.Error)
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
                JBudget.state.budgets.forEach { budget ->
                    if (budget.transactions.find { it == transaction } != null) {
                        val transactions = JBudget.state.budgets.find { it == budget }?.transactions?.toList()

                        transactions?.find { it.id == transaction.id }?.amount = transaction.amount
                        transactions?.find { it.id == transaction.id }?.categoryId = transaction.categoryId
                        JBudget.state.budgets.find { it == budget }?.transactions?.clear()
                        JBudget.state.budgets.find { it == budget }?.transactions?.addAll(transactions as Collection<Transaction>)
                    }
                }
            }
            .addOnCanceledListener {
                callback(FirebaseResponse.ConnectivityError)
            }
            .addOnFailureListener {
                callback(FirebaseResponse.Error)
            }
    }

    fun removeTransaction(transactionId: String, callback: (FirebaseResponse) -> Unit) {
        database.document(transactionId).delete()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    callback(FirebaseResponse.Success)
                    return@addOnCompleteListener
                }
                callback(FirebaseResponse.Error)
            }
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