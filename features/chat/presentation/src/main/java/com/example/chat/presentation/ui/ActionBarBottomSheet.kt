package com.example.chat.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chat.presentation.ChatFragment
import com.example.chat.presentation.ui.custom_message.model.EmojiNCSUi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActionBar(
    onEmojiClick: (EmojiNCSUi) -> Unit,
    onDismissRequest: () -> Unit,
    onCopyButtonClick: () -> Unit,
    onDeleteButtonClick: () -> Unit,
    onEditButtonClick: () -> Unit,
    modifier: Modifier,
    emojiList: List<EmojiNCSUi>,
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        tonalElevation = 0.dp,
        shape = MaterialTheme.shapes.medium,
        containerColor = Color.Transparent,
        dragHandle = { },
    ) {
        ActionBarLayout(
            emojiList = emojiList,
            onCopyButtonClick = onCopyButtonClick,
            onDeleteButtonClick = onDeleteButtonClick,
            onEditButtonClick = onEditButtonClick,
            onEmojiClick = onEmojiClick,
            onChangeTopicClick = {},
            isOwnMessage = false,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OwnActionBar(
    onEmojiClick: (EmojiNCSUi) -> Unit,
    onDismissRequest: () -> Unit,
    onCopyButtonClick: () -> Unit,
    onDeleteButtonClick: () -> Unit,
    onEditButtonClick: () -> Unit,
    modifier: Modifier,
    onChangeTopicClick: () -> Unit,
    emojiList: List<EmojiNCSUi>,
) {
    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        tonalElevation = 0.dp,
        shape = MaterialTheme.shapes.medium,
        containerColor = Color.Transparent,
        dragHandle = { },
    ) {
        ActionBarLayout(
            emojiList = emojiList,
            onCopyButtonClick = onCopyButtonClick,
            onDeleteButtonClick = onDeleteButtonClick,
            onEditButtonClick = onEditButtonClick,
            onEmojiClick = onEmojiClick,
            onChangeTopicClick = onChangeTopicClick,
            isOwnMessage = true,
        )
    }
}

@Composable
fun ActionBarLayout(
    emojiList: List<EmojiNCSUi>,
    onCopyButtonClick: () -> Unit,
    onDeleteButtonClick: () -> Unit,
    onEditButtonClick: () -> Unit,
    onEmojiClick: (EmojiNCSUi) -> Unit,
    onChangeTopicClick: () -> Unit,
    isOwnMessage: Boolean
) {
    Column(modifier = Modifier.navigationBarsPadding()) {
        Surface(
            color = MaterialTheme.colorScheme.surfaceContainer,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
            ) {
                items(items = emojiList, key = { item -> item.code }) { emoji ->
                    Text(
                        text = emoji.getCodeString(),
                        fontSize = 30.sp,
                        modifier = Modifier
                            .clickable { onEmojiClick(emoji) }
                            .semantics {
                                testTag = ChatFragment.reactionTestTag
                            }
                            .padding(4.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Surface(
            color = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        ) {
            Column(
                modifier = Modifier.padding(8.dp),
            ) {
                if (isOwnMessage){
                    ButtonOptions(
                        text = "Delete",
                        icon = Icons.Default.Delete,
                        onClick = onDeleteButtonClick,
                    )
                    Spacer(Modifier.height(8.dp))
                    ButtonOptions(
                        text = "Edit",
                        icon = Icons.Default.Edit,
                        onClick = onEditButtonClick,
                    )
                    Spacer(Modifier.height(8.dp))
                    ButtonOptions(
                        text = "Change topic",
                        icon = Icons.AutoMirrored.Filled.ExitToApp,
                        onClick = onChangeTopicClick,
                    )
                    Spacer(Modifier.height(8.dp))
                }
                ButtonOptions(
                    text = "Copy",
                    icon = Icons.Default.ContentCopy,
                    onClick = onCopyButtonClick,
                )
            }
        }

    }
}

@Composable
fun ButtonOptions(text: String, icon: ImageVector, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(imageVector = icon, contentDescription = null)
            Spacer(Modifier.width(16.dp))
            Text(
                text = text,
                textAlign = TextAlign.Start,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Normal
            )
        }
    }
}
