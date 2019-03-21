package com.example.pshmakov.blacksurfaceviewsample

import android.animation.LayoutTransition
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.transition.Transition
import android.util.AttributeSet
import android.util.Log
import android.view.*

import com.example.pshmakov.blacksurfaceviewsample2.R
import android.graphics.drawable.ColorDrawable
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator





class TargetActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setBackgroundDrawable(android.graphics.drawable.ColorDrawable(Color.BLUE))
        android.util.Log.e("ABCD", "created")
        setContentView(R.layout.activity_target)
        findViewById<ViewGroup>(R.id.layout).setBackgroundColor(Color.BLUE)
        val surfaceView = DrawingSurfaceView(this).apply {
            layoutParams = ViewGroup.MarginLayoutParams(500, 300).apply { bottomMargin = 100 }
        }

        try {
            // Emulate behavior of EnterTransitionCoordinator
            var listenerClass = Class.forName("android.app.Activity\$TranslucentConversionListener")
            var method = TargetActivity::class.java.getMethod("convertToTranslucent", listenerClass, android.app.ActivityOptions::class.java)
            method.invoke(this@TargetActivity, null, null)
            var background = getWindow().decorView.getBackground()
            window.setBackgroundDrawable(null)
            background!!.setAlpha(0)
            window.setBackgroundDrawable(background)
        } catch (e:Exception) {
            e.printStackTrace();
        }

        android.os.Handler().postDelayed(object:Runnable {
            override fun run() {
                getWindow().decorView.getBackground().setAlpha(255)
                try {
                    TargetActivity::class.java.getMethod("convertFromTranslucent").invoke(this@TargetActivity)
                } catch (e:Exception) {
                    e.printStackTrace();
                }
                findViewById<ViewGroup>(R.id.layout).addView(surfaceView)
            }
        }, 10000)
    }

    internal inner class DrawingSurfaceView : SurfaceView {

        constructor(context: Context) : this(context, null)
        constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet) {
            holder.addCallback(object : SurfaceHolder.Callback2 {

                override fun surfaceDestroyed(holder: SurfaceHolder?) {
                }

                override fun surfaceCreated(holder: SurfaceHolder?) {
                    draw()
                }

                override fun surfaceRedrawNeeded(holder: SurfaceHolder?) {
                    draw()
                }

                override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
                    draw();
                }

            })
        }

        private var path: Path? = null

        private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = 3f
            color = Color.BLUE
        }

        private val fillPaint = Paint().apply {
            style = Paint.Style.FILL
            color = Color.rgb(-0, 0, 255)
        }


        override fun onTouchEvent(event: MotionEvent): Boolean {
            when {
                event.action == MotionEvent.ACTION_DOWN -> {
                    path = Path().also { it.moveTo(event.x, event.y) }
                }
                event.action == MotionEvent.ACTION_MOVE -> path?.lineTo(event.x, event.y)
                event.action == MotionEvent.ACTION_UP -> path?.lineTo(event.x, event.y)
            }

            draw()
            return true
        }

        fun draw(color: Int = Color.rgb(100, 100, 255)) {
            fillPaint.color = color
            val canvas = holder.lockCanvas()
            canvas?.drawRect(0f, 0f, width.toFloat(), height.toFloat(), fillPaint)
            path?.let { canvas?.drawPath(it, paint) }
            holder.unlockCanvasAndPost(canvas)
        }


    }
}
