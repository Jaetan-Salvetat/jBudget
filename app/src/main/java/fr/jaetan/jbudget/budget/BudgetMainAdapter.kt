package fr.jaetan.jbudget.budget

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import fr.jaetan.jbudget.R
import fr.jaetan.jbudget.models.BudgetItem
import fr.jaetan.jbudget.models.BudgetTitle_
import fr.jaetan.jbudget.services.Database
import java.lang.Exception
import java.util.ArrayList

class BudgetMainAdapter : RecyclerView.Adapter<BudgetMainAdapter.ViewHolder>() {
    private var budgetTitles: ArrayList<String> = arrayListOf()
    private var spinnerItems: ArrayList<String> = arrayListOf()
    private var budgetItems: ArrayList<BudgetItem> = arrayListOf()
    private var firstload = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_modal_budget_list_item, parent, false)

        return ViewHolder(view, parent.context)
    }

    override fun onBindViewHolder(view: ViewHolder, p1: Int) {
        var spinnerId = 0
        val position = p1

        spinnerItems = budgetTitles

        view.spinner.adapter = ArrayAdapter(view.context, android.R.layout.simple_spinner_item, spinnerItems)

        for(i in spinnerItems.indices){
            if(budgetTitles[i] == budgetItems[position].name){
                spinnerId = i
                break
            }
        }

        view.spinner.setSelection(spinnerId)

        if(budgetItems[position].value > 0.0){
            view.editText.setText(budgetItems[position].value.toString())
        }

        if(position == 0 && firstload){
            view.editText.isFocusedByDefault = true
            /*editText.requestFocus()
            val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)*/
            firstload = false
        }



        //TODO: Events
        view.editText.addTextChangedListener { inputManager(view, position) }

        view.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                budgetItems[position].name = spinnerItems[p2]
                if(spinnerItems[p2].lowercase() != "rentrée d'argent"){
                    budgetItems[position].title.target = Database.instance.budgetTitles.query(
                        BudgetTitle_.name.equal(spinnerItems[p2])).build().find()[0]
                }
                budgetItems[position].cashFlow = budgetItems[position].name.lowercase() == "Rentrée d'argent".lowercase()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}

        }
    }

    override fun getItemCount(): Int {
        init()
        return budgetItems.count()
    }

    class ViewHolder(view: View, val context: Context) : RecyclerView.ViewHolder(view){
        val spinner: Spinner = view.findViewById(R.id.budget_titles_modal_budget)
        val editText: EditText = view.findViewById(R.id.edittext_budget_value)
    }



    private fun init(){
        if(budgetTitles.isEmpty()){
            for(value in Database.instance.budgetTitles.all){
                budgetTitles.add(value.name)
            }
            budgetTitles.add("Rentrée d'argent")
            spinnerItems = budgetTitles
        }
        if(budgetItems.count() == 0){
            budgetItems.add(BudgetItem(name = budgetTitles[0], value = 0.0))
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(){
        if(budgetTitles.isEmpty()){
            for(value in Database.instance.budgetTitles.all){
                budgetTitles.add(value.name)
            }
            budgetTitles.add("Rentrée d'argent")
            spinnerItems = budgetTitles
        }

        val name = budgetTitles[0]
        val item = BudgetItem(name = name, cashFlow = name.lowercase() == "Rentrée d'argent".lowercase())
        item.title.target = Database.instance.budgetTitles.all[0]
        budgetItems.add(item)
        notifyDataSetChanged()
    }

    private fun inputManager(view: ViewHolder, position: Int){
        val value = try {
            view.editText.text.toString().replace(",", ".").toDouble()
        }catch (e: Exception){
            0.0
        }

        budgetItems[position].value = value
    }

    fun getValues(): MutableList<BudgetItem> { return budgetItems }
}