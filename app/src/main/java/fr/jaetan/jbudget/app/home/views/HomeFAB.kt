package fr.jaetan.jbudget.app.home.views

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import fr.jaetan.jbudget.R
import fr.jaetan.jbudget.app.home.HomeViewModel

@Composable
fun HomeFAB(viewModel: HomeViewModel) {
    Column() {
        Column {
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
}


@Composable
fun HomeFABItem() {

}