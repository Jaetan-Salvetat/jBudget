package fr.jaetan.jbudget.app.category.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import fr.jaetan.jbudget.app.category.CategoryViewModel
import fr.jaetan.jbudget.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateCategoryDialog(budgetId: String, dismiss: () -> Unit) {
    val viewModel = CategoryViewModel()

    Dialog(onDismissRequest = dismiss) {
        Column(
            Modifier
                .padding(20.dp)
                .background(MaterialTheme.colorScheme.background, RoundedCornerShape(20.dp))
        ) {
            Text(stringResource(R.string.new_category), style = MaterialTheme.typography.titleLarge)
            OutlinedTextField(
                value = viewModel.categoryName,
                onValueChange = { viewModel.categoryName = it },
                label = { Text(stringResource(R.string.category_name)) },
                colors = TextFieldDefaults.outlinedTextFieldColors(containerColor = Color.Transparent)
            )
        }
    }
}