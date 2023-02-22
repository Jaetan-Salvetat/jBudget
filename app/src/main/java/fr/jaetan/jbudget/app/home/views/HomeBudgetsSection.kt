package fr.jaetan.jbudget.app.home.views

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import fr.jaetan.jbudget.R
import fr.jaetan.jbudget.app.home.HomeViewModel
import fr.jaetan.jbudget.core.models.Budget
import fr.jaetan.jbudget.core.services.extentions.toText
import fr.jaetan.jbudget.ui.widgets.BudgetChart
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun HomeBudgetsSection(viewModel: HomeViewModel) {
    LazyColumn(contentPadding = PaddingValues(bottom = 100.dp)) {
        item { HomeTipsSection(viewModel) }
        items(viewModel.currentBudgets) { budget ->
            HomeBudgetsListItem(
                budget,
                viewModel.selectedCurrentBudgets.find { it == budget } != null,
                viewModel
            )
        }
        if (viewModel.oldBudgets.isNotEmpty()) {
            item { Divider() }
            items(viewModel.oldBudgets) { HomeBudgetsListItem(it, viewModel.selectedOldBudget == it, viewModel) }
        }
        item { Spacer(Modifier.height(100.dp)) }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun HomeBudgetsListItem(budget: Budget, isExpanded: Boolean, viewModel: HomeViewModel) {
    val coroutineScope = rememberCoroutineScope()
    val sizePx = with(LocalDensity.current) { LocalConfiguration.current.screenWidthDp.dp.toPx() }
    val anchors = mapOf(0f to 0, sizePx to 1)
    val swipeState = rememberSwipeableState(0)
    val isRemovable = swipeState.offset.value > (sizePx *  0.3f)
    val removeIconScaleAnimation by animateFloatAsState(targetValue = if (isRemovable) 1.2f else 1f )
    val containerShape by animateDpAsState(targetValue =  if (isExpanded) 10.dp else 0.dp)
    val containerPadding by animateDpAsState(targetValue =  if (isExpanded) 10.dp else 0.dp)
    val containerBackground by animateColorAsState(
        targetValue = if (isExpanded) MaterialTheme.colorScheme.secondaryContainer.copy(alpha = .4f) else  Color.Transparent
    )

    if (isRemovable && swipeState.isAnimationRunning) {
        LaunchedEffect(Unit) {
            coroutineScope.launch {
                viewModel.budgetToRemove = budget
                swipeState.animateTo(0)
            }
        }
    }

    Box(
        Modifier
            .swipeable(
                state = swipeState,
                anchors = anchors,
                thresholds = { _, _ ->
                    androidx.compose.material.FractionalThreshold(0.3f)
                },
                orientation = Orientation.Horizontal
            )
    ) {
        Icon(
            imageVector = Icons.Default.DeleteSweep,
            contentDescription = stringResource(R.string.swipe_to_remove_budget_descriptor),
            modifier = Modifier
                .padding(start = 30.dp, top = 20.dp)
                .scale(removeIconScaleAnimation),
            tint = MaterialTheme.colorScheme.errorContainer
        )
        Box(
            Modifier
                .offset { IntOffset(swipeState.offset.value.roundToInt(), 0) }
                .padding(top = containerPadding, end = containerPadding, start = containerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column(
                Modifier
                    .clip(RoundedCornerShape(containerShape))
                    .clickable { viewModel.toggleSelectedBudget(budget) }
                    .background(containerBackground, RoundedCornerShape(containerShape))
                    .animateContentSize()
            ) {
                HomeBudgetHeader(isExpanded, budget)
                if (isExpanded) {
                    HomeBudgetContent(budget, viewModel)
                }
            }
        }
    }
}

@Composable
private fun HomeBudgetHeader(isExpanded: Boolean, budget: Budget) {
    val arrowRotation by animateFloatAsState(if (!isExpanded) 0f else 180f)
    var dates = "(${budget.startDate.toText()}"

    dates += when (budget.endDate) {
        null -> " - ${stringResource(R.string.actually)})"
        else -> " - ${budget.endDate!!.toText()})"
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
            Icon(
                imageVector = Icons.Filled.KeyboardArrowDown,
                contentDescription = null,
                modifier = Modifier.rotate(arrowRotation)
            )
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
private fun HomeBudgetContent(budget: Budget, viewModel: HomeViewModel) {
    Column(Modifier.fillMaxWidth()) {

        if (budget.transactions.isEmpty()) {
            Text(
                stringResource(R.string.empty_budget),
                style = MaterialTheme.typography.bodySmall.copy(fontStyle = FontStyle.Italic),
                modifier = Modifier.padding(start = 20.dp)
            )
        } else {
            BudgetChart(budget, false)
        }
        
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomEnd = 10.dp, bottomStart = 10.dp))
                .clickable { viewModel.navigateToBudgetScreen(budget.id) }
                .padding(vertical = 15.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.more_details),
                style = MaterialTheme.typography.labelLarge.copy(color = MaterialTheme.colorScheme.primary)
            )
        }
    }
}