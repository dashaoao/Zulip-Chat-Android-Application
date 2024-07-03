package com.example.chat.presentation.ui.custom_message

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import com.example.common.ui.R

class ReactionView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    private val textToDraw: String
        get() {
            return if (count == 0) emoji
            else "$emoji $count"
        }

    var emoji = DEFAULT_EMOJI
        set(value) {
            field = value
            requestLayout()
        }

    var count: Int = DEFAULT_COUNT
        set(value) {
            field = value
            requestLayout()
        }

    private val textBounds = Rect()
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 14f.sp(context)
        color = Color.WHITE
    }

    init {
        context.withStyledAttributes(attrs, R.styleable.EmojiView) {
            emoji = this.getString(R.styleable.EmojiView_emoji) ?: DEFAULT_EMOJI
            count = this.getInt(R.styleable.ReactionView_count, DEFAULT_COUNT)
        }
        isClickable = true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        textPaint.getTextBounds(textToDraw, 0, textToDraw.length, textBounds)
        val measuredWidth = resolveSize(textBounds.width() + paddingLeft + paddingRight, widthMeasureSpec)
        val measuredHeight = resolveSize(textBounds.height() + paddingTop + paddingBottom, heightMeasureSpec)
        setMeasuredDimension(measuredWidth, measuredHeight)
    }

    override fun onDraw(canvas: Canvas) {
        val centerY = height / 2 - textBounds.exactCenterY()
        canvas.drawText(textToDraw, paddingLeft.toFloat(), centerY, textPaint)
    }

    override fun onSaveInstanceState(): Parcelable {
        val bundle = Bundle()
        bundle.putString(EMOJI_STATE_KEY, emoji)
        bundle.putInt(COUNT_STATE_KEY, count)
        bundle.putParcelable(SUPER_STATE_KEY, super.onSaveInstanceState())
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        var viewState = state
        if (viewState is Bundle) {
            emoji = viewState.getString(EMOJI_STATE_KEY, DEFAULT_EMOJI)
            count = viewState.getInt(COUNT_STATE_KEY, DEFAULT_COUNT)
            viewState = viewState.getParcelable(SUPER_STATE_KEY)!!
        }
        super.onRestoreInstanceState(viewState)
    }

    companion object {
        private const val DEFAULT_EMOJI = ""
        private const val DEFAULT_COUNT = 0
        private const val EMOJI_STATE_KEY = "EMOJI_STATE_KEY"
        private const val COUNT_STATE_KEY = "COUNT_STATE_KEY"
        private const val SUPER_STATE_KEY = "SUPER_STATE_KEY"
    }
}
