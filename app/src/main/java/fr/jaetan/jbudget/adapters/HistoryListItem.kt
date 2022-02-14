package fr.jaetan.jbudget.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.content.res.AppCompatResources
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
    //private var nbrItemCheck = 0


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
        val textNameItem = view.findViewById<TextView>(R.id.history_item_name)
        val removeBtn = view.findViewById<ImageButton>(R.id.remove_history_item_btn)
        val checkbox = view.findViewById<CheckBox>(R.id.history_item_checked)
        val container = view.findViewById<LinearLayout>(R.id.container)

        textNameItem.text = historyItem.name

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

        if(historyItem.done){
            checkbox.isChecked = true
            checkedStyleManager(true, textValueItem, textNameItem, view.findViewById(R.id.euro), container)
        }

        //TODO: Events
        /*container.setOnLongClickListener {
            true
        }
        container.setOnClickListener {
            if(nbrItemCheck == 0) return@setOnClickListener
        }*/
        checkbox.setOnCheckedChangeListener { _, checked ->
            historyItem.done = checked
            checkedStyleManager(checked, textValueItem, textNameItem, view.findViewById(R.id.euro), container)
            Database.store.boxFor(BudgetHistory::class.java).put(historyItem)
        }

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

    fun update(history: Collection<BudgetHistory>? = null){
        budgetHistory = history as ToMany<BudgetHistory>? ?: Database.store.boxFor(Budget::class.java).all[budgetId].history
        notifyDataSetChanged()
    }

    private fun checkedStyleManager(checked: Boolean, textValue: TextView, textName: TextView, textEuro: TextView, container: LinearLayout){
        if(checked){
            textValue.setTypeface(null, Typeface.BOLD_ITALIC)
            textName.setTypeface(null, Typeface.ITALIC)
            textEuro.setTypeface(null, Typeface.ITALIC)
            container.foreground = AppCompatResources.getDrawable(context, R.drawable.history_item_done)
            return
        }
        textValue.setTypeface(null, Typeface.BOLD)
        textName.setTypeface(null, Typeface.NORMAL)
        textEuro.setTypeface(null, Typeface.NORMAL)
        container.foreground = null
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