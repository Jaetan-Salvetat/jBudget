package fr.jaetan.jbudget.core.services.extentions

operator fun Boolean.inc(): Boolean {
    return !this
}