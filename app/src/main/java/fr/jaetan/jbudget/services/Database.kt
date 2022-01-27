package fr.jaetan.jbudget.services

import android.content.Context
import fr.jaetan.jbudget.models.MyObjectBox
import io.objectbox.BoxStore

object Database {
    lateinit var store: BoxStore

    fun init(context: Context) {
        store = MyObjectBox.builder()
            .androidContext(context.applicationContext)
            .build()
    }
}