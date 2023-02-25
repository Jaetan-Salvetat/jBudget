package fr.jaetan.jbudget.core.services

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import fr.jaetan.jbudget.core.repositories.*

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
        }

        fun initFireStore() {
            FirebaseFirestore.getInstance().clearPersistence()
        }
    }
}