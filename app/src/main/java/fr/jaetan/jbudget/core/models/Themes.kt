package fr.jaetan.jbudget.core.models

import androidx.annotation.StringRes
import fr.jaetan.jbudget.R

enum class Themes(@StringRes val textRes: Int) {
    Light(R.string.theme_light),
    Dark(R.string.theme_dark),
    System(R.string.theme_system)
}