package fr.jaetan.jbudget.app.home.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import fr.jaetan.jbudget.app.home.HomeViewModel
import fr.jaetan.jbudget.core.models.Budget
import fr.jaetan.jbudget.core.services.extentions.toText
import fr.jaetan.jbudget.R

@Composable
fun HomeBudgetsListItem(budget: Budget, viewModel: HomeViewModel) {
    val isExpanded = budget == viewModel.selectedBudget
    val containerShape by animateDpAsState(targetValue =  if (isExpanded) 10.dp else 0.dp)
    val containerBackground by animateColorAsState(
        targetValue =  if (isExpanded) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
    )

    Box(Modifier.padding(containerShape)) {
        Column(
            Modifier
                .clip(RoundedCornerShape(containerShape))
                .clickable { }
                .background(containerBackground, RoundedCornerShape(containerShape))
        ) {
            HomeBudgetHeader(isExpanded, budget, viewModel)
            AnimatedVisibility(isExpanded) {
                HomeBudgetContent()
            }
        }
    }
}

@Composable
private fun HomeBudgetHeader(isExpanded: Boolean, budget: Budget, viewModel: HomeViewModel) {
    val arrowRotation by animateFloatAsState(if (!isExpanded) 0f else 180f)
    var dates = ""

    budget.startDate?.let { dates += "(${it.toText()}" }
    budget.endDate.let {
        dates += when {
            it == null && budget.startDate != null -> " - ${stringResource(R.string.actually)})"
            it == null -> "(∞)"
            else -> " - ${it.toText()})"
        }
    }

    Column(Modifier.padding(vertical = 20.dp, horizontal = 20.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(budget.name, style = MaterialTheme.typography.titleLarge)
            Text(
                if (isExpanded) "" else dates,
                modifier = Modifier
                    .padding(start = 5.dp)
                    .weight(1f),
                style = MaterialTheme.typography.labelLarge.copy(fontStyle = FontStyle.Italic, color = MaterialTheme.colorScheme.outline),
                overflow = TextOverflow.Ellipsis, maxLines = 1
            )
            IconButton(onClick = { viewModel.toggleSelectedBudget(budget) }){
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowDown,
                    contentDescription = null,
                    modifier = Modifier.rotate(arrowRotation)
                )
            }
        }
        if (isExpanded) {
            Text(
                dates.replace("(", "").replace(")", ""),
                style = MaterialTheme.typography.labelLarge.copy(fontStyle = FontStyle.Italic, color = MaterialTheme.colorScheme.outline),
                overflow = TextOverflow.Ellipsis, maxLines = 1
            )
        }
    }
}

@Composable
private fun HomeBudgetContent() {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp), horizontalArrangement = Arrangement.Center) {

    }
}