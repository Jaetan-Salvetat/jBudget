package fr.jaetan.jbudget.app.budget.views

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun BudgetContent(padding: PaddingValues) {
    LazyColumn(Modifier.padding(padding)) {
        item { }
        items(listOf<Any>()) {

        }
    }
}