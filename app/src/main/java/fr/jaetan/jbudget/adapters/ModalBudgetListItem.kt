package fr.jaetan.jbudget.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.text.InputType.TYPE_CLASS_NUMBER
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.contains
import androidx.core.widget.addTextChangedListener
import com.google.android.material.navigation.NavigationBarView
import fr.jaetan.jbudget.R
import fr.jaetan.jbudget.models.BudgetItem
import fr.jaetan.jbudget.models.BudgetTitle
import fr.jaetan.jbudget.services.Database
import java.lang.Exception
import java.util.ArrayList

class ModalBudgetListItem(private val context: Context) : BaseAdapter(){
    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private var budgetTitles: ArrayList<String> = arrayListOf()
    private var spinnerItems: ArrayList<String> = arrayListOf()
    private var budgetItems: ArrayList<BudgetItem> = arrayListOf()

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
    override fun getView(position: Int, p1: View?, parent: ViewGroup?): View {
        //TODO: Init
        val view = inflater.inflate(R.layout.adapter_modal_budget_list_item, parent, false)
        val spinner = view.findViewById<Spinner>(R.id.budget_titles_modal_budget)
        val editText = view.findViewById<EditText>(R.id.edittext_budget_value)
        var spinnerId = 0

        spinnerItems = budgetTitles
        editText.inputType = TYPE_CLASS_NUMBER

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


        //TODO: Events
        editText.addTextChangedListener {

            val value = try {
                editText.text.toString().replace(",", ".").replace(" ", "").toDouble()
            }catch (e: Exception){
                0.0
            }

            budgetItems[position].value = value
        }
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                budgetItems[position].name = spinnerItems[p2]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

        return view
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
        budgetItems.add(BudgetItem(name = name))
        notifyDataSetChanged()
    }

    fun getValues(): MutableList<BudgetItem> { return budgetItems }
}