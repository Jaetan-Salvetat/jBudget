package fr.jaetan.jbudget.app.transaction.view

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Euro
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import fr.jaetan.jbudget.R
import fr.jaetan.jbudget.app.transaction.TransactionViewModel
import fr.jaetan.jbudget.core.models.Category
import fr.jaetan.jbudget.core.models.State
import fr.jaetan.jbudget.core.services.JBudget

@Composable
fun TransactionContent(padding: PaddingValues, viewModel: TransactionViewModel) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(padding)
            .background(MaterialTheme.colorScheme.background, RoundedCornerShape(20.dp))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(20.dp)
        ) {
            if (!viewModel.isInUpdateMode) TransactionSelectBudget(viewModel)
            TransactionSelectCategory(viewModel)
            TransactionAmountSection(viewModel)
            TransactionBottomButtons(viewModel)
        }
    }
}

@Composable
private fun TransactionSelectBudget(viewModel: TransactionViewModel) {
    val arrowRotation by animateFloatAsState(if (!viewModel.showBudgetDropDown) 0f else 180f)

    Column(
        Modifier
            .height(70.dp)
            .fillMaxWidth()
            .clickable { viewModel.showBudgetDropDown = true }) {
        Row(
            Modifier
                .height(70.dp)
                .padding(horizontal = 15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                stringResource(R.string.budget),
                modifier = Modifier.weight(1f)
            )

            Column(
                Modifier.clickable { viewModel.showBudgetDropDown = true }
            ){
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        viewModel.currentBudget?.name ?: "Vide",
                        modifier = Modifier.padding(10.dp),
                        fontWeight = FontWeight.Bold,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.width(3.dp))
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowDown,
                        contentDescription = null,
                        modifier = Modifier.rotate(arrowRotation)
                    )
                }
                DropdownMenu(
                    expanded = viewModel.showBudgetDropDown,
                    onDismissRequest = { viewModel.showBudgetDropDown = false }
                ) {
                    viewModel.budgets.forEach {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    it.name,
                                    fontWeight = if (it == viewModel.currentBudget) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            onClick = { viewModel.changeCurrentBudget(it) }
                        )
                    }

                    DropdownMenuItem(
                        text = {
                            Row {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = null
                                )
                                Spacer(Modifier.width(10.dp))
                                Text(
                                    stringResource(R.string.new_male),
                                    fontWeight = FontWeight.Normal
                                )
                            }
                        },
                        onClick = { viewModel.showBudgetDialog = true }
                    )
                }
            }
        }
    }
}

@Composable
private fun TransactionSelectCategory(viewModel: TransactionViewModel) {
    val context = LocalContext.current
    val arrowRotation by animateFloatAsState(if (!viewModel.showCategoryDropDown) 0f else 180f)

    Column(
        Modifier
            .height(70.dp)
            .fillMaxWidth()
            .clickable { viewModel.showCategoryDropDown = true }) {
        Row(
            Modifier
                .height(70.dp)
                .padding(horizontal = 15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                stringResource(R.string.category),
                modifier = Modifier.weight(1f)
            )

            Column(
                Modifier.clickable { viewModel.showCategoryDropDown = true }
            ){
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        viewModel.currentCategory?.name ?: "Vide",
                        modifier = Modifier.padding(10.dp),
                        fontWeight = FontWeight.Bold,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.width(3.dp))
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowDown,
                        contentDescription = null,
                        modifier = Modifier.rotate(arrowRotation)
                    )
                }
                DropdownMenu(
                    expanded = viewModel.showCategoryDropDown,
                    onDismissRequest = { viewModel.showCategoryDropDown = false }
                ) {
                    DropdownMenuItem(
                        text = {
                            Text(
                                stringResource(R.string.no_category),
                                fontWeight = FontWeight.Normal
                            )
                        },
                        onClick = {
                            viewModel.currentCategory = null
                            viewModel.showCategoryDropDown = false
                        }
                    )

                    JBudget.state.categories.forEach {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    it.name,
                                    fontWeight = if (it == viewModel.currentCategory) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            onClick = {
                                viewModel.currentCategory = it
                                viewModel.showCategoryDropDown = false
                            }
                        )
                    }

                    DropdownMenuItem(
                        text = {
                            Row {
                                Icon(
                                    imageVector = Icons.Default.Paid,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Spacer(Modifier.width(10.dp))
                                Text(
                                    stringResource(R.string.payship),
                                    fontWeight = FontWeight.Normal,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        },
                        onClick = {
                            viewModel.currentCategory = Category(
                                name = context.getString(R.string.payship)
                            )
                            viewModel.showCategoryDropDown = false
                        }
                    )

                    DropdownMenuItem(
                        text = {
                            Row {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = null
                                )
                                Spacer(Modifier.width(10.dp))
                                Text(
                                    stringResource(R.string.new_female),
                                    fontWeight = FontWeight.Normal
                                )
                            }
                        },
                        onClick = {
                            viewModel.showCategoryDialog = true
                            viewModel.showCategoryDropDown = false
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TransactionAmountSection(viewModel: TransactionViewModel) {
    OutlinedTextField(
        value = viewModel.amountString,
        onValueChange = viewModel::updateAmount,
        placeholder = { Text(stringResource(R.string.zero)) },
        modifier = Modifier.width(IntrinsicSize.Min),
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        leadingIcon = {
            Icon(imageVector = Icons.Filled.Euro, contentDescription = null)
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
    )
}

@Composable
private fun TransactionBottomButtons(viewModel: TransactionViewModel) {
    val context = LocalContext.current
    Button(
        onClick = { viewModel.save(context) },
        enabled = viewModel.currentBudget != null
                && viewModel.amountString.isNotEmpty()
                && viewModel.loadingState == State.None
    ) {
        if (viewModel.loadingState == State.Loading) {
            CircularProgressIndicator(Modifier.size(20.dp))
        } else {
            Text(
                if (!viewModel.isInUpdateMode) {
                    stringResource(R.string.new_dialog_create)
                } else {
                    stringResource(R.string.update)
                }
            )
        }
    }
}