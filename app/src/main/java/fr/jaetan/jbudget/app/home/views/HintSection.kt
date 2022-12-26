package fr.jaetan.jbudget.app.home.views

import androidx.compose.runtime.Composable
import fr.jaetan.jbudget.app.home.HomeViewModel

@Composable
fun HintSection(viewModel: HomeViewModel) {
    viewModel.hintItems[viewModel.currentHint].GetView(viewModel)
}