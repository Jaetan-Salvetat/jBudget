package fr.jaetan.jbudget.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.marginTop
import fr.jaetan.jbudget.R
import fr.jaetan.jbudget.misc.UiMisc
import fr.jaetan.jbudget.models.Budget
import fr.jaetan.jbudget.models.BudgetHistory
import fr.jaetan.jbudget.models.BudgetItem
import fr.jaetan.jbudget.services.Database
import io.objectbox.relation.ToMany
import java.text.FieldPosition

class HistoryListItem(private val context: Context, private var budgetHistory: ToMany<BudgetHistory>, private val budgetId: Int): BaseAdapter() {
    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private var lastDate: String? = ""


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
        val view = inflater.inflate(R.layout.adapter_history_list_item, parent, false)
        val historyItem = getItem(count - position - 1)

        view.findViewById<TextView>(R.id.history_item_name).text = "${historyItem.name}: ${historyItem.value}€"
        val removeBtn = view.findViewById<ImageButton>(R.id.remove_history_item_btn)

        /*if(position == 0 && historyItem.date != null){
            createDateLayoutWidget(historyItem.date, view.findViewById(R.id.history_item_date))
        }else if(position > 0 && historyItem.date != getItem(count - position).date){
            createDateLayoutWidget(historyItem.date, view.findViewById(R.id.history_item_date))
        }*/


        //TODO: Events
        removeBtn.setOnClickListener {
            UiMisc.alertDialog(this.context, title = "Alerte", text = "Voulez vous vraiment supprimer ce budget ??",
                callback = { dialog, _ ->
                    val budget = Database.store.boxFor(Budget::class.java).all[budgetId]

                    if(historyItem.name.lowercase() == "Rentrée d'argent".lowercase()){
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


        return view
    }

    private fun update(){
        budgetHistory = Database.store.boxFor(Budget::class.java).all[budgetId].history
        notifyDataSetChanged()
    }

    private fun createDateLayoutWidget(date: String?, parent: LinearLayout){
        val dateText = TextView(context)
        dateText.text = date
        dateText.textSize = 25f

        val divider = View(context)
        divider.minimumHeight = 1
        divider.layoutParams.height = 1
        divider.top = 5
        divider.right = 15
        divider.bottom = 15
        divider.setBackgroundColor(0xa9a9a9)

        parent.addView(dateText)
        parent.addView(divider)
    }
}

/*
            <View
                android:layout_width="wrap_content"
                android:layout_height="1dp"
                android:layout_marginEnd="15dp"
                android:layout_marginTop="5dp"
                android:layout_marginBottom="15dp"
                android:background="@android:color/darker_gray"/>
*/