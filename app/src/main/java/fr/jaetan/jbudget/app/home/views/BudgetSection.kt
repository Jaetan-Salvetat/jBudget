package fr.jaetan.jbudget.app.home.views

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.jaetan.jbudget.R
import fr.jaetan.jbudget.app.home.HomeViewModel

@Composable
fun BudgetSection(viewModel: HomeViewModel) {
    for(budget in viewModel.budgetList) {
        if(budget.isClosed.value) {
            Column {
                Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(text = budget.title, fontSize = 25.sp, textAlign = TextAlign.Center, modifier = Modifier.weight(1F))
                    Text(text = "Date Random", textAlign = TextAlign.Center, modifier = Modifier.weight(1F))
                }
                Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(text = stringResource(R.string.home_budget_closed), textAlign = TextAlign.Center, modifier = Modifier.weight(1F))
                    IconButton(onClick = { budget.isClosed.value = !budget.isClosed.value }) {
                        Icon(imageVector = Icons.Filled.ExpandMore, contentDescription = stringResource(R.string.home_budget_develop_content))
                    }
                }
            }
        } else {
            Column {
                Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(text = budget.title, fontSize = 25.sp, textAlign = TextAlign.Center, modifier = Modifier.weight(1F))
                    Text(text = "Date Random", textAlign = TextAlign.Center, modifier = Modifier.weight(1F))
                }
                Spacer(modifier = Modifier.height(40.dp))
                Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(text = stringResource(R.string.home_budget_closed), textAlign = TextAlign.Center,  modifier = Modifier.weight(2F))
                    IconButton(onClick = { budget.isClosed.value = !budget.isClosed.value }) {
                        Icon(imageVector = Icons.Filled.ExpandMore, contentDescription = stringResource(R.string.home_budget_develop_content))
                    }
                }
            }
        }
    }
}