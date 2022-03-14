package fr.jaetan.jbudget.settings

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import fr.jaetan.jbudget.R
import fr.jaetan.jbudget.misc.UiMisc
import fr.jaetan.jbudget.models.BudgetTitle
import fr.jaetan.jbudget.services.Database

class SettingsListItem (private val context: Context) : BaseAdapter() {
    private var dataSource = Database.instance.budgetTitles.all
    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater


    override fun getCount(): Int {
        return dataSource.size
    }
    override fun getItem(position: Int): BudgetTitle {
        return dataSource[position]
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    @SuppressLint("ViewHolder", "SetTextI18n")
    override fun getView(position: Int, p1: View?, parent: ViewGroup?): View {
        //TODO: Init
        val view = inflater.inflate(R.layout.adapter_settings_list_item, parent, false)

        view.findViewById<TextView>(R.id.name_budget_item).text = getItem(position).name


        //TODO: Events
        view.findViewById<ImageButton>(R.id.remove_budget_item).setOnClickListener {
            UiMisc.alertDialog(context, title = "Alerte", text = "Voulez vous vraiment supprimer ce budget ??",
                callback = { dialog, _ ->
                    Database.instance.budgetTitles.remove(getItem(position))
                    update()
                    dialog.cancel()
                })
        }


        return view
    }

    fun update(){
        dataSource = Database.instance.budgetTitles.all
        notifyDataSetChanged()
    }
}