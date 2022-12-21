package fr.jaetan.jbudget.app.auth.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import fr.jaetan.jbudget.R
import fr.jaetan.jbudget.app.auth.AuthScreens
import fr.jaetan.jbudget.app.auth.AuthViewModel

@Composable
fun AuthContent(padding: PaddingValues, viewModel: AuthViewModel) {
    Column(
        Modifier
            .padding(padding)
            .padding(horizontal = 20.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(50.dp))
        Image(
            painter = painterResource(R.drawable.app_icon),
            contentDescription = stringResource(R.string.app_name),
            modifier = Modifier
                .size(200.dp)
                .clip(CircleShape)
                .border(1.dp, MaterialTheme.colorScheme.primary, CircleShape)
        )
        Spacer(Modifier.height(50.dp))

        when (viewModel.currentScreen) {
            AuthScreens.Login -> LoginView(viewModel)
            AuthScreens.Register -> RegisterView(viewModel)
        }
    }
}