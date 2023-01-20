package fr.jaetan.jbudget.ui.widgets

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.himanshoe.charty.pie.PieChart
import com.himanshoe.charty.pie.config.PieConfig
import com.himanshoe.charty.pie.config.PieData
import fr.jaetan.jbudget.core.models.Transaction


@Composable
fun BudgetChart(transactions: List<Transaction>) {
    var percentages = getPercentages(transactions)

    PieChart(
        modifier = Modifier,
        config = PieConfig(isDonut = true),
        pieData = listOf(PieData(20F, Color.Blue), PieData(12.5F, Color.Red), PieData(10.5F, Color.Green), PieData(7F, Color.Yellow), PieData(50F, Color.Magenta))
    )
}


private data class CategoryPercentage(
    var categoryId: String?,
    var values: Double = 0.0,
    var percentage: Double = 0.0
) {
    fun addValue(value: Double) {
        values += value
    }
    fun getPercentageOf(total: Double) :Double {
        return (total / 100) * values
    }
}


private fun getPercentages(transactions: List<Transaction>) :List<CategoryPercentage> {
    val percentages = mutableListOf<CategoryPercentage>()
    val groupedTransactions = transactions.groupBy { it.categoryId }
    val categoryPercentages = mutableListOf<CategoryPercentage>()

    groupedTransactions.forEach { (key, values) ->
        val myCategoryPercentage = CategoryPercentage(key)
        values.forEach { transaction ->
            myCategoryPercentage.addValue(transaction.amount)
        }
        categoryPercentages.add(myCategoryPercentage)
    }

    return percentages
}