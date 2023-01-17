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

    fun createTransaction(transaction: Transaction, callback: (String?, FirebaseResponse) -> Unit) {
        database
            .add(transaction.toMap())
            .addOnSuccessListener { documentReference ->
                callback(documentReference.id, FirebaseResponse.Success) }
            .addOnCanceledListener {
                callback(null, FirebaseResponse.ConnectivityError)
            }
            .addOnFailureListener {
                callback(null, FirebaseResponse.Error)
            }
    }
}