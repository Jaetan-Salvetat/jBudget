package fr.jaetan.jbudget.ui.widgets

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import fr.jaetan.jbudget.R
import kotlin.system.exitProcess

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBackHandler(navController: NavHostController) {
    var showQuitDialog by rememberSaveable { mutableStateOf(false) }
    BackHandler {
        if (navController.backQueue.size <= 2) showQuitDialog = true
        else navController.popBackStack()
    }

    if (showQuitDialog) {
        AlertDialog(onDismissRequest = { showQuitDialog = false }) {
            Box(Modifier.background(MaterialTheme.colorScheme.background, RoundedCornerShape(20.dp))) {
                Column(Modifier.padding(20.dp)) {
                    Text(
                        text = stringResource(R.string.quit_app_dialog_text),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(Modifier.height(20.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        TextButton(onClick = { showQuitDialog = false }) {
                            Text(stringResource(R.string.cancel))
                        }
                        TextButton(onClick = { exitProcess(0) }) {
                            Text(stringResource(R.string.quit))
                        }
                    }
                }
            }
        }
    }
}