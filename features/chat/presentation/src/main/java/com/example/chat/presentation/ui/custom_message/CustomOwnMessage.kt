package com.example.chat.presentation.ui.custom_message

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import com.example.chat.presentation.R
import com.example.chat.presentation.ui.custom_message.model.ReactionUi

class CustomOwnMessage @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ViewGroup(context, attrs, defStyleAttr, defStyleRes) {

    var onClick: (reaction: ReactionUi) -> Unit = { }
    var onAddReaction: () -> Unit = { }
    var onLongClick: () -> Unit = { }

    private val message: TextView
    private val reactions: ReactionFlexboxLayout
    private val container: View

    private val plusView = ReactionView(context, defStyleRes = com.example.common.ui.R.style.EmojiView).apply {
        emoji = context.getString(com.example.common.ui.R.string.emoji_plus)
        visibility = View.GONE
        layoutParams = generateDefaultLayoutParams()
        setOnClickListener { onAddReaction() }
    }

    init {
        inflate(context, R.layout.custom_own_message, this)
        message = findViewById(R.id.message)
        reactions = findViewById(R.id.reactions)
        container = findViewById(R.id.bg_container)
        reactions.addView(plusView)
        isLongClickable = true
    }

    override fun performLongClick(): Boolean {
        if (super.performLongClick()) return true
        onLongClick()
        return true
    }

    private fun bind(reaction: ReactionUi) =
        ReactionView(context, defStyleRes = com.example.common.ui.R.style.EmojiView).apply {
            emoji = reaction.emoji.getCodeString()
            count = reaction.count
            isSelected = reaction.selected
            setOnClickListener { onClick(reaction) }
        }

    fun setMessage(text: String) {
        message.text = text
        requestLayout()
    }

    fun setReactions(reactions: List<ReactionUi>) {
        this.reactions.apply {
            removeAllViews()
            addView(plusView)
            reactions.forEach { addView(bind(it),  childCount - 1) }
            plusView.isVisible = childCount > 1
        }
        requestLayout()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        measureChildWithMargins(
            message,
            widthMeasureSpec,
            paddingLeft + paddingRight,
            heightMeasureSpec,
            paddingTop + paddingBottom
        )

        measureChildWithMargins(
            reactions,
            widthMeasureSpec,
            paddingLeft + paddingRight,
            heightMeasureSpec,
            message.fullHeight + paddingTop + paddingBottom
        )

        val containerHeight = container.paddingTop + container.paddingBottom + message.fullHeight

        val totalWidth = MeasureSpec.getSize(widthMeasureSpec)
        val totalHeight = paddingTop + paddingBottom + containerHeight + reactions.fullHeight
        setMeasuredDimension(totalWidth, totalHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var offsetY = paddingTop

        container.layout(
            fullWidth - (paddingRight + message.fullWidth + container.paddingLeft + container.paddingRight),
            offsetY,
            fullWidth - paddingRight,
            offsetY + message.fullHeight + container.paddingBottom + container.paddingTop
        )
        offsetY += container.paddingTop

        message.layout(
            fullWidth - (paddingRight + container.paddingRight + message.fullWidth),
            offsetY,
            fullWidth - (paddingRight + container.paddingRight),
            offsetY + message.fullHeight
        )
        offsetY += message.fullHeight + container.paddingBottom

        reactions.layout(
            fullWidth - (paddingRight + reactions.fullWidth),
            offsetY + reactions.marginTop,
            fullWidth - (paddingRight + reactions.marginRight),
            offsetY + reactions.fullHeight
        )
    }

    override fun generateDefaultLayoutParams() =
        MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)

    override fun generateLayoutParams(p: LayoutParams) = MarginLayoutParams(p)

    override fun generateLayoutParams(attrs: AttributeSet?) = MarginLayoutParams(context, attrs)

    override fun checkLayoutParams(p: LayoutParams) = p is MarginLayoutParams
}
