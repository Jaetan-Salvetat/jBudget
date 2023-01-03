package fr.jaetan.jbudget.app.settings.view

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import fr.jaetan.jbudget.R
import fr.jaetan.jbudget.app.settings.SettingsViewModel
import fr.jaetan.jbudget.core.models.Themes
import fr.jaetan.jbudget.core.services.JBudget

@Composable
fun SettingsContent(padding: PaddingValues, viewModel: SettingsViewModel) {
    LazyColumn(modifier = Modifier
        .padding(padding)
        .fillMaxWidth()) {
        //User section
        //stickyHeader { SettingsListTitle(R.string.my_data) }
        item { UserItem(Icons.Filled.AlternateEmail, R.string.update_my_email) {} }
        item { UserItem(Icons.Filled.Person, R.string.update_my_username) {} }
        item { UserItem(Icons.Filled.Lock, R.string.update_my_password, false) {} }

        //Theme section
        item { ThemeSelector(viewModel) }

        //Other section
        item { OtherSection() }
    }
}

@Composable
private fun OtherSection() {
    Column {
        Divider()
        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp)) {
            Spacer(Modifier.height(15.dp))
            Button(
                onClick = {  },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                )
            ) {
                Text("Déconnexion")
            }
            TextButton(
                onClick = {  },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) { Text("Supprimer mon compte") }
        }
    }
}

@Composable
private fun UserItem(icon: ImageVector, @StringRes textRes: Int, showDivider: Boolean = true, action: () -> Unit) {
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
                Spacer(Modifier.weight(1f))
                if (showDivider) Divider()
            }
        }
    }
}

@Composable
private fun ThemeSelector(viewModel: SettingsViewModel) {
    val context = LocalContext.current

    Column(Modifier.height(70.dp).fillMaxWidth().clickable { viewModel.showThemeDropDown = true }) {
        Divider()
        Row(
            Modifier
                .height(70.dp)
                .padding(horizontal = 15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Changer le thème de l'application", modifier = Modifier.weight(1f))

            Column(
                Modifier
                    .clickable { viewModel.showThemeDropDown = true }
                    .clip(RoundedCornerShape(5.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(5.dp))
            ){
                Text(
                    JBudget.state.currentTheme.text,
                    modifier = Modifier.padding(10.dp),
                    fontWeight = FontWeight.Bold
                )
                DropdownMenu(
                    expanded = viewModel.showThemeDropDown,
                    onDismissRequest = { viewModel.showThemeDropDown = false }
                ) {
                    Themes.values().forEach {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    it.text,
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