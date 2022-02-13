package fr.jaetan.jbudget.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import fr.jaetan.jbudget.R
import fr.jaetan.jbudget.models.BudgetItem
import io.objectbox.relation.ToMany

class HomeBudgetListItem(context: Context, private val budgetItems: ToMany<BudgetItem>) : BaseAdapter() {
    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater


    override fun getCount(): Int {
        return budgetItems.count()
    }

    override fun getItem(p0: Int): BudgetItem {
        return budgetItems[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    @SuppressLint("ViewHolder")
    override fun getView(p0: Int, p1: View?, parent: ViewGroup?): View {
        val view = inflater.inflate(R.layout.adapter_home_budget, parent, false)

        if(budgetItems[p0].cashFlow) return view

        view.findViewById<TextView>(R.id.item_name).text = budgetItems[p0].name
        view.findViewById<TextView>(R.id.item_value).text = budgetItems[p0].value.toString()

        return view
    }
}