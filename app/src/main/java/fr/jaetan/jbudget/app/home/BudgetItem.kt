package fr.jaetan.jbudget.app.home

import androidx.compose.runtime.MutableState

class BudgetItem(val title: String, var isClosed: MutableState<Boolean>, val categories: List<String>)