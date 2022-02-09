package fr.jaetan.jbudget

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.widget.addTextChangedListener
import androidx.navigation.Navigation
import fr.jaetan.jbudget.adapters.ModalBudgetListItem
import fr.jaetan.jbudget.models.Budget
import fr.jaetan.jbudget.models.BudgetHistory
import fr.jaetan.jbudget.models.BudgetItem
import fr.jaetan.jbudget.services.Database
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ModalBudgetFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ModalBudgetFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var args: ModalBudgetFragmentArgs
    private lateinit var budget: Budget

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_modal_budget, container, false)
        val bumble = arguments
        val textEdit = view.findViewById<EditText>(R.id.modal_title_budget)
        val listView = view.findViewById<ListView>(R.id.listview_modal)
        val btnAddItem = LinearLayout(this.context)
        val textBtnAddItem = TextView(this.context)
        val adapter = this.context?.let { ModalBudgetListItem(it) }

        listView.adapter = adapter
        listView.addFooterView(btnAddItem)
        btnAddItem.addView(textBtnAddItem)
        btnAddItem.gravity = Gravity.CENTER
        btnAddItem.setPadding(0, 40, 0, 40)
        btnAddItem.setBackgroundResource(R.drawable.ripple_effect_btn_add_item_modal_budget)
        textBtnAddItem.textSize = 18F
        textBtnAddItem.text = "Ajouter une dépense"

        if(bumble != null) {
            args = ModalBudgetFragmentArgs.fromBundle(bumble)

            budget = Database.store.boxFor(Budget::class.java).all[args.budgetId]
            textEdit.setText(budget.title)
        }


        //TODO: Events
        view.findViewById<ImageButton>(R.id.back_to_home).setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_modalBudgetFragment_to_homeViewFragment)
        }
        view.findViewById<ImageButton>(R.id.go_to_history).setOnClickListener {
            val action = ModalBudgetFragmentDirections.actionModalBudgetFragmentToHistoryFragment(args.budgetId)
            Navigation.findNavController(view).navigate(action)
        }
        view.findViewById<ImageButton>(R.id.share_budget).setOnClickListener {
            var res: String = "Total dépensé: ${String.format("%.2f", budget.totalSpent)}€\n" +
                    "Total resntant: ${String.format("%.2f", budget.total - budget.totalSpent)}€\n\n"

            for(item in budget.items){
                res += "${item.name}: ${String.format("%.2f", item.value)}€\n"
            }

            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, res)
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }
        view.findViewById<Button>(R.id.save_update_budget_btn).setOnClickListener {
            val budgetItems = adapter?.getValues() ?: return@setOnClickListener
            val history: ArrayList<BudgetHistory> = arrayListOf()

            for(item in budgetItems){
                if(item.value <= 0.0) continue

                var containsItem = false
                var containsHistory = false

                for(j in budget.items.indices){
                    if(budget.items[j].name.lowercase() == item.name.lowercase()){
                        if (item.cashFlow){
                            budget.total += item.value
                        }else{
                            budget.items[j].value += item.value
                            budget.totalSpent += item.value
                        }
                        containsItem = true
                        break
                    }
                }

                if(!containsItem){
                    if (item.cashFlow){
                        budget.total += item.value
                    }else{
                        budget.totalSpent += item.value
                        budget.items.add(item)
                    }
                }

                for(i in history.indices.reversed()){
                    if(history[i].name.lowercase() == item.name.lowercase()){
                        history[i].value +=  item.value
                        containsHistory = true
                        break
                    }
                }
                if(!containsHistory){
                    history.add(BudgetHistory(value = item.value, name = item.name, cashFlow = item.cashFlow, date = LocalDate.now().format(
                        DateTimeFormatter.ofPattern("dd-MM-yyyy"))))
                }
            }

            budget.history.addAll(history)
            Database.store.boxFor(Budget::class.java).put(budget)
            Database.store.boxFor(BudgetItem::class.java).put(budget.items)
            Database.store.boxFor(BudgetHistory::class.java).put(budget.history)

            Navigation.findNavController(view).navigate(R.id.action_modalBudgetFragment_to_homeViewFragment)
        }
        btnAddItem.setOnClickListener {
            adapter?.update()
        }
        textEdit.addTextChangedListener {
            budget.title = it.toString()
        }



        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ModalBudgetFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ModalBudgetFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}