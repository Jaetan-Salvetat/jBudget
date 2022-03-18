package fr.jaetan.jbudget.misc

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import fr.jaetan.jbudget.models.Budget

class FuncMisc {
    companion object {
        private val months: ArrayList<String> = arrayListOf(
            "Janvier",
            "Février",
            "Mars",
            "Avril",
            "Mai",
            "Juin",
            "Juillet",
            "Août",
            "Septembre",
            "Octobre",
            "Novembre",
            "Décembre",
        )

        fun getStringMonth(id: Int): String {
            if(id < 0) return "null"
            return months[id]
        }

        fun shareBudget(context: Context, budget: Budget){
            var res: String = "Total dépensé: ${String.format("%.2f", budget.totalSpent)}€\n" +
                    "Total resntant: ${String.format("%.2f", budget.total - budget.totalSpent)}€\n\n"

            for(item in budget.items){
                res += "${item.name}: ${String.format("%.2f", item.value)}€\n"
            }

            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, res)
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(context, shareIntent, null)
        }
    }
}