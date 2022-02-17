package fr.jaetan.jbudget.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.TextView
import fr.jaetan.jbudget.R
import fr.jaetan.jbudget.models.Budget
import fr.jaetan.jbudget.models.BudgetHistory
import fr.jaetan.jbudget.models.SortType
import fr.jaetan.jbudget.services.Database
import io.objectbox.relation.ToMany

class HistoryBottomSheetListItem(context: Context, private val budgetId: Int, private val titles: ArrayList<Map<String, SortType>>, private val callback: (Collection<BudgetHistory>) -> Unit) : BaseAdapter() {
    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private lateinit var historyItems: ToMany<BudgetHistory>

    override fun getCount(): Int {
        return titles.count()
    }

    override fun getItem(p0: Int): Map<String, SortType> {
        return titles[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    @SuppressLint("ViewHolder", "SetTextI18n")
    override fun getView(p0: Int, p1: View?, parent: ViewGroup?): View {
        val view = inflater.inflate(R.layout.adapter_history_bottom_sheet, parent, false)
        val btnContainer = view.findViewById<LinearLayout>(R.id.btn_container)
        historyItems = Database.store.boxFor(Budget::class.java).all[budgetId].history

        if(p0 == 0 || titles[p0].values.first() == titles[p0 - 1].values.first()){
            view.findViewById<LinearLayout>(R.id.container_title).removeAllViewsInLayout()
        }

        view.findViewById<TextView>(R.id.btn_text).text = titles[p0].keys.first()

        btnContainer.setOnClickListener {_ ->

            when(titles[p0].values.first()){
                SortType.Done -> historyItems.removeIf { !it.done }
                SortType.NotDone -> historyItems.removeIf { it.done }
                SortType.Name -> historyItems.removeIf { it.name != titles[p0].keys.first() }
                else -> {}
            }

            callback(historyItems)
        }

        return view
    }

}