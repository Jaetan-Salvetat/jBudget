package fr.jaetan.jbudget.home

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import fr.jaetan.jbudget.HomeViewFragmentDirections
import fr.jaetan.jbudget.R
import fr.jaetan.jbudget.misc.UiMisc
import fr.jaetan.jbudget.models.Budget
import fr.jaetan.jbudget.services.Database
import fr.jaetan.jbudget.services.ObjectBox

class HomeMainAdapter(private  val changeView: () -> Unit) : RecyclerView.Adapter<HomeMainAdapter.ViewHolder>() {
    private var budgets = Database.instance.budgets.all


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_home_list_item, parent, false)

        return ViewHolder(view, parent.context)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(view: ViewHolder, position: Int) {
        val id: Int = if(position == 0){
            itemCount - 1
        }else{
            view.divider.removeAllViewsInLayout()
            view.topSpace.removeAllViewsInLayout()
                itemCount - position - 1
        }

        val budget = budgets[id]

        view.budgetsContainer.removeAllViewsInLayout()

        UiMisc.scaleAnimation(view.container) {
            val action = HomeViewFragmentDirections.actionHomeViewFragmentToModalBudgetFragment(id)
            Navigation.findNavController(view.itemView).navigate(action)
        }

        view.title.text = budget.title
        view.totalSpent.text = String.format("%.2f", budget.totalSpent)
        view.totalRemaining.text = String.format("%.2f", budget.total - budget.totalSpent)

        if(budget.total - budget.totalSpent > 0.0){
            view.totalRemaining.setTextColor(Color.parseColor("#32CD32"))//ff0000
        }else if(budget.total - budget.totalSpent < 0.0){
            view.totalRemaining.setTextColor(Color.parseColor("#ff0000"))//ff0000
        }

        if(budget.items.isEmpty()) {
            val text = TextView(view.context)
            text.text = "Ce budget n'a pour le moment aucune donnée."
            text.textSize = 16f
            text.textAlignment = TextView.TEXT_ALIGNMENT_CENTER

            view.budgetsContainer.addView(text)
        }

        for(item in budget.items){
            val layout = LinearLayout(view.context)
            val layoutName = LinearLayout(view.context)
            val name = TextView(view.context)
            val value = TextView(view.context)
            val euro = TextView(view.context)

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
            view.budgetsContainer.addView(layout)
        }


        //TODO: Events
        view.removeBtn.setOnClickListener { removeBudget(id, view.context) }
        view.goToHistoryBtn.setOnClickListener {
            val action = HomeViewFragmentDirections.actionHomeViewFragmentToHistoryFragment(id)
            Navigation.findNavController(view.itemView).navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return budgets.count()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(){
        budgets = Database.instance.budgets.all
        notifyDataSetChanged()
    }

    private fun removeBudget(id: Int, context: Context){
        UiMisc.alertDialog(context, title = "Alerte", text = "Voulez vous vraiment supprimer ce budget ?",
            callback = { dialog, _ ->
                ObjectBox.store.boxFor(Budget::class.java).remove(budgets[id])
                changeView()
                dialog.cancel()
            })
    }

    class ViewHolder(view: View, c: Context) : RecyclerView.ViewHolder(view) {
        val container: LinearLayout = view.findViewById(R.id.list_item_budget)
        val divider: LinearLayout = view.findViewById(R.id.divider)
        val topSpace: LinearLayout = view.findViewById(R.id.top_space)
        val budgetsContainer: LinearLayout = view.findViewById(R.id.container_budget_items)

        val title: TextView = view.findViewById(R.id.title_budget)
        val totalRemaining: TextView = view.findViewById(R.id.home_list_item_total_remaining)
        val totalSpent: TextView = view.findViewById(R.id.home_list_item_total_spent)

        val removeBtn: ImageButton = view.findViewById(R.id.remove_budget_btn)
        val goToHistoryBtn: ImageButton = view.findViewById(R.id.go_to_history)

        val context = c
    }
}

