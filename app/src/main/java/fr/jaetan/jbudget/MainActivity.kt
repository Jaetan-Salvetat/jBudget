package fr.jaetan.jbudget

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import fr.jaetan.jbudget.models.BudgetTitle
import fr.jaetan.jbudget.services.Database
import fr.jaetan.jbudget.services.ObjectBox

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //TODO: Init
        try {
            ObjectBox.init(this)
            Database.init()
        }catch (e: Exception){
            print(e.toString())
        }

        if(Database.instance.budgetTitles.count() > 0) return

        val items = arrayListOf(
            BudgetTitle(name = "Alimentations"),
            BudgetTitle(name = "Loisirs"),
            BudgetTitle(name = "Factures"),
            BudgetTitle(name = "Transports"),
            BudgetTitle(name = "Santés"),
            BudgetTitle(name = "Autres")
        )
        Database.instance.put(items)
    }
}