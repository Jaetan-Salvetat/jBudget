package fr.jaetan.jbudget.app.home.views

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeContent(padding: PaddingValues) {
    Column(
        modifier = Modifier
            .padding(padding)
            .padding(horizontal = 20.dp)
            .fillMaxSize(),
    ) {
        HintSection()
    }
}