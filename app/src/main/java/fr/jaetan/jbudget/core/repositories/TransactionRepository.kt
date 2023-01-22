package fr.jaetan.jbudget.core.repositories

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import fr.jaetan.jbudget.core.models.FirebaseResponse
import fr.jaetan.jbudget.core.models.Transaction

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
}