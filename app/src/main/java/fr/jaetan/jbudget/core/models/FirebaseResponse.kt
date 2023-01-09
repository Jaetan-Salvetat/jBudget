package fr.jaetan.jbudget.core.models

import androidx.annotation.StringRes
import fr.jaetan.jbudget.R

enum class FirebaseResponse(@StringRes val messageRes: Int) {
    Success(R.string.success),
    Error(R.string.sample_error),
    ConnectivityError(R.string.connectivity_error),

    //Auth
    UserAlreadyExist(R.string.user_already_exist),
    BadEmailOrPassword(R.string.bad_email_or_password)
}