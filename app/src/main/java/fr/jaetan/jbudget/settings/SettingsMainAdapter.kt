package fr.jaetan.jbudget.settings

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import fr.jaetan.jbudget.R
import fr.jaetan.jbudget.misc.UiMisc
import fr.jaetan.jbudget.services.Database



class SettingsMainAdapter : RecyclerView.Adapter<SettingsMainAdapter.ViewHolder>() {
    private var titles = Database.instance.budgetTitles.all

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_settings_list_item, parent, false)
        return ViewHolder(view, parent.context)
    }

    override fun onBindViewHolder(view: ViewHolder, position: Int) {
        view.name.text = titles[position].name

        view.removeBtn.setOnClickListener { removeTitle(view.context, position) }
    }

    override fun getItemCount(): Int {
        return titles.count()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(){
        titles = Database.instance.budgetTitles.all
        notifyDataSetChanged()
    }


    private fun removeTitle(context: Context, id: Int){
        UiMisc.alertDialog(context, title = "Alerte", text = "Voulez vous vraiment supprimer ce budget ??",
            callback = { dialog, _ ->
                Database.instance.budgetTitles.remove(titles[id])
                update()
                dialog.cancel()
            })
    }

    class ViewHolder(view: View, val context: Context) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.name_budget_item)
        val removeBtn: ImageButton = view.findViewById(R.id.remove_budget_item)
    }
}