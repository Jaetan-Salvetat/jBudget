package fr.jaetan.jbudget.home

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
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
import fr.jaetan.jbudget.services.ObjectBox

class HomeListItem(private var context: Context, private  val changeView: () -> Unit) : BaseAdapter() {
    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private var budgets = Database.instance.budgets.all

    override fun getCount(): Int {
        return budgets.count() - 1
    }

    override fun getItem(p0: Int): Budget {
        return budgets[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    @SuppressLint("ViewHolder", "SetTextI18n", "ClickableViewAccessibility")
    override fun getView(position: Int, p1: View?, parent: ViewGroup?): View? {
        //TODO: Init
        val id: Int
        val view = inflater.inflate(R.layout.adapter_home_list_item, parent, false)
        val totalRemaining = view.findViewById<TextView>(R.id.home_list_item_total_remaining)
        val totalSpent = view.findViewById<TextView>(R.id.home_list_item_total_spent)
        val container = view.findViewById<LinearLayout>(R.id.list_item_budget)
        val removeBtn = view.findViewById<ImageButton>(R.id.remove_budget_btn)

        if(position == -1){
            id = count
        }else{
            id = count - position - 1
            view.findViewById<LinearLayout>(R.id.divider).removeAllViewsInLayout()
        }


        val budget = getItem(id)

        UiMisc.scaleAnimation(container) {
            val action = HomeViewFragmentDirections.actionHomeViewFragmentToModalBudgetFragment(id)
            Navigation.findNavController(view).navigate(action)
        }

        view.findViewById<TextView>(R.id.title_budget).text = budget.title
        totalSpent.text = String.format("%.2f", budget.totalSpent)
        totalRemaining.text = String.format("%.2f", budget.total - budget.totalSpent)

        if(budget.total - budget.totalSpent > 0.0){
            totalRemaining.setTextColor(Color.parseColor("#32CD32"))//ff0000
        }else if(budget.total - budget.totalSpent < 0.0){
            totalRemaining.setTextColor(Color.parseColor("#ff0000"))//ff0000
        }

        if(budget.items.isEmpty()) {
            val text = TextView(context)
            text.text = "Ce budget n'a pour le moment aucune donnée."
            text.textSize = 16f
            text.textAlignment = TextView.TEXT_ALIGNMENT_CENTER

            container.addView(text)
        }else{
            for(item in budget.items){
                val layout = LinearLayout(context)
                val layoutName = LinearLayout(context)
                val name = TextView(context)
                val value = TextView(context)
                val euro = TextView(context)

                layoutName.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                name.text = item.name
                name.textSize = 18f
                value.text = String.format("%.2f", item.value)
                value.textSize = 18f
                value.setTypeface(null, Typeface.BOLD)
                euro.text = "€"
                euro.textSize = 18f

                layoutName.addView(name)
                layout.addView(layoutName)
                layout.addView(value)
                layout.addView(euro)
                view.findViewById<LinearLayout>(R.id.container_budget_items).addView(layout)
            }
        }


        //TODO: Events
        removeBtn.setOnClickListener {
            UiMisc.alertDialog(context, title = "Alerte", text = "Voulez vous vraiment supprimer ce budget ?",
                callback = { dialog, _ ->
                    ObjectBox.store.boxFor(Budget::class.java).remove(getItem(id))
                    changeView()
                    dialog.cancel()
                })
        }
        view.findViewById<ImageButton>(R.id.go_to_history).setOnClickListener {
            val action = HomeViewFragmentDirections.actionHomeViewFragmentToHistoryFragment(id)
            Navigation.findNavController(view).navigate(action)
        }


        return view
    }

    fun update(){
        budgets = Database.instance.budgets.all
        notifyDataSetChanged()
    }
}