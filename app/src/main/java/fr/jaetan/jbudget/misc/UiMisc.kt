package fr.jaetan.jbudget.misc

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.view.MotionEvent
import android.view.ViewGroup
import android.view.ViewPropertyAnimator
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import androidx.core.view.updateLayoutParams
import kotlinx.coroutines.*

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
        fun scaleAnimation(element: ViewGroup, callback: () -> Unit){element.setOnTouchListener { _, event ->
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        scale(element, .95f, 1f)
                        true
                    }
                    MotionEvent.ACTION_CANCEL -> {
                        scale(element, 1f, .95f)
                        true
                    }
                    MotionEvent.ACTION_UP -> {
                        scale(element, 1f, .95f)
                        callback()
                        true
                    }
                    else -> false
                }
            }
        }

        private fun scale(element: ViewGroup, scaleTo: Float, scaleFrom: Float) {
            val anim = ScaleAnimation(scaleFrom, scaleTo, scaleFrom, scaleTo, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
            anim.duration = 300
            anim.isFillEnabled = true
            anim.fillAfter = true
            element.startAnimation(anim)
        }
    }
}