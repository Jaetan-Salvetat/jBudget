package fr.jaetan.jbudget.core.models

enum class FirebaseResponse {
    Success,
    Error,

    //Auth
    UserAlreadyExist,
    BadEmailOrPassword
}