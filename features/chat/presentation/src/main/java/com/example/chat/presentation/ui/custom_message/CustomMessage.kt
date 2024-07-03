package com.example.chat.presentation.ui.custom_message

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import coil.load
import com.example.chat.presentation.R
import com.example.chat.presentation.ui.custom_message.model.ReactionUi

class CustomMessage @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ViewGroup(context, attrs, defStyleAttr, defStyleRes) {

    var onClick: (reaction: ReactionUi) -> Unit = { }
    var onAddReaction: () -> Unit = { }
    var onLongClick: () -> Unit = { }

    private val image: ImageView
    private val name: TextView
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
        inflate(context, R.layout.custom_message, this)
        image = findViewById(R.id.image)
        name = findViewById(R.id.name)
        message = findViewById(R.id.message)
        reactions = findViewById(R.id.reactions)
        container = findViewById(R.id.container)
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

    fun setName(name: String) {
        this.name.text = name
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

    fun setAvatar(avatarUrl: String?) {
        avatarUrl?.let {
            image.load(avatarUrl)
        } ?: run {
            image.setImageResource(R.drawable.ic_face)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        measureChildWithMargins(
            image,
            widthMeasureSpec,
            paddingLeft + paddingRight,
            heightMeasureSpec,
            paddingTop + paddingBottom
        )

        measureChildWithMargins(
            name,
            heightMeasureSpec,
            image.fullWidth + paddingLeft + paddingRight,
            heightMeasureSpec,
            paddingTop + paddingBottom
        )

        measureChildWithMargins(
            message,
            widthMeasureSpec,
            image.fullWidth + paddingLeft + paddingRight,
            heightMeasureSpec,
            name.fullHeight + paddingTop + paddingBottom
        )

        measureChildWithMargins(
            reactions,
            widthMeasureSpec,
            image.fullWidth + paddingLeft + paddingRight,
            heightMeasureSpec,
            name.fullHeight + message.fullHeight + paddingTop + paddingBottom
        )

        val containerHeight = container.paddingTop + container.paddingBottom + name.fullHeight + message.fullHeight

        val totalWidth = MeasureSpec.getSize(widthMeasureSpec)
        val totalHeight = paddingTop + paddingBottom + maxOf(image.fullHeight, containerHeight + reactions.fullHeight)
        setMeasuredDimension(totalWidth, totalHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var offsetX = paddingLeft
        var offsetY = paddingTop

        image.layout(offsetX, offsetY)
        offsetX += image.fullWidth + container.paddingLeft
        offsetY += container.paddingTop

        name.layout(offsetX, offsetY)
        offsetY += name.fullHeight

        message.layout(offsetX, offsetY)
        offsetY += message.fullHeight + container.paddingBottom

        container.layout(
            offsetX - container.paddingLeft,
            paddingTop,
            offsetX + maxOf(name.fullWidth, message.fullWidth) + container.paddingRight,
            offsetY
        )
        offsetX -= container.paddingLeft

        reactions.layout(offsetX, offsetY)
    }

    override fun generateDefaultLayoutParams() =
        MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)

    override fun generateLayoutParams(p: LayoutParams) = MarginLayoutParams(p)

    override fun generateLayoutParams(attrs: AttributeSet?) = MarginLayoutParams(context, attrs)

    override fun checkLayoutParams(p: LayoutParams) = p is MarginLayoutParams
}
