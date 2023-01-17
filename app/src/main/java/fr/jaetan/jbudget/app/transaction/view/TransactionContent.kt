package fr.jaetan.jbudget.app.transaction.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Euro
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import fr.jaetan.jbudget.R
import fr.jaetan.jbudget.app.transaction.TransactionViewModel

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
            TransactionSelectBudget(viewModel)
            TransactionSelectCategory(viewModel)
            AnimatedVisibility(viewModel.showCategoryInput) {
                TransactionCategoryName(viewModel)
            }
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
                            onClick = {
                                viewModel.currentBudget = it
                                viewModel.showBudgetDropDown = false
                            }
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
                    viewModel.categories.forEach {
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
                            viewModel.showCategoryInput = true
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
private fun TransactionCategoryName(viewModel: TransactionViewModel) {
    val focusRequester = remember { FocusRequester() }
    SideEffect { focusRequester.requestFocus() }

    OutlinedTextField(
        value = viewModel.categoryName,
        onValueChange = { viewModel.categoryName = it },
        label = { Text(stringResource(R.string.category_name)) },
        colors = TextFieldDefaults.outlinedTextFieldColors(containerColor = Color.Transparent),
        modifier = Modifier.focusRequester(focusRequester),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = { viewModel.saveCategory() })
    )
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
    Button(
        onClick = viewModel::save,
        enabled = viewModel.currentBudget != null
                && viewModel.currentCategory != null
                && viewModel.amountString.isNotEmpty()
    ) {
        Text(stringResource(R.string.new_dialog_create))
    }
}