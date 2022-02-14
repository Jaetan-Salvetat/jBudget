package fr.jaetan.jbudget.misc

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.MotionEvent
import android.view.ViewGroup

class UiMisc {
    companion object {
        fun alertDialog(context: Context, callback: DialogInterface.OnClickListener, title: String = "", text: String = ""){
            val dialogBuilder = AlertDialog.Builder(context)

            dialogBuilder.setMessage(text)
                .setTitle(title)
                .setPositiveButton("continuer", callback)
                .setNegativeButton("annuler") { dialog, _ ->
                    dialog.cancel()
                }

            dialogBuilder.create().show()
        }

        @SuppressLint("ClickableViewAccessibility")
        fun scaleAnimation(element: ViewGroup, callback: () -> Unit){
            element.setOnTouchListener { _, event ->
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        scale(element, .9f, 300)
                    }
                    MotionEvent.ACTION_MOVE, MotionEvent.ACTION_CANCEL -> {
                        scale(element, 1f, 300)
                    }
                    MotionEvent.ACTION_UP -> {
                        scale(element, 1f, 300)
                        callback()
                    }
                }

                true
            }
        }

        private fun scale(element: ViewGroup, scale: Float, duration: Long) {
            element.animate().scaleX(scale).duration = duration
            element.animate().scaleY(scale).duration = duration
        }
    }
}