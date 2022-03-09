package fr.jaetan.jbudget.budget

import android.annotation.SuppressLint
import android.content.Context
import android.view.*
import android.widget.*
import androidx.core.widget.addTextChangedListener
import fr.jaetan.jbudget.R
import fr.jaetan.jbudget.models.BudgetItem
import fr.jaetan.jbudget.models.BudgetTitle
import fr.jaetan.jbudget.services.Database
import java.lang.Exception
import java.util.ArrayList

class BudgetListItem(private val context: Context) : BaseAdapter(){
    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private var budgetTitles: ArrayList<String> = arrayListOf()
    private var spinnerItems: ArrayList<String> = arrayListOf()
    private var budgetItems: ArrayList<BudgetItem> = arrayListOf()
    private var firstload = true

    override fun getCount(): Int {
        init()
        return budgetItems.count()
    }

    override fun getItem(p0: Int): BudgetItem {
        return budgetItems[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, p1: View?, parent: ViewGroup?): View {
        //TODO: Init
        val view = inflater.inflate(R.layout.adapter_modal_budget_list_item, parent, false)
        val spinner = view.findViewById<Spinner>(R.id.budget_titles_modal_budget)
        val editText = view.findViewById<EditText>(R.id.edittext_budget_value)
        var spinnerId = 0

        spinnerItems = budgetTitles

        spinner.adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, spinnerItems)

        for(i in spinnerItems.indices){
            if(budgetTitles[i] == budgetItems[position].name){
                spinnerId = i
                break
            }
        }

        spinner.setSelection(spinnerId)

        if(budgetItems[position].value > 0.0){
            editText.setText(budgetItems[position].value.toString())
        }

        if(position == 0 && firstload){
            editText.isFocusedByDefault = true
            /*editText.requestFocus()
            val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)*/
            firstload = false
        }



        //TODO: Events
        editText.addTextChangedListener {

            val value = try {
                editText.text.toString().replace(",", ".").toDouble()
            }catch (e: Exception){
                0.0
            }

            budgetItems[position].value = value
        }
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                budgetItems[position].name = spinnerItems[p2]
                budgetItems[position].cashFlow = budgetItems[position].name.lowercase() == "Rentrée d'argent".lowercase()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

        return view
    }

    private fun init(){
        if(budgetTitles.isEmpty()){
            for(value in Database.store.boxFor(BudgetTitle::class.java).all){
                budgetTitles.add(value.name)
            }
            budgetTitles.add("Rentrée d'argent")
            spinnerItems = budgetTitles
        }
        if(budgetItems.count() == 0){
            budgetItems.add(BudgetItem(name = budgetTitles[0], value = 0.0))
        }
    }

    fun update(){
        if(budgetTitles.isEmpty()){
            for(value in Database.store.boxFor(BudgetTitle::class.java).all){
                budgetTitles.add(value.name)
            }
            budgetTitles.add("Rentrée d'argent")
            spinnerItems = budgetTitles
        }

        val name = budgetTitles[0]
        budgetItems.add(BudgetItem(name = name, cashFlow = name.lowercase() == "Rentrée d'argent".lowercase()))
        notifyDataSetChanged()
    }

    fun getValues(): MutableList<BudgetItem> { return budgetItems }
}