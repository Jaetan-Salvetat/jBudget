package fr.jaetan.jbudget

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import fr.jaetan.jbudget.models.Budget
import fr.jaetan.jbudget.models.BudgetTitle
import fr.jaetan.jbudget.services.Database
import fr.jaetan.jbudget.settings.SettingsMainAdapter
import java.time.LocalDate

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SettingsViewFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SettingsViewFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @SuppressLint("SetTextI18n", "InflateParams")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //TODO: Init
        val view = inflater.inflate(R.layout.fragment_settings_view, container, false)
        val listView = view.findViewById<RecyclerView>(R.id.listview_settings_budget_title)
        val adapter = SettingsMainAdapter()

        ViewCompat.setNestedScrollingEnabled(listView, true)
        listView.apply {
            this.adapter = adapter
            this.layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        }
        view.findViewById<LinearLayout>(R.id.footer).bringToFront()

        //TODO: Events
        view.findViewById<MaterialToolbar>(R.id.top_app_bar_settings).setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        view.findViewById<Button>(R.id.add_budget_type_btn).setOnClickListener {
            val textEdit = view.findViewById<EditText>(R.id.name_budget_title_textedit)
            val budgetName: String = textEdit.text.toString()
            if(budgetName.length > 2){
                Database.instance.put(BudgetTitle(name = budgetName))
                textEdit.setText("")
                adapter.update()
            }
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
         * @return A new instance of fragment SettingsViewFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SettingsViewFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}