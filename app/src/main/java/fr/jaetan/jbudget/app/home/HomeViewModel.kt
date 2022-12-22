package fr.jaetan.jbudget.app.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NoteAdd
import androidx.compose.material.icons.filled.RequestQuote
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import fr.jaetan.jbudget.R

class HomeViewModel(navController: NavHostController) : ViewModel() {
    var fabExpanded by mutableStateOf(false)
    val fabItems = listOf(
        FabItem(text = R.string.home_fab_add_transaction, descriptor = R.string.home_fab_add_transaction_descriptor, onClick = {navController.navigate("transaction")}, Icons.Default.RequestQuote),
        FabItem(text = R.string.home_fab_add_budget, descriptor = R.string.home_fab_add_budget_descriptor, onClick = {/* TODO */}, Icons.Default.NoteAdd),
    )
}