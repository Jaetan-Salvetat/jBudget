package fr.jaetan.jbudget.misc

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.LinearLayout
import java.time.Duration

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
                        element.animate().scaleX(0.95f)
                        element.animate().scaleY(0.95f)
                    }
                    MotionEvent.ACTION_MOVE -> {
                        element.animate().scaleX(1f)
                        element.animate().scaleY(1f)
                    }

                    MotionEvent.ACTION_UP -> {
                        element.animate().scaleX(1f)
                        element.animate().scaleY(1f)
                        callback()
                    }
                }

                true
            }
        }
    }
}