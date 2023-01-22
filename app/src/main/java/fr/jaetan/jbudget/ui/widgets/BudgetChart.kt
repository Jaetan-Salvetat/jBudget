package fr.jaetan.jbudget.ui.widgets

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.himanshoe.charty.pie.PieChart
import com.himanshoe.charty.pie.config.PieConfig
import com.himanshoe.charty.pie.config.PieData
import fr.jaetan.jbudget.core.models.Transaction
import kotlin.random.Random


@Composable
fun BudgetChart(transactions: List<Transaction>) {
    var showPercentage by remember { mutableStateOf(false) }
    val categoryPercentages = remember { getPercentages(transactions) }

    PieChart(
        modifier = Modifier.fillMaxWidth()
            .padding(50.dp),
        config = PieConfig(isDonut = true, expandDonutOnClick = true, textColor = MaterialTheme.colorScheme.onBackground),
        pieData = categoryPercentages.map {
            PieData(it.percentage.toFloat(), it.color)
        },
        onSectionClicked = { _, _ -> showPercentage = ! showPercentage }
    )
}


private data class CategoryPercentage(
    var categoryId: String?,
    var values: Double = 0.0,
    var percentage: Double = 0.0,
    var color: Color = Color(Random.nextInt(256), Random.nextInt(256), Random.nextInt(256))
)


private fun getPercentages(transactions: List<Transaction>) :List<CategoryPercentage> {
    if (transactions.isEmpty()) return listOf()

    val percentages = mutableListOf<CategoryPercentage>()
    val categoriesGrouped = transactions.groupBy { it.categoryId }
    val totalAmount = transactions.map{ it.amount }.reduce { acc, i -> acc + i}
    categoriesGrouped.mapValues { (categoryId, group) ->
        val categoryTotal = group.map{ it.amount }.reduce { acc, i -> acc + i}
        percentages.add(CategoryPercentage(
            categoryId = categoryId,
            values = categoryTotal,
            percentage = (totalAmount / 100) * categoryTotal
        ))
    }
    return percentages
}