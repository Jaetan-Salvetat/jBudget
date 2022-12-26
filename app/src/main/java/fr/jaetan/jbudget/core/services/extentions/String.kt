package fr.jaetan.jbudget.core.services.extentions

import android.util.Patterns

val String.isEmail: Boolean
    get() = Patterns.EMAIL_ADDRESS.matcher(this).matches() && isNotEmpty()

val String.isPassword: Boolean
    get() = length >= 8