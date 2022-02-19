package fr.jaetan.jbudget

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.Navigation
import fr.jaetan.jbudget.adapters.HomeListItem
import fr.jaetan.jbudget.models.Budget
import fr.jaetan.jbudget.services.Database

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeViewFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeViewFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var listview: ListView
    private lateinit var noBudgetText: ConstraintLayout
    private var headerView: View? = null
    private var adapter: HomeListItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @SuppressLint("InflateParams")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //TODO: Init
        val view = inflater.inflate(R.layout.fragment_home_view, container, false)
        val topAppBar = inflater.inflate(R.layout.home_top_app_bar, container, false)

        adapter = this.context?.let { HomeListItem(it, updateView) }
        noBudgetText = view.findViewById(R.id.home_no_budgets)
        listview = view.findViewById(R.id.listview_home_budgets)
        listview.addHeaderView(topAppBar)
        listview.adapter = adapter

        updateView(Database.store.boxFor(Budget::class.java).count())

        //TODO: Events
        topAppBar.findViewById<Toolbar>(R.id.top_app_bar_home).setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.go_to_settings -> {
                    Navigation.findNavController(view).navigate(R.id.action_homeViewFragment_to_settingsViewFragment)
                    true
                }
                else -> false
            }
        }

        return view
    }

    private var updateView = { count: Long ->
        if(count > 0){
            if (adapter != null) {
                listview.removeHeaderView(headerView)
                headerView = adapter?.getView(-1, null, listview)
                listview.addHeaderView(headerView)
            }
            listview.visibility = View.VISIBLE
            noBudgetText.visibility = View.INVISIBLE
        }else{
            listview.visibility = View.INVISIBLE
            noBudgetText.visibility = View.VISIBLE
        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeViewFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeViewFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}