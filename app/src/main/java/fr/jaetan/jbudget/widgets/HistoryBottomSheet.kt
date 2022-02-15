package fr.jaetan.jbudget.widgets

import android.content.Context
import android.os.Bundle
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import fr.jaetan.jbudget.R
import fr.jaetan.jbudget.adapters.HistoryBottomSheetListItem
import fr.jaetan.jbudget.models.BudgetItem
import io.objectbox.relation.ToMany

class HistoryBottomSheet(private val c: Context, private val titles: ToMany<BudgetItem>, private val callback: (String) -> Unit) : BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.history_bottom_sheet, container, false)
        val listView = view.findViewById<ListView>(R.id.listview_history_bottom_sheet)
        val adapter = HistoryBottomSheetListItem(c, titles, callback)

        listView.adapter = adapter
        return view
    }
}