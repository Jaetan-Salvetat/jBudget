package fr.jaetan.jbudget.core.repositories

import android.util.Log
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import fr.jaetan.jbudget.core.models.FirebaseResponse
import fr.jaetan.jbudget.core.services.JBudget

class UserRepository {
    private val auth = FirebaseAuth.getInstance()

    fun updateEmail(email: String, password: String, callback: (FirebaseResponse) -> Unit) {
        reAuthenticate(password) { res ->
            if (res != FirebaseResponse.Success) {
                callback(res)
                return@reAuthenticate
            }

            auth.currentUser?.updateEmail(email)
                ?.addOnSuccessListener {
                    JBudget.state.currentUser = auth.currentUser
                    callback(FirebaseResponse.Success)
                }
                ?.addOnCanceledListener {
                    Log.e("testt", "updateEmail canceled")
                    callback(FirebaseResponse.Error)
                }
                ?.addOnFailureListener {
                    Log.e("testt", it.message.toString())
                    callback(FirebaseResponse.Error)
                }
        }
    }

    fun removeAccount(password: String, callback: (FirebaseResponse) -> Unit) {
        reAuthenticate(password) { res ->
            callback(res)
            if (res != FirebaseResponse.Success) {
                return@reAuthenticate
            }

            JBudget.state.currentUser?.delete()
                ?.addOnCompleteListener {
                    if (it.isSuccessful) {
                        Log.d("testt", "removed")
                    } else {
                        Log.d("testt:isCanceled", it.isCanceled.toString())
                        Log.d("testt:exception", it.exception?.message.toString())
                    }
                }
            JBudget.categoryRepository.removeAll()
            JBudget.budgetRepository.removeAll()
            JBudget.state.budgets.forEach {
                JBudget.transactionRepository.removeAll(it.transactions)
            }
        }
    }

    private fun reAuthenticate(password: String, callback: (FirebaseResponse) -> Unit) {
        val credential = EmailAuthProvider.getCredential(JBudget.state.currentUser!!.email!!, password)
        auth.currentUser?.reauthenticate(credential)
            ?.addOnSuccessListener {
                callback(FirebaseResponse.Success)
            }
            ?.addOnCanceledListener {
                Log.e("testt", "reauthenticate canceled")
                callback(FirebaseResponse.Error)
            }
            ?.addOnFailureListener {
                Log.e("testt", it.message.toString())
                callback(FirebaseResponse.BadEmailOrPassword)
            }
    }
}