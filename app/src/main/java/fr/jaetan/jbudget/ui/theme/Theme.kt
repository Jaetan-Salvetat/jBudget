package fr.jaetan.jbudget.ui.theme

import android.content.Context
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import fr.jaetan.jbudget.core.models.Themes
import fr.jaetan.jbudget.core.services.MainViewModel

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun JBudgetTheme(
    state: MainViewModel,
    content: @Composable () -> Unit
) {
    val dynamicColor = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    val darkTheme = isSystemInDarkTheme()

    MaterialTheme(
        colorScheme = getTheme(state.currentTheme, darkTheme, dynamicColor, LocalContext.current),
        typography = Typography,
        content = content
    )
}

private fun getTheme(theme: Themes, isSystemDarkTheme: Boolean, dynamicColor: Boolean, context: Context): ColorScheme = when {
    dynamicColor && theme == Themes.Light -> dynamicLightColorScheme(context)
    dynamicColor && theme == Themes.Dark -> dynamicDarkColorScheme(context)
    dynamicColor && theme == Themes.System && isSystemDarkTheme -> dynamicDarkColorScheme(context)
    dynamicColor && theme == Themes.System && !isSystemDarkTheme -> dynamicLightColorScheme(context)
    !dynamicColor && theme == Themes.Light -> LightColorScheme
    !dynamicColor && theme == Themes.Dark -> DarkColorScheme
    else -> darkColorScheme(primary = Color.Red)
}