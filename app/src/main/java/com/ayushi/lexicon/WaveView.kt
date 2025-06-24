package com.ayushi.lexicon


import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils

class WaveView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    // Wave properties with defaults
    private var waveHeight = 50f
    private var waveCount = 3
    private var waveColor = ContextCompat.getColor(context, R.color.black)
    private var secondaryColor = ContextCompat.getColor(context, R.color.white)
    private var waveAlpha = 0.45f
    private var waveProgress = 1f
    private var waveVelocity = 0.2f
    private var isRunning = true

    private val wavePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    private val path = Path()
    private var offset = 0f

    init {
        // Handle XML attributes
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.WaveView,
            0, 0
        ).apply {
            try {
                waveHeight = getDimension(R.styleable.WaveView_waveHeight, 50f)
                waveCount = getInt(R.styleable.WaveView_waveCount, 3)
                waveColor = getColor(R.styleable.WaveView_waveColor, waveColor)
                secondaryColor = getColor(R.styleable.WaveView_waveSecondaryColor, secondaryColor)
                waveAlpha = getFloat(R.styleable.WaveView_waveAlpha, 0.45f)
                waveProgress = getFloat(R.styleable.WaveView_waveProgress, 1f)
                waveVelocity = getFloat(R.styleable.WaveView_waveVelocity, 0.2f)
                isRunning = getBoolean(R.styleable.WaveView_waveRunning, true)
            } finally {
                recycle()
            }
        }
    }

    private val waveAnimator by lazy {
        ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 2000
            repeatCount = ValueAnimator.INFINITE
            interpolator = LinearInterpolator()
            addUpdateListener {
                offset = it.animatedValue as Float
                invalidate()
            }
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (isRunning) startAnimation()
    }

    override fun onDetachedFromWindow() {
        stopAnimation()
        super.onDetachedFromWindow()
    }

    fun startAnimation() {
        if (!waveAnimator.isRunning) {
            waveAnimator.start()
        }
    }

    fun pauseAnimation() {
        waveAnimator.pause()
    }

    fun resumeAnimation() {
        waveAnimator.resume()
    }

    fun stopAnimation() {
        waveAnimator.cancel()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val width = width.toFloat()
        val height = height * waveProgress

        // Create gradient
        val gradient = LinearGradient(
            0f, 0f, 0f, height,
            ColorUtils.setAlphaComponent(waveColor, (waveAlpha * 255).toInt()),
            ColorUtils.setAlphaComponent(secondaryColor, (waveAlpha * 255).toInt()),
            Shader.TileMode.CLAMP
        )
        wavePaint.shader = gradient

        // Draw waves
        for (i in 0 until waveCount) {
            val phase = 2 * Math.PI * i / waveCount
            val phaseOffset = (offset * waveVelocity + phase).toFloat()
            drawWave(canvas, width, height, phaseOffset)
        }
    }

    private fun drawWave(canvas: Canvas, width: Float, height: Float, phase: Float) {
        path.reset()
        path.moveTo(0f, height)

        val waveLength = width / 2
        val amplitude = waveHeight

        for (x in 0..width.toInt() step 20) {
            val xf = x.toFloat()
            val angle = 2 * Math.PI * (xf / waveLength + phase)
            val y = height - amplitude * sin(angle.toFloat())
            if (x == 0) path.moveTo(xf, y)
            else path.lineTo(xf, y)
        }

        path.lineTo(width, height)
        path.lineTo(0f, height)
        path.close()
        canvas.drawPath(path, wavePaint)
    }

    private fun sin(angle: Float) = kotlin.math.sin(angle).toFloat()
}