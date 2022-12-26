package fr.jaetan.jbudget.core.repositories

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import fr.jaetan.jbudget.core.models.FirebaseResponse
import fr.jaetan.jbudget.core.services.JBudget

class AuthRepository {
    private val auth = FirebaseAuth.getInstance()

    fun loginWithEmailAndPassword(email: String, password: String, callback: (FirebaseResponse) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { callback(FirebaseResponse.Success) }
            .addOnCanceledListener { callback(FirebaseResponse.Error) }
            .addOnFailureListener {
                when (it) {
                    //is FirebaseAuthInvalidUserException -> user banned
                    is FirebaseAuthInvalidUserException -> callback(FirebaseResponse.BadEmailOrPassword) //bad email
                    is FirebaseAuthInvalidCredentialsException -> callback(FirebaseResponse.BadEmailOrPassword) //bad password
                    else -> callback(FirebaseResponse.Error)
                }
            }
    }

    fun registerWithEmailAndPassword(email: String, username: String, password: String, callback: (FirebaseResponse) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                JBudget.userRepository.updateUsername(username) { callback(FirebaseResponse.Success) }
            }
            .addOnCanceledListener { callback(FirebaseResponse.Error) }
            .addOnFailureListener {
                when (it) {
                    is FirebaseAuthUserCollisionException -> callback(FirebaseResponse.UserAlreadyExist)
                    else -> callback(FirebaseResponse.Error)
                }
            }
    }


}