package fr.jaetan.jbudget.app.home.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import fr.jaetan.jbudget.R
import fr.jaetan.jbudget.app.home.HomeViewModel

@Composable
fun HomeFAB(viewModel: HomeViewModel) {
    Column(verticalArrangement = Arrangement.SpaceEvenly, horizontalAlignment = Alignment.End) {
        Column(horizontalAlignment = Alignment.End) {
            HomeFabContainer(viewModel)
        }
        FloatingActionButton(
            onClick = {
                viewModel.fabExpanded = !viewModel.fabExpanded
            }
        ) {
            Icon(Icons.Filled.Add, contentDescription = stringResource(R.string.home_fab_add_descriptor))
        }
    }
}


@Composable
fun HomeFabContainer(viewModel: HomeViewModel) {
    if(viewModel.fabExpanded)
    {
        for(item in viewModel.fabItems) {
            Row(horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically) {
                Text(stringResource(item.text))
                FilledIconButton(onClick = item.onClick) {
                    Icon(imageVector = item.icon, contentDescription = stringResource(item.descriptor))
                }
            }
        }
    }
}