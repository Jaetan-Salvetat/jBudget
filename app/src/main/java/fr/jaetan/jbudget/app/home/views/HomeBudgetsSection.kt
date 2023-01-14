package fr.jaetan.jbudget.app.home.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
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
import fr.jaetan.jbudget.R
import fr.jaetan.jbudget.app.home.HomeViewModel
import fr.jaetan.jbudget.core.models.Budget
import fr.jaetan.jbudget.core.services.extentions.toText

@Composable
fun HomeBudgetsSection(viewModel: HomeViewModel) {
    LazyColumn(contentPadding = PaddingValues(bottom = 100.dp)) {
        item { HomeTipsSection(viewModel) }
        item { Divider() }
        items(viewModel.currentBudgets) { budget ->
            HomeBudgetsListItem(
                budget,
                viewModel.selectedCurrentBudgets.find { it == budget } != null,
                viewModel
            )
        }
        item { Divider() }
        items(viewModel.oldBudgets) { HomeBudgetsListItem(it, viewModel.selectedOldBudget == it, viewModel) }
        item { Spacer(Modifier.height(100.dp)) }
    }
}

@Composable
private fun HomeBudgetsListItem(budget: Budget, isExpanded: Boolean, viewModel: HomeViewModel) {
    val containerShape by animateDpAsState(targetValue =  if (isExpanded) 10.dp else 0.dp)
    val containerPadding by animateDpAsState(targetValue =  if (isExpanded) 10.dp else 0.dp)
    val containerBackground by animateColorAsState(
        targetValue = if (isExpanded) MaterialTheme.colorScheme.secondaryContainer.copy(alpha = .4f) else  Color.Transparent
    )

    Box(Modifier.padding(containerPadding)) {
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