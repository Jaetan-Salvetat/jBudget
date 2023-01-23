package fr.jaetan.jbudget.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.himanshoe.charty.pie.PieChart
import com.himanshoe.charty.pie.config.PieConfig
import com.himanshoe.charty.pie.config.PieData
import fr.jaetan.jbudget.R
import fr.jaetan.jbudget.core.models.Category
import fr.jaetan.jbudget.core.models.Transaction
import fr.jaetan.jbudget.core.services.JBudget
import kotlin.random.Random


@Composable
fun BudgetChart(budgetId: String, transactions: List<Transaction>, categories: List<Category>, showNewCategory: Boolean = true) {
    var showPercentage by remember { mutableStateOf(false) }
    var showCategoryDialog by remember { mutableStateOf(false) }
    val categoryPercentages = remember { getPercentages(transactions) }

    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        PieChart(
            modifier = Modifier.fillMaxWidth(.5f),
            config = PieConfig(
                isDonut = true,
                expandDonutOnClick = true,
                textColor = MaterialTheme.colorScheme.onBackground
            ),
            pieData = categoryPercentages.map {
                PieData(it.percentage.toFloat(), it.color)
            },
            onSectionClicked = { _, _ -> showPercentage = !showPercentage }
        )
    }

    Box(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
    ) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            items(categories) { category ->
                Spacer(Modifier.width(10.dp))
                Box(
                    Modifier
                        .clip(RoundedCornerShape(7.dp))
                        .background(
                            category.color ?: MaterialTheme.colorScheme.secondaryContainer
                        )
                        .clickable { }
                ) {
                    Text(
                        text = category.name,
                        modifier = Modifier.padding(15.dp, 10.dp),
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
            item {
                Spacer(Modifier.width(45.dp))
            }
        }
        if (showNewCategory) {
            Box(
                Modifier
                    .background(
                        brush = Brush.horizontalGradient(
                            0.0f to Color.Transparent,
                            0.2f to MaterialTheme.colorScheme.background.copy(0.5f),
                            0.6f to MaterialTheme.colorScheme.background.copy(1f)
                        )
                    )
                    .align(Alignment.CenterEnd)
                    .fillMaxHeight()
            ) {
                IconButton(onClick = { showCategoryDialog = true }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(R.string.expand_section_descriptor)
                    )
                }
            }
        }
    }

    NewCategoryDialog(
        isVisible = showCategoryDialog,
        budgetId = budgetId,
        dismiss = { showCategoryDialog = false },
        onSave = { JBudget.state.budgets.find { b -> b.id == budgetId }?.categories?.add(it) }
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