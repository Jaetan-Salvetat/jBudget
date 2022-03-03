package fr.jaetan.jbudget

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.TypedValue
import android.view.*
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import fr.jaetan.jbudget.home.HomeListItem
import fr.jaetan.jbudget.models.Budget
import fr.jaetan.jbudget.services.Database
import java.time.LocalDate

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
    private lateinit var noBudgetText: LinearLayout
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
        val addBtn = view.findViewById<ExtendedFloatingActionButton>(R.id.add_budget_btn)

        val topDivider = View(context)
        topDivider.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 40)

        adapter = this.context?.let { HomeListItem(it, updateView) }
        noBudgetText = view.findViewById(R.id.home_no_budgets)
        listview = view.findViewById(R.id.listview_home_budgets)
        listview.adapter = adapter
        listview.addHeaderView(topDivider)

        updateView()
        ViewCompat.setNestedScrollingEnabled(listview, true)

        //TODO: Events
        view.findViewById<Toolbar>(R.id.top_app_bar_home).setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.go_to_settings -> {
                    Navigation.findNavController(view).navigate(R.id.action_homeViewFragment_to_settingsViewFragment)
                    true
                }
                else -> false
            }
        }
        addBtn.setOnClickListener {
            val title = "${LocalDate.now().month.name} ${LocalDate.now().year}"
            Database.store.boxFor(Budget::class.java).put(Budget(title = title))
            Toast.makeText(this.context, title, Toast.LENGTH_LONG).show()
            updateView()
        }
        listview.setOnScrollListener(object: AbsListView.OnScrollListener{
            var oldState = 0
            override fun onScrollStateChanged(p0: AbsListView?, actualState: Int) {
            }

            override fun onScroll(p0: AbsListView?, actualState: Int, p2: Int, p3: Int) {
                if (oldState < actualState) {
                    addBtn.shrink()
                }
                if (oldState > actualState) {
                    addBtn.extend()
                }
                oldState = actualState
            }

        })

        return view
    }

    private var updateView = {
        val count = Database.store.boxFor(Budget::class.java).count()
        if(count > 0){
            adapter?.update()
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