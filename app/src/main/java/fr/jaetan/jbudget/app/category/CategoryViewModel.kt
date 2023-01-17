package fr.jaetan.jbudget.app.category

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class CategoryViewModel: ViewModel() {
    var categoryName by mutableStateOf("")
}