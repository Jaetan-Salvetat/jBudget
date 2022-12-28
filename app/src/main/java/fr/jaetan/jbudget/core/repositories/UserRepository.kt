package fr.jaetan.jbudget.core.repositories

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest

class UserRepository {
    private val auth = FirebaseAuth.getInstance()

    fun updateUsername(username: String, callback: () -> Unit) {
        val profileChangeRequest = userProfileChangeRequest {
            displayName = username
        }

        auth.currentUser?.updateProfile(profileChangeRequest)
            ?.addOnCompleteListener { callback() }
    }
}