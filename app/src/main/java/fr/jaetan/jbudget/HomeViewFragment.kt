package fr.jaetan.jbudget

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import fr.jaetan.jbudget.home.HomeListItem
import fr.jaetan.jbudget.home.HomeMainAdapter
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
    private lateinit var listview: RecyclerView
    private lateinit var noBudgetText: LinearLayout
    private var headerView: View? = null
    private var adapter: HomeMainAdapter? = null

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

        adapter = HomeMainAdapter(updateView)
        noBudgetText = view.findViewById(R.id.home_no_budgets)
        listview = view.findViewById(R.id.listview_home_budgets)
        listview.adapter = adapter
        listview.layoutManager = LinearLayoutManager(this.context)
        //listview.addHeaderView(topDivider)

        updateView()
        ViewCompat.setNestedScrollingEnabled(listview, true)

        //TODO: Events
        view.findViewById<Toolbar>(R.id.top_app_bar_home).setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.go_to_settings -> {
                    view.findNavController().navigate(R.id.action_homeViewFragment_to_settingsViewFragment)
                    true
                }
                else -> false
            }
        }
        addBtn.setOnClickListener {
            val title = "${LocalDate.now().month.name} ${LocalDate.now().year}"
            Database.instance.put(Budget(title = title))
            Toast.makeText(this.context, title, Toast.LENGTH_LONG).show()
            updateView()
        }

        listview.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            var oldState = 0
            var oldScroll = 0
            var extend = true

            override fun onScrollStateChanged(recyclerView: RecyclerView, actualState: Int) {
                if (oldState < actualState && !extend) {
                    addBtn.shrink()
                }
                if (oldState > actualState && extend) {
                    addBtn.extend()
                }
                oldState = actualState
            }

            override fun onScrolled(recyclerView: RecyclerView, p1: Int, actualScroll: Int) {
                if (oldScroll < actualScroll + 2) {
                    extend = false
                }
                if (oldScroll + 2 > actualScroll) {
                    extend = true
                }
            }

        })

        return view
    }

    private var updateView = {
        val count = Database.instance.budgets.count()
        listview.visibility = View.INVISIBLE
        noBudgetText.visibility = View.VISIBLE

        if(count > 0 && adapter != null){
            adapter?.update()
            //listview.removeHeaderView(headerView)
            //headerView = adapter?.getView(-1, null, listview)
            //listview.addHeaderView(headerView)
            listview.visibility = View.VISIBLE
            noBudgetText.visibility = View.INVISIBLE
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