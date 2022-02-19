package fr.jaetan.jbudget.history

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import fr.jaetan.jbudget.R
import fr.jaetan.jbudget.history.HistoryBottomSheetListItem
import fr.jaetan.jbudget.models.BudgetHistory
import fr.jaetan.jbudget.models.SortType

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