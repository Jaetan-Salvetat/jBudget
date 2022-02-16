package fr.jaetan.jbudget.widgets

import android.content.Context
import android.os.Bundle
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebHistoryItem
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import fr.jaetan.jbudget.R
import fr.jaetan.jbudget.adapters.HistoryBottomSheetListItem
import fr.jaetan.jbudget.models.BudgetHistory
import fr.jaetan.jbudget.models.BudgetItem
import fr.jaetan.jbudget.models.SortType
import io.objectbox.relation.ToMany
import kotlin.reflect.KProperty0

class HistoryBottomSheet(private val c: Context, private val budgetId: Int, private val titles: ArrayList<Map<String, SortType>>, private val callback: (Collection<BudgetHistory>) -> Unit) : BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.history_bottom_sheet, container, false)
        val listView = view.findViewById<ListView>(R.id.listview_history_bottom_sheet)
        val adapter = HistoryBottomSheetListItem(c, budgetId, titles, callback)

        listView.adapter = adapter
        return view
    }
}