package fr.jaetan.jbudget.core.services.extentions

import android.util.Patterns

val String?.isEmail: Boolean
    get() = this == null || Patterns.EMAIL_ADDRESS.matcher(this).matches() && isNotEmpty()

val String?.isPassword: Boolean
    get() = this == null || length >= 8

val String?.isUsename: Boolean
    get() = this == null || length >= 3


val String.isCategoryName: Boolean
    get() = length >= 3