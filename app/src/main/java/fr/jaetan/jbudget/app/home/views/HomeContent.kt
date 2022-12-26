package fr.jaetan.jbudget.app.home.views

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.jaetan.jbudget.app.home.HomeViewModel

@Composable
fun HomeContent(padding: PaddingValues, viewModel: HomeViewModel) {
    Column(
        modifier = Modifier
            .padding(padding)
            .padding(horizontal = 20.dp)
            .fillMaxSize(),
    ) {
        HintSection(viewModel)
        Spacer(modifier = Modifier.height(20.dp))
    }
}