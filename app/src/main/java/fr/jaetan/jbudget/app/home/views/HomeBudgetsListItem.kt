package fr.jaetan.jbudget.app.home.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import fr.jaetan.jbudget.app.home.HomeViewModel
import fr.jaetan.jbudget.core.models.Budget
import fr.jaetan.jbudget.core.services.extentions.toText

@Composable
fun HomeBudgetsListItem(budget: Budget, viewModel: HomeViewModel) {
    val isExpanded = budget == viewModel.selectedBudget

    Column(
        Modifier
            .fillMaxWidth()
            .clickable { viewModel.toggleSelectedBudget(budget) }) {
        HomeBudgetHeader(isExpanded, budget)
        AnimatedVisibility(isExpanded) {
            HomeBudgetContent(budget)
        }
    }
}

@Composable
private fun HomeBudgetHeader(isExpanded: Boolean, budget: Budget) {
    val arrowRotation by animateFloatAsState(if (!isExpanded) 0f else 180f)
    var dates = ""

    budget.startDate?.let { dates += it.toText() }
    budget.endDate?.let { dates += "- ${it.toText()}" }

    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp, horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(budget.name, style = MaterialTheme.typography.titleLarge)
        Text(
            "(${dates})",
            modifier = Modifier
                .weight(1f)
                .padding(start = 5.dp),
            style = MaterialTheme.typography.labelLarge.copy(fontStyle = FontStyle.Italic, color = MaterialTheme.colorScheme.outline),
            overflow = TextOverflow.Ellipsis, maxLines = 1
        )
        Icon(
            imageVector = Icons.Filled.KeyboardArrowDown,
            contentDescription = null,
            modifier = Modifier.rotate(arrowRotation)
        )
    }
}

@Composable
private fun HomeBudgetContent(budget: Budget) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp), horizontalArrangement = Arrangement.Center) {
        Text(budget.name)
    }
}