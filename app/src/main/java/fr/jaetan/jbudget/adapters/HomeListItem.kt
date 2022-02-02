package fr.jaetan.jbudget.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.Navigation
import fr.jaetan.jbudget.HomeViewFragmentDirections
import fr.jaetan.jbudget.ModalBudgetFragmentDirections
import fr.jaetan.jbudget.R
import fr.jaetan.jbudget.models.Budget
import fr.jaetan.jbudget.services.Database

class HomeListItem(private var context: Context, private  val changeView: (Long) -> Unit) : BaseAdapter() {
    private var budgets: ArrayList<Budget> = Database.store.boxFor(Budget::class.java).all as ArrayList<Budget>
    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return budgets.count()
    }

    override fun getItem(p0: Int): Budget {
        return budgets[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    @SuppressLint("ViewHolder", "SetTextI18n")
    override fun getView(position: Int, p1: View?, parent: ViewGroup?): View {
        //TODO: Init
        val view = inflater.inflate(R.layout.adapter_home_list_item, parent, false)
        val budget = getItem(position)

        view.findViewById<TextView>(R.id.title_budget).text = budget.title
        view.findViewById<TextView>(R.id.home_list_item_total_spent).text = "Total dépensé: ${String.format("%.2f", budget.totalSpent)}€"
        view.findViewById<TextView>(R.id.home_list_item_total_remaining).text = "Total restant: ${String.format("%.2f", budget.total - budget.totalSpent)}€"

        if(budget.items.isEmpty()) {
            val text = TextView(context)
            text.text = "Ce budget n'a pour le moment aucune donnée."
            text.textSize = 16f
            text.textAlignment = TextView.TEXT_ALIGNMENT_CENTER

            view.findViewById<LinearLayout>(R.id.list_budget_items).addView(text)
        }else{
            for(item in budget.items){
                val text = TextView(view.context)
                text.text = "${item.name}: ${String.format("%.2f", item.value)}€"
                text.textSize = 16f

                view.findViewById<LinearLayout>(R.id.list_budget_items).addView(text)
            }
        }


        //TODO: Events
        view.findViewById<ImageButton>(R.id.remove_budget_btn).setOnClickListener {
            Database.store.boxFor(Budget::class.java).remove(getItem(position))
            update()
        }
        view.findViewById<LinearLayout>(R.id.list_item_budget).setOnClickListener {
            val action = HomeViewFragmentDirections.actionHomeViewFragmentToModalBudgetFragment(getItemId(position).toInt())
            Navigation.findNavController(view).navigate(action)
        }


        return view
    }

    private fun update(){
        budgets = Database.store.boxFor(Budget::class.java).all as ArrayList<Budget>
        notifyDataSetChanged()
        changeView(budgets.count().toLong())
    }
}