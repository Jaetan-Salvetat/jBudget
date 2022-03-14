package fr.jaetan.jbudget.services

import fr.jaetan.jbudget.models.Budget
import fr.jaetan.jbudget.models.BudgetHistory
import fr.jaetan.jbudget.models.BudgetItem
import fr.jaetan.jbudget.models.BudgetTitle
import io.objectbox.Box

class Database {
    val budgets: Box<Budget> = ObjectBox.store.boxFor(Budget::class.java)
    private val budgetItems: Box<BudgetItem> = ObjectBox.store.boxFor(BudgetItem::class.java)
    val budgetHistory: Box<BudgetHistory> = ObjectBox.store.boxFor(BudgetHistory::class.java)
    val budgetTitles: Box<BudgetTitle> = ObjectBox.store.boxFor(BudgetTitle::class.java)

    fun put(budget: Budget){
        budgets.put(budget)
        budgetItems.put(budget.items)
        budgetHistory.put(budget.history)
    }
    fun put(budgetTitle: BudgetTitle){
        budgetTitles.put(budgetTitle)
    }
    fun put(budgetTitle: ArrayList<BudgetTitle>){
        budgetTitles.put(budgetTitle)
    }

    companion object {
        lateinit var instance: Database

        fun init(){
            instance = Database()
        }
    }
}