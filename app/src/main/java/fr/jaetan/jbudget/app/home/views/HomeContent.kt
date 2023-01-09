package fr.jaetan.jbudget.app.home.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import fr.jaetan.jbudget.R

@Composable
fun HomeContent() {
    LazyColumn {
        item { TipsSection() }
    }
}

@Composable
private fun TipsSection() {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(top = 15.dp, start = 20.dp, end = 20.dp)
            .background(MaterialTheme.colorScheme.tertiaryContainer, RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .clickable {  }
    ) {
        Column(Modifier.fillMaxWidth().padding(5.dp).padding(start = 10.dp, bottom = 10.dp)) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    stringResource(R.string.tips_title),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(Icons.Filled.ChevronLeft, contentDescription = stringResource(R.string.previous_item_descriptor))
                }
                Text(stringResource(R.string.tips_advance, 1, 4))
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(Icons.Filled.ChevronRight, contentDescription = stringResource(R.string.next_item_descriptor))
                }
            }
            
            Text("Vous pouvez réer un budget sans date de fin", modifier = Modifier.padding(end = 30.dp))

            Text(
                stringResource(R.string.show_me_more),
                style = MaterialTheme.typography.labelMedium.copy(color = MaterialTheme.colorScheme.primary),
                modifier = Modifier.fillMaxWidth().padding(end = 15.dp),
                textAlign = TextAlign.End
            )
        }
    }
}