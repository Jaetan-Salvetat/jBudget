package fr.jaetan.jbudget.misc

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.View
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

        fun scaleAnimationStart(element: LinearLayout, context: Context){
            val animation = ScaleAnimation(1f, 0.9f, 1f, 0.9f, 50f, 50f)
            animation.setInterpolator(context, android.R.interpolator.accelerate_decelerate)
            animation.duration = 1000
            element.startAnimation(animation)
        }
        fun scaleAnimationEnd(element: LinearLayout, context: Context){
            val animation = ScaleAnimation(0.9f, 1f, 0.9f, 1f, 50f, 50f)
            animation.setInterpolator(context, android.R.interpolator.accelerate_decelerate)
            animation.duration = 1000
            element.startAnimation(animation)
        }
    }
}