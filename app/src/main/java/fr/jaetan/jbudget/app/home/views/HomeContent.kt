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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import fr.jaetan.jbudget.R
import fr.jaetan.jbudget.app.home.HomeViewModel
import fr.jaetan.jbudget.app.home.TipsItem
import kotlinx.coroutines.launch

@Composable
fun HomeContent(viewModel: HomeViewModel) {
    LazyColumn {
        item { TipsSection(viewModel) }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun TipsSection(viewModel: HomeViewModel) {
    val pagerState = rememberPagerState()

    HorizontalPager(viewModel.tips.size, state =  pagerState) {
        TipsItem(viewModel.tips[this.currentPage], pagerState)
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun TipsItem(tips: TipsItem, pageState: PagerState) {
    val coroutineScope = rememberCoroutineScope()
    val nextPage: () -> Unit = {
        if (pageState.currentPage + 1 < pageState.pageCount) {
            coroutineScope.launch {
                pageState.animateScrollToPage(pageState.currentPage + 1)
            }
        }

    }
    val previousPage: () -> Unit = {
        if (pageState.currentPage > 0) {
            coroutineScope.launch {
                pageState.animateScrollToPage(pageState.currentPage - 1)
            }
        }
    }

    Row(
        Modifier
            .fillMaxWidth()
            .padding(top = 15.dp, start = 20.dp, end = 20.dp)
            .background(MaterialTheme.colorScheme.tertiaryContainer, RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .clickable { tips.action() }
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(5.dp)
                .padding(start = 10.dp, bottom = 10.dp)) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    stringResource(R.string.tips_title),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = previousPage) {
                    Icon(Icons.Filled.ChevronLeft, contentDescription = stringResource(R.string.previous_item_descriptor))
                }
                Text(stringResource(R.string.tips_advance, pageState.currentPage + 1, pageState.pageCount))
                IconButton(onClick = nextPage) {
                    Icon(Icons.Filled.ChevronRight, contentDescription = stringResource(R.string.next_item_descriptor))
                }
            }

            Text(stringResource(tips.text), modifier = Modifier.padding(end = 30.dp))

            Text(
                stringResource(R.string.show_me_more),
                style = MaterialTheme.typography.labelMedium.copy(color = MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 15.dp),
                textAlign = TextAlign.End
            )
        }
    }
}