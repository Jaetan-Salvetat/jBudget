package fr.jaetan.jbudget.app.home

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector

class FabItem(@StringRes val text: Int, @StringRes val descriptor: Int, val onClick: () -> Unit, val icon: ImageVector)