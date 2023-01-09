package fr.jaetan.jbudget.app.home.views

import androidx.annotation.StringRes
import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import fr.jaetan.jbudget.R
import fr.jaetan.jbudget.app.home.HomeViewModel

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun HomeFAB(modifier: Modifier, viewModel: HomeViewModel) {
    val fabIconRotation by animateFloatAsState(if (!viewModel.fabExpanded) 0f else 225f)
    val fabContainerColor by animateColorAsState(if (!viewModel.fabExpanded) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.errorContainer
    })
    
    Column(modifier, horizontalAlignment = Alignment.End) {
        AnimatedVisibility(
            viewModel.fabExpanded,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            HomeFabItem(
                Modifier.animateEnterExit(
                    enter = slideInVertically { it },
                    exit = slideOutVertically { it }
                ),
                viewModel
            )
        }
        FloatingActionButton(
            onClick = { viewModel.fabExpanded = !viewModel.fabExpanded },
            containerColor = fabContainerColor
        ) {
            Icon(
                Icons.Filled.Add,
                contentDescription = stringResource(R.string.home_fab_add_descriptor),
                modifier = Modifier.rotate(fabIconRotation)
            )
        }
    }
}


@Composable
private fun HomeFabItem(modifier: Modifier, viewModel: HomeViewModel) {
    Column(modifier, horizontalAlignment = Alignment.End) {
        val textShape = RoundedCornerShape(10.dp)

        for(item in viewModel.fabItems) {
            Row(
                Modifier.padding(bottom = 10.dp, end = 3.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    Modifier
                        .background(
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = .6f),
                            textShape
                        )
                        .clip(textShape)
                        .clickable { item.onClick() }

                ) {
                    Text(stringResource(item.text), modifier = Modifier.padding(vertical = 5.dp, horizontal = 10.dp))
                }

                Spacer(Modifier.width(10.dp))

                SmallFloatingActionButton(onClick = item.onClick) {
                    Icon(imageVector = item.icon, contentDescription = stringResource(item.descriptor))
                }
            }
        }
    }
}

class FabItem(@StringRes val text: Int, @StringRes val descriptor: Int, val onClick: () -> Unit, val icon: ImageVector)