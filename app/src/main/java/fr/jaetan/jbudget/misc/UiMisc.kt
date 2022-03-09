package fr.jaetan.jbudget.misc

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.view.ContextThemeWrapper
import android.view.MotionEvent
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import fr.jaetan.jbudget.R

class UiMisc {
    companion object {
        fun alertDialog(context: Context, callback: DialogInterface.OnClickListener, title: String = "", text: String = ""){
            MaterialAlertDialogBuilder(ContextThemeWrapper(context, R.style.Theme_Dialog))
                .setMessage(text)
                .setTitle(title)
                .setPositiveButton("continuer", callback)
                .setNegativeButton("annuler") { dialog, _ ->
                    dialog.cancel()
                }
                .show()
            
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
            anim.duration = 200
            anim.isFillEnabled = true
            anim.fillAfter = true
            element.startAnimation(anim)
        }
    }
}