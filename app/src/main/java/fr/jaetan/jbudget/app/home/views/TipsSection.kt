package fr.jaetan.jbudget.app.home.views

import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
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

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TipsSection(viewModel: HomeViewModel) {
    val pagerState = rememberPagerState()

    HorizontalPager(viewModel.tips.size, state =  pagerState) {
        val pageScope = this

        Column {
            TipsItem(viewModel, viewModel.tips[pageScope.currentPage], pagerState)
            Spacer(Modifier.height(20.dp))
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun TipsItem(viewModel: HomeViewModel, tips: TipsItem, pageState: PagerState) {
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

    Box(Modifier.fillMaxWidth()) {
        TipsItemContent(viewModel, tips, pageState, nextPage, previousPage)
        TipsItemActionContent(viewModel, pageState)
    }
}

@OptIn(ExperimentalPagerApi::class, ExperimentalFoundationApi::class)
@Composable
private fun TipsItemContent(
    viewModel: HomeViewModel,
    tips: TipsItem,
    pageState: PagerState,
    nextPage: () -> Unit,
    previousPage: () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(top = 15.dp, start = 20.dp, end = 20.dp)
            .height(135.dp)
            .background(MaterialTheme.colorScheme.tertiaryContainer, RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .combinedClickable(
                onClick = { tips.action() },
                onLongClick = { viewModel.showDeleteTipsButton = true }
            )
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

            Spacer(Modifier.weight(1f))
            Text(stringResource(tips.text), modifier = Modifier.padding(end = 30.dp))
            Spacer(Modifier.weight(1f))

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

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun TipsItemActionContent(viewModel: HomeViewModel, pageState: PagerState) {
    val coroutineScope = rememberCoroutineScope()

    if (viewModel.showDeleteTipsButton) {
        Row(
            Modifier
                .fillMaxWidth()
                .height(150.dp)
                .padding(top = 15.dp, start = 20.dp, end = 20.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(
                    MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = .8f),
                    RoundedCornerShape(10.dp)
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
        ) {
            TipsIconButton(
                Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(topStart = 10.dp, bottomStart = 10.dp)),
                Icons.Rounded.Delete, textRes = R.string.hide
            ) { coroutineScope.launch { viewModel.removeTips(pageState) } }
            TipsIconButton(
                Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(topEnd = 10.dp, bottomEnd = 10.dp)),
                Icons.Rounded.Close,
                textRes = R.string.cancel
            ) { viewModel.showDeleteTipsButton = false }
        }
    }
}

@Composable
private fun TipsIconButton(modifier: Modifier, icon: ImageVector, @StringRes textRes: Int?, onClick: () -> Unit) {
    Column(
        modifier
            .fillMaxHeight()
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = textRes?.let { stringResource(it) },
            Modifier
                .size(30.dp)
        )
        Spacer(Modifier.height(10.dp))
        textRes?.let {
            Text(stringResource(it))
        }
    }
}