package fr.jaetan.jbudget.ui.widgets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.himanshoe.charty.pie.PieChart
import com.himanshoe.charty.pie.config.PieConfig
import com.himanshoe.charty.pie.config.PieData
import fr.jaetan.jbudget.R
import fr.jaetan.jbudget.core.models.Budget
import fr.jaetan.jbudget.core.services.extentions.inc
import fr.jaetan.jbudget.core.services.extentions.roundTo2Decimal
import kotlin.math.roundToInt

@Composable
fun BudgetChart(budget: Budget, showNewCategory: Boolean = true) {
    var showPercentage by remember { mutableStateOf(false) }
    var showCategoryDialog by remember { mutableStateOf(false) }
    val categoryPercentages = remember { budget.getPercentages().sortedByDescending { it.percentage } }

    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        PieChart(
            modifier = Modifier.fillMaxWidth(.4f),
            config = PieConfig(
                isDonut = false,
                expandDonutOnClick = true
            ),
            pieData = categoryPercentages.map {
                PieData(it.percentage.toFloat(), it.color ?: MaterialTheme.colorScheme.secondaryContainer)
            },
        )

        Column {
            Row {
                Text(stringResource(R.string.payship_title))
                Text(
                    text = budget.payship.toString().plus("€"),
                    modifier = Modifier.padding(start = 5.dp)
                )
            }

            Row {
                Text(stringResource(R.string.total_amount))
                Text(
                    text = budget.transactionTotalAmount.roundTo2Decimal().plus("€"),
                    modifier = Modifier.padding(start = 5.dp),
                )
            }

            Row {
                Text(stringResource(R.string.total_remaining))
                Text(
                    text = (budget.payship - budget.transactionTotalAmount).roundTo2Decimal().plus("€"),
                    modifier = Modifier.padding(start = 5.dp),
                    color = if (budget.transactionTotalAmount > budget.payship) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.onBackground
                    }
                )
            }
        }
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
            items(categoryPercentages) { categoryPercentage ->
                Spacer(Modifier.width(10.dp))
                Row(
                    Modifier
                        .clip(RoundedCornerShape(7.dp))
                        .background(
                            categoryPercentage.color ?: MaterialTheme.colorScheme.secondaryContainer
                        )
                        .padding(15.dp, 10.dp)
                        .clickable { showPercentage++ },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = categoryPercentage.category?.name ?: stringResource(R.string.no_category_assigned),
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        fontWeight = FontWeight.Bold,
                    )
                    AnimatedVisibility(showPercentage) {
                        Text(
                            "(${categoryPercentage.percentage.roundToInt()}%)",
                            modifier = Modifier.padding(start = 5.dp),
                            fontStyle = FontStyle.Italic,
                            fontSize = 14.sp
                        )
                    }
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
        dismiss = { showCategoryDialog = false }
    )
}
