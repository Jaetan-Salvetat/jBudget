package fr.jaetan.jbudget.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.Navigation
import fr.jaetan.jbudget.HomeViewFragmentDirections
import fr.jaetan.jbudget.R
import fr.jaetan.jbudget.misc.UiMisc
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

    @SuppressLint("ViewHolder", "SetTextI18n", "ClickableViewAccessibility")
    override fun getView(position: Int, p1: View?, parent: ViewGroup?): View {
        //TODO: Init
        val id = budgets.count() - position - 1
        val view = inflater.inflate(R.layout.adapter_home_list_item, parent, false)
        val budget = getItem(id)
        val totalRemaining = view.findViewById<TextView>(R.id.home_list_item_total_remaining)
        val totalSpent = view.findViewById<TextView>(R.id.home_list_item_total_spent)

        view.findViewById<TextView>(R.id.title_budget).text = budget.title
        totalSpent.text = String.format("%.2f", budget.totalSpent)
        totalRemaining.text = String.format("%.2f", budget.total - budget.totalSpent)

        if(budget.total - budget.totalSpent > 0.0){
            totalRemaining.setTextColor(Color.parseColor("#32CD32"))//ff0000
            totalSpent.setTextColor(Color.parseColor("#32CD32"))//ff0000
        }else if(budget.total - budget.totalSpent < 0.0){
            totalRemaining.setTextColor(Color.parseColor("#ff0000"))//ff0000
            totalSpent.setTextColor(Color.parseColor("#ff0000"))//ff0000
        }

        if(budget.items.isEmpty()) {
            val text = TextView(context)
            text.text = "Ce budget n'a pour le moment aucune donnée."
            text.textSize = 16f
            text.textAlignment = TextView.TEXT_ALIGNMENT_CENTER

            view.findViewById<LinearLayout>(R.id.list_item_budget).addView(text)
        }else{
            /*for(item in budget.items){
                val text = TextView(view.context)
                text.text = "${item.name}: ${String.format("%.2f", item.value)}€"
                text.textSize = 16f

                view.findViewById<LinearLayout>(R.id.list_budget_items).addView(text)
            }*/
            val layout = view.findViewById<LinearLayout>(R.id.list_item_budget)
            val list = ListView(context)
            val layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 60 * budget.items.count())

            list.adapter = HomeBudgetListItem(context, budget.items)
            list.layoutParams = layoutParams
            list.divider = null
            list.scrollBarSize = 0
            layout.addView(list)

        }


        //TODO: Events
        view.findViewById<ImageButton>(R.id.remove_budget_btn).setOnClickListener {
            UiMisc.alertDialog(this.context, title = "Alerte", text = "Voulez vous vraiment supprimer ce budget ??",
                callback = { dialog, _ ->
                    Database.store.boxFor(Budget::class.java).remove(getItem(id))
                    update()
                    dialog.cancel()
                })
        }
        view.findViewById<LinearLayout>(R.id.list_item_budget).setOnClickListener {
            val action = HomeViewFragmentDirections.actionHomeViewFragmentToModalBudgetFragment(id)
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