package fr.jaetan.jbudget.core.repositories

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.snapshots
import fr.jaetan.jbudget.core.models.Category
import fr.jaetan.jbudget.core.services.JBudget

class CategoryRepository {
    private val database = FirebaseFirestore.getInstance().collection("categories")

    fun createCategory(category: Category, callback: () -> Unit) {
        database.add(category.toMap())
            .addOnCompleteListener { callback() }
    }

    suspend fun initListener() {
        database.whereEqualTo("userId", JBudget.state.currentUser?.uid)
            .snapshots()
            .collect { query ->
                JBudget.state.categories.clear()
                JBudget.state.categories.addAll(Category.fromMapList(query.documents))
            }
    }

    fun updateCategoryName(category: Category, onComplete: () -> Unit) {
        database.document(category.id).set(category.toMap())
            .addOnCompleteListener { onComplete() }
    }

    fun removeAll() {
        JBudget.state.categories.forEach {
            database.document(it.id).delete()
        }
    }
}