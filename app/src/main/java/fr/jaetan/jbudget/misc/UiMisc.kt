package fr.jaetan.jbudget.misc

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface

class UiMisc {
    companion object {
        public fun alertDialog(context: Context, callback: DialogInterface.OnClickListener, title: String = "", text: String = ""){
            val dialogBuilder = AlertDialog.Builder(context)

            dialogBuilder.setMessage(text)
                .setTitle(title)
                .setPositiveButton("continuer", callback)
                .setNegativeButton("annuler") { dialog, _ ->
                    dialog.cancel()
                }

            dialogBuilder.create().show()
        }
    }
}