package fr.jaetan.jbudget.core.services

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import fr.jaetan.jbudget.core.repositories.AuthRepository
import fr.jaetan.jbudget.core.repositories.BudgetRepository
import fr.jaetan.jbudget.core.repositories.CategoryRepository
import fr.jaetan.jbudget.core.repositories.TransactionRepository
import fr.jaetan.jbudget.core.repositories.UserRepository

class JBudget {
    companion object {
        var state = MainViewModel()

        val authRepository = AuthRepository()
        val userRepository = UserRepository()
        val budgetRepository = BudgetRepository()
        val categoryRepository = CategoryRepository()
        val transactionRepository = TransactionRepository()

        suspend fun init(context: Context) {
            state.init(context)
            initFirebase()
        }

        private fun initFirebase() {
            FirebaseFirestore.getInstance().firestoreSettings = FirebaseFirestoreSettings
                .Builder()
                .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
                .setPersistenceEnabled(true)
                .build()
        }
    }
}