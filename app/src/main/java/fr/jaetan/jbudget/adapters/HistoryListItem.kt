package fr.jaetan.jbudget.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.TextView
import fr.jaetan.jbudget.R
import fr.jaetan.jbudget.misc.FuncMisc
import fr.jaetan.jbudget.misc.UiMisc
import fr.jaetan.jbudget.models.Budget
import fr.jaetan.jbudget.models.BudgetHistory
import fr.jaetan.jbudget.models.BudgetItem
import fr.jaetan.jbudget.services.Database
import io.objectbox.relation.ToMany

class HistoryListItem(private val context: Context, private var budgetHistory: ToMany<BudgetHistory>, private val budgetId: Int): BaseAdapter() {
    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater


    override fun getCount(): Int {
        return budgetHistory.count()
    }

    override fun getItem(p0: Int): BudgetHistory {
        return budgetHistory[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    @SuppressLint("ViewHolder", "SetTextI18n")
    override fun getView(position: Int, p1: View?, parent: ViewGroup?): View {
        //TODO: Init
        val historyItem = getItem(count - position - 1)
        var cashFlowModified = false
        val view: View

        if(position > 0 && historyItem.date != getItem(count - position).date || position == 0){
            view = inflater.inflate(R.layout.adapter_history_list_item_with_title, parent, false)

            dateManager(historyItem.date, view)
        }else{
            view = inflater.inflate(R.layout.adapter_history_list_item_without_title, parent, false)
        }
        val textValueItem = view.findViewById<TextView>(R.id.history_item_value)
        val removeBtn = view.findViewById<ImageButton>(R.id.remove_history_item_btn)

        view.findViewById<TextView>(R.id.history_item_name).text = historyItem.name

        if(historyItem.cashFlow || historyItem.name.lowercase() == "Rentrée d'argent".lowercase()){
            textValueItem.text = " +${String.format("%.2f", historyItem.value)}"
            textValueItem.setTextColor(Color.parseColor("#32CD32"))//ff0000
        }else{
            textValueItem.text = " -${String.format("%.2f", historyItem.value)}"
            textValueItem.setTextColor(Color.parseColor("#ff0000"))//ff0000
        }

        //clean value cashFlow in BudgetHistory database
        if(historyItem.name.lowercase() == "Rentrée d'argent".lowercase()){
            historyItem.cashFlow = true
            cashFlowModified = true
        }

        //TODO: Events
        removeBtn.setOnClickListener {
            UiMisc.alertDialog(this.context, title = "Alerte", text = "Voulez vous vraiment supprimer ce budget ??",
                callback = { dialog, _ ->
                    val budget = Database.store.boxFor(Budget::class.java).all[budgetId]

                    if(historyItem.name.lowercase() == "Rentrée d'argent".lowercase() || historyItem.cashFlow){
                        budget.total -= historyItem.value
                    }else{
                        for(i in budget.items.indices){
                            if(budget.items[i].name.lowercase() == historyItem.name.lowercase()){
                                budget.totalSpent -= historyItem.value
                                budget.items[i].value -= historyItem.value
                                break
                            }
                        }
                    }

                    Database.store.boxFor(Budget::class.java).put(budget)
                    Database.store.boxFor(BudgetItem::class.java).put(budget.items)
                    Database.store.boxFor(BudgetHistory::class.java).remove(historyItem)

                    update()
                    dialog.cancel()
                })
        }

        if(cashFlowModified){
            Database.store.boxFor(BudgetHistory::class.java).put(historyItem)
        }

        return view
    }

    private fun update(){
        budgetHistory = Database.store.boxFor(Budget::class.java).all[budgetId].history
        notifyDataSetChanged()
    }

    @SuppressLint("SetTextI18n")
    private fun dateManager(date: String?, view: View){
        if(date == null){
            view.findViewById<TextView>(R.id.title_date).text = "Aucune date enregistré"
            return
        }


        val dateArray = date.split('-')
        val day: String = dateArray[0]
        val month: String = FuncMisc.getStringMonth((date.split('-')[1].toInt()) -1)
        val year: String = dateArray[2]

        view.findViewById<TextView>(R.id.title_date).text = "$day $month $year"
    }
}