package fr.jaetan.jbudget.misc

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.CountDownTimer
import android.view.MotionEvent
import android.view.ViewGroup
import android.view.animation.ScaleAnimation

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
                        scale(element, .95f)
                        true
                    }
                    MotionEvent.ACTION_MOVE, MotionEvent.ACTION_CANCEL -> {
                        scale(element, 1f)
                        true
                    }
                    MotionEvent.ACTION_UP -> {
                        scale(element, 1f)
                        callback()
                        true
                    }
                    else -> false
                }
            }
        }

        private fun scale(element: ViewGroup, scale: Float) {
            element.animate().scaleX(scale).duration = 300
            element.animate().scaleY(scale).duration = 300
        }
    }
}