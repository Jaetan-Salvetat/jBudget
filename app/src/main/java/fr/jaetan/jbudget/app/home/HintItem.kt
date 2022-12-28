package fr.jaetan.jbudget.app.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import fr.jaetan.jbudget.R

class HintItem(private val content: String, val onClick: () -> Unit /* Should need Navigation */) {
    @Composable
    fun GetView(viewModel: HomeViewModel) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.home_hint_did_you_know),
                    fontSize = 25.sp)
                Text( (viewModel.currentHint + 1).toString() + " sur " + viewModel.hintItems.count().toString())
                IconButton(onClick = { viewModel.nextHint() }) {
                    Icon(
                        imageVector =  Icons.Filled.NavigateNext,
                        contentDescription = stringResource(R.string.navigate_next)
                    )
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = content,
                    Modifier.weight(2F))
                Button(
                    onClick = { onClick(/* Should ask Navigation */) },
                    Modifier.weight(1F)) {
                    Text(text = stringResource(R.string.home_hint_show_me))
                }
            }
        }
    }
}