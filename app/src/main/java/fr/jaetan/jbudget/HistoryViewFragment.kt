package fr.jaetan.jbudget

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ListView
import androidx.navigation.Navigation
import com.google.android.material.appbar.MaterialToolbar
import fr.jaetan.jbudget.history.HistoryListItem
import fr.jaetan.jbudget.models.Budget
import fr.jaetan.jbudget.models.BudgetHistory
import fr.jaetan.jbudget.models.BudgetItem
import fr.jaetan.jbudget.models.SortType
import fr.jaetan.jbudget.services.Database
import fr.jaetan.jbudget.history.HistoryBottomSheet
import io.objectbox.relation.ToMany
import kotlin.properties.Delegates

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HistoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HistoryFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var args: HistoryFragmentArgs
    private lateinit var historyItems: ToMany<BudgetHistory>
    private lateinit var budgetItems: ToMany<BudgetItem>
    private lateinit var historyActualItems: ToMany<BudgetHistory>
    private lateinit var bottomSheet: HistoryBottomSheet
    //private var bottomSheet: HistoryBottomSheet? = null
    private var adapter: HistoryListItem? = null
    private var budgetId by Delegates.notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //TODO: Init
        val view = inflater.inflate(R.layout.fragment_history_view, container, false)
        val appBar = view.findViewById<MaterialToolbar>(R.id.top_app_bar_history)
        val bumble = arguments
        val listview = view.findViewById<ListView>(R.id.listview_history)

        if(bumble != null){
            args = HistoryFragmentArgs.fromBundle(bumble)
            historyItems = Database.store.boxFor(Budget::class.java).all[args.budgetId].history
            historyActualItems = Database.store.boxFor(Budget::class.java).all[args.budgetId].history
            budgetItems = Database.store.boxFor(Budget::class.java).all[args.budgetId].items
            budgetId = args.budgetId
            adapter = this.context?.let { HistoryListItem(it, historyItems, budgetId) }
            listview.adapter = adapter
        }



        appBar.setOnMenuItemClickListener{ menuItem ->
            when(menuItem.itemId){
                R.id.sort -> {
                    bottomSheet = context?.let { HistoryBottomSheet(it, budgetId, generateMapOfSort(), sort) }!!
                    bottomSheet.show(parentFragmentManager, "TAG")
                    true
                }
                else -> false
            }
        }

        //TODO: Events
        appBar.setNavigationOnClickListener {
            val action = HistoryFragmentDirections.actionHistoryFragmentToModalBudgetFragment(budgetId)
            Navigation.findNavController(view).navigate(action)
        }
        

        return view
    }

    private fun generateMapOfSort(): ArrayList<Map<String, SortType>>{
        val res: ArrayList<Map<String, SortType>> = arrayListOf()
        res += mapOf("Par défaut" to SortType.Default)
        res += mapOf("Par item sélectionné" to SortType.Done)
        res += mapOf("Par item pas sélectionné" to SortType.NotDone)
        res += mapOf("Rentrée d'argent" to SortType.Name)

        for(item in budgetItems){
            res += mapOf(item.name to SortType.Name)
        }

        return res
    }

    private val sort: (Collection<BudgetHistory>) -> Unit = {items ->
        adapter?.update(items)
        bottomSheet?.dismiss()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HistoryFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HistoryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}