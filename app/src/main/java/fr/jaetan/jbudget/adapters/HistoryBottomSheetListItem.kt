package fr.jaetan.jbudget.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.TextView
import fr.jaetan.jbudget.R
import fr.jaetan.jbudget.models.BudgetItem
import io.objectbox.relation.ToMany
import org.w3c.dom.Text

class HistoryBottomSheetListItem(private val context: Context, private val titles: ToMany<BudgetItem>, private val callback: (String) -> Unit) : BaseAdapter() {
    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return titles.count()
    }

    override fun getItem(p0: Int): BudgetItem {
        return titles[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    @SuppressLint("ViewHolder")
    override fun getView(p0: Int, p1: View?, parent: ViewGroup?): View {
        val view = inflater.inflate(R.layout.adapter_history_bottom_sheet, parent, false)
        val btnContainer = view.findViewById<LinearLayout>(R.id.btn_container)

        view.findViewById<TextView>(R.id.btn_text).text = titles[p0].name

        btnContainer.setOnClickListener {
            callback(titles[p0].name)
        }

        return view
    }

}