package fr.jaetan.jbudget

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import fr.jaetan.jbudget.models.BudgetTitle
import fr.jaetan.jbudget.services.Database

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //TODO: Init
        try {
            Database.init(this)
        }catch (e: Exception){
            print(e.toString())
        }

        val budgetTitles = Database.store.boxFor(BudgetTitle::class.java)
        if(budgetTitles.count() > 0) return

        val items = arrayListOf(
            BudgetTitle(name = "Alimentations"),
            BudgetTitle(name = "Loisirs"),
            BudgetTitle(name = "Factures"),
            BudgetTitle(name = "Transports"),
            BudgetTitle(name = "Santés"),
            BudgetTitle(name = "Autres")
        )
        budgetTitles.put(items)
    }
}