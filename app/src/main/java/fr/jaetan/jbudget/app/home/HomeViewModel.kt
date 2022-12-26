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
import fr.jaetan.jbudget.core.models.Screen

class HomeViewModel(navController: NavHostController) : ViewModel() {
    //#region FAB
    //=========//
    //===FAB===//
    //=========//
    var fabExpanded by mutableStateOf(false)
    val fabItems = listOf(
        FabItem(text = R.string.home_fab_add_transaction, descriptor = R.string.home_fab_add_transaction_descriptor, onClick = {navController.navigate(
            Screen.Transaction.route)}, Icons.Default.RequestQuote),
        FabItem(text = R.string.home_fab_add_budget, descriptor = R.string.home_fab_add_budget_descriptor, onClick = {/* TODO */}, Icons.Default.NoteAdd),
    )
    //#endregion


    //#region Hints
    //===========//
    //===HINTS===//
    //===========//
    val hintItems = listOf(
        HintItem(R.string.hint_1){},
        HintItem(R.string.hint_2){},
        HintItem(R.string.hint_3){},
        HintItem(R.string.hint_4){},
    )
    var currentHint by mutableStateOf(hintItems[0])
    var hintCount = 0
    fun nextHint() {
        hintCount++
        if(hintCount >= hintItems.size) { hintCount -= hintItems.size }
    }
    //#endregion


    //#region Budget
    //============//
    //===BUDGET===//
    //============//
    val budgetList = listOf(
        BudgetItem("Décembre", mutableStateOf(true), listOf("Nourriture", "Loisir", "Transport", "Noël", "École")),
        BudgetItem("Novembre", mutableStateOf(false), listOf("École", "Charges", "Loisirs")),
        BudgetItem("2022", mutableStateOf(false), listOf("Charges", "Loisir", "Crédits", "Autres"))
    )
    //#endregion
}

