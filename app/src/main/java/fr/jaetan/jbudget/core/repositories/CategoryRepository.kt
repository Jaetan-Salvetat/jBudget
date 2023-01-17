package fr.jaetan.jbudget.core.repositories

import com.google.firebase.firestore.FirebaseFirestore
import fr.jaetan.jbudget.core.models.Category
import fr.jaetan.jbudget.core.models.FirebaseResponse

class CategoryRepository {
    private val database = FirebaseFirestore.getInstance().collection("categories")

    fun createCategory(category: Category, callback: (String?, FirebaseResponse) -> Unit) {
        database.add(category.toMap())
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    callback(it.result.id, FirebaseResponse.Success)
                    return@addOnCompleteListener
                }
                callback(null, FirebaseResponse.Error)
            }
    }

    fun getAll(budgetId: String?, callback: (List<Category>, FirebaseResponse) -> Unit) {
        database.whereEqualTo("budgetId", budgetId).get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    callback(Category.fromMapList(it.result.documents), FirebaseResponse.Success)
                    return@addOnCompleteListener
                }
                callback(listOf(), FirebaseResponse.Error)
            }
    }
}