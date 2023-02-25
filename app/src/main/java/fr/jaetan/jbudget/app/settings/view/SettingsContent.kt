package fr.jaetan.jbudget.app.settings.view

import android.os.Build
import androidx.annotation.StringRes
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.jaetan.jbudget.R
import fr.jaetan.jbudget.app.settings.SettingsViewModel
import fr.jaetan.jbudget.core.models.Category
import fr.jaetan.jbudget.core.models.Themes
import fr.jaetan.jbudget.core.services.JBudget

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SettingsContent(padding: PaddingValues, viewModel: SettingsViewModel) {
    LazyColumn(modifier = Modifier
        .padding(padding)
        .fillMaxWidth()
    ) {
        //User section
        //stickyHeader { SettingsListTitle(R.string.my_data) }
        item {
            UserItem(
                Icons.Filled.AlternateEmail,
                R.string.update_my_email,
                viewModel.currentEmail
            ) { viewModel.showUpdateEmailDialog = true }
        }
        item {
            UserItem(
                Icons.Filled.Lock,
                R.string.update_my_password
            ) { viewModel.showResetPasswordDialog = true }
        }

        //Notifications section
        //item { NotificationItem(viewModel) }
        //Theme section
        item { ThemeSelector(viewModel) }
        //Categories section
        stickyHeader { CategoriesSectionHeader(viewModel) }
        item { EmptyCategoriesSection(viewModel) }
        items(viewModel.categories.size) {
            CategorySectionItem(viewModel.categories[it], viewModel)
        }
        //Disconnect section
        item { DisconnectSection() }
        item { RemoveAccountSection(viewModel) }
    }
}

@Composable
private fun UserItem(icon: ImageVector, @StringRes textRes: Int, subText: String? = null, action: () -> Unit) {
    Box(
        Modifier
            .fillMaxWidth()
            .clickable { action() }
            .height(70.dp)
    ) {
        Row(
            Modifier
                .padding(start = 15.dp)
                .fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = icon, contentDescription = stringResource(textRes))
            Spacer(Modifier.width(20.dp))
            Column(
                Modifier
                    .weight(1f)
                    .fillMaxHeight()) {
                Spacer(Modifier.weight(1f))
                Text(stringResource(textRes), style = MaterialTheme.typography.bodyMedium)
                subText?.let {
                    Text(it, style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.outline, fontSize = 11.sp))
                }
                Spacer(Modifier.weight(1f))
                Divider()
            }
        }
    }
}

/*@Composable
private fun NotificationItem(viewModel: SettingsViewModel) {
    val context = LocalContext.current

    Box(
        Modifier
            .fillMaxWidth()
            .clickable {
                viewModel.notificationHandler(
                    context,
                    !JBudget.state.isNotificationEnabled
                )
            }
            .height(70.dp)
    ) {
        Row(
            Modifier
                .padding(start = 15.dp)
                .fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = Icons.Filled.Notifications, contentDescription = stringResource(R.string.notifications))
            Spacer(Modifier.width(20.dp))
            Column(Modifier.weight(1f)) {
                Row(Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Spacer(Modifier.weight(1f))
                        Text(stringResource(R.string.notifications), style = MaterialTheme.typography.bodyMedium)
                        Spacer(Modifier.weight(1f))
                    }
                    Switch(
                        checked = JBudget.state.isNotificationEnabled,
                        onCheckedChange = { viewModel.notificationHandler(context, it) },
                        modifier = Modifier.padding(end = 15.dp)
                    )
                }
                Divider()
            }
        }
    }
}*/

@Composable
private fun ThemeSelector(viewModel: SettingsViewModel) {
    val context = LocalContext.current
    val arrowRotation by animateFloatAsState(if (!viewModel.showThemeDropDown) 0f else 180f)

    Column(
        Modifier
            .height(70.dp)
            .fillMaxWidth()
            .clickable { viewModel.showThemeDropDown = true }) {
        Row(
            Modifier
                .height(70.dp)
                .padding(horizontal = 15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = Icons.Filled.DarkMode, contentDescription = stringResource(R.string.theme))
            Spacer(Modifier.width(20.dp))
            Text(stringResource(R.string.theme), modifier = Modifier.weight(1f))

            Column(
                Modifier
                    .clickable { viewModel.showThemeDropDown = true }
            ){
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        stringResource(JBudget.state.currentTheme.textRes),
                        modifier = Modifier.padding(10.dp),
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.width(3.dp))
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowDown,
                        contentDescription = null,
                        modifier = Modifier.rotate(arrowRotation)
                    )
                }
                DropdownMenu(
                    expanded = viewModel.showThemeDropDown,
                    onDismissRequest = { viewModel.showThemeDropDown = false }
                ) {
                    Themes.values().forEach {
                        if (it == Themes.System && Build.VERSION.SDK_INT <= Build.VERSION_CODES.S) return@forEach

                        DropdownMenuItem(
                            text = {
                                Text(
                                    stringResource(it.textRes),
                                    fontWeight = if (it == JBudget.state.currentTheme) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            onClick = { viewModel.changeTheme(context, it) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DisconnectSection() {
    Column {
        Divider()
        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp)) {
            Spacer(Modifier.height(15.dp))
            Button(
                onClick = { JBudget.authRepository.disconnect() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.onErrorContainer,
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text(stringResource(R.string.disconnect))
            }
        }
    }
}

@Composable
private fun RemoveAccountSection(viewModel: SettingsViewModel) {
    Column {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp)) {
            Spacer(Modifier.height(15.dp))
            TextButton(
                onClick = { viewModel.showRemoveAccountDialog = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text(stringResource(R.string.remove_my_account))
            }
        }
    }
}

@Composable
private fun CategoriesSectionHeader(viewModel: SettingsViewModel) {
    val arrowRotation by animateFloatAsState(if (!viewModel.showCategories) 0f else 180f)

    Column(
        Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)) {
        Divider()
        Row(
            Modifier
                .fillMaxWidth()
                .height(70.dp)
                .clickable { viewModel.showCategories = !viewModel.showCategories }
                .padding(horizontal = 15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = Icons.Default.Category, contentDescription = null)
            Spacer(Modifier.width(20.dp))
            Text(
                stringResource(R.string.my_categories),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = { viewModel.showNewCategoryDialog = true }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
            Icon(
                imageVector = Icons.Filled.KeyboardArrowDown,
                contentDescription = null,
                modifier = Modifier.rotate(arrowRotation)
            )
        }
    }
}

@Composable
private fun CategorySectionItem(category: Category, viewModel: SettingsViewModel) {
    var categoryName by remember { mutableStateOf("") }
    val focusRequester = FocusRequester()

    Column(
        Modifier
            .fillMaxWidth()
            .animateContentSize()) {
        if (viewModel.showCategories && JBudget.state.categories.isNotEmpty()) {

            Divider(Modifier.padding(horizontal = 20.dp))

            Row(
                Modifier
                    .fillMaxWidth()
                    .clickable {
                        viewModel.editingCategory = category
                        categoryName = category.name
                    }
                    .padding(vertical = 20.dp, horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (viewModel.editingCategory == category) {
                    BasicTextField(
                        value = categoryName,
                        onValueChange = { categoryName = it },
                        textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onBackground),
                        singleLine = true,
                        modifier = Modifier
                            .focusRequester(focusRequester)
                            .weight(1f),
                        cursorBrush = SolidColor(MaterialTheme.colorScheme.onBackground)
                    )

                    IconButton(
                        onClick = {
                            viewModel.updateCategoryName(category.copy(name = categoryName))
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Done,
                            contentDescription = null
                        )
                    }

                    SideEffect {
                        focusRequester.requestFocus()
                    }
                } else {
                    Text(category.name, modifier = Modifier.weight(1f))
                    if (viewModel.isCategoryLoading && viewModel.editingCategory == category) {
                        CircularProgressIndicator(Modifier.size(24.dp), strokeWidth = 2.dp)
                    } else {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyCategoriesSection(viewModel: SettingsViewModel) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .animateContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (viewModel.showCategories && JBudget.state.categories.isEmpty()) {
            Spacer(Modifier.height(30.dp))
            Text(stringResource(R.string.no_categories))
            TextButton(onClick = { viewModel.showNewCategoryDialog = true }) {
                Text(stringResource(R.string.new_category))
            }
            Spacer(Modifier.height(30.dp))
        }
    }
}