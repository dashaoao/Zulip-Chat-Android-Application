package com.example.chat.presentation.ui.custom_message

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup

class ReactionFlexboxLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ViewGroup(context, attrs, defStyleAttr, defStyleRes) {

    override fun addView(child: View?) {
        child?.layoutParams = generateDefaultLayoutParams()
        super.addView(child)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val availableWidth = MeasureSpec.getSize(widthMeasureSpec) - paddingLeft - paddingRight
        var maxWidth = 0
        var height = 0
        var width = 0
        var rowCount = 1
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (child.visibility != View.GONE) {
                measureChild(child, widthMeasureSpec, heightMeasureSpec)
                if (width + child.fullWidth > availableWidth) {
                    width = 0
                    rowCount++
                }
                height = child.fullHeight * rowCount
                width += child.fullWidth
                maxWidth = maxOf(width, maxWidth)
            }
        }
        maxWidth += paddingLeft + paddingRight
        height += paddingTop + paddingBottom
        setMeasuredDimension(maxWidth, height)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var offsetX = paddingLeft
        var offsetY = paddingTop
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (offsetX + child.fullWidth > measuredWidth) {
                offsetX = paddingLeft
                offsetY += child.fullHeight
            }
            child.layout(offsetX, offsetY)
            offsetX += child.fullWidth
        }
    }

    override fun generateDefaultLayoutParams(): MarginLayoutParams {
        return MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            .apply { setMargins(MARGIN, MARGIN, MARGIN, MARGIN) }
    }

    override fun generateLayoutParams(p: LayoutParams) = MarginLayoutParams(p)

    override fun generateLayoutParams(attrs: AttributeSet?) = MarginLayoutParams(context, attrs)

    override fun checkLayoutParams(p: LayoutParams) = p is MarginLayoutParams

    companion object {
        const val MARGIN = 10
    }
}
