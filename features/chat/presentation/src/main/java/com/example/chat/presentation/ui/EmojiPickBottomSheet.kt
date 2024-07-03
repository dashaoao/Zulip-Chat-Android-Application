package com.example.chat.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chat.presentation.ChatFragment
import com.example.chat.presentation.ui.custom_message.model.EmojiNCSUi
import com.example.common.ui.theme.ChatTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmojiPickBottomSheet(
    onEmojiClick: (EmojiNCSUi) -> Unit,
    onDismissRequest: () -> Unit,
    emojiList: List<EmojiNCSUi>,
    modifier: Modifier
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        tonalElevation = 0.dp,
        shape = MaterialTheme.shapes.medium,
        dragHandle = {
            Box(
                modifier = modifier
                    .padding(top = 8.dp, bottom = 20.dp)
                    .clip(CircleShape)
                    .size(62.dp, 7.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )
        }
    ) {
        EmojiPickLayout(
            emojiList,
            onEmojiClick,
        )
    }

}

@Composable
fun EmojiPickLayout(
    emojiList: List<EmojiNCSUi>,
    onEmojiClick: (EmojiNCSUi) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(45.dp),
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(8.dp),
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
            )
        }
    }

}

@Preview
@Composable
fun EmojiPickPreview() {
    ChatTheme {
        EmojiPickLayout(
            listOf(
                EmojiNCSUi("grinning", "1f600"),
                EmojiNCSUi("big_smile", "1f604"),
                EmojiNCSUi("grinning_face_with_smiling_eyes", "1f601"),
                EmojiNCSUi("laughing", "1f606"),
                EmojiNCSUi("sweat_smile", "1f605"),
                EmojiNCSUi("rolling_on_the_floor_laughing", "1f923"),
                EmojiNCSUi("joy", "1f602"),
                EmojiNCSUi("smile", "1f642"),
                EmojiNCSUi("upside_down", "1f643"),
                EmojiNCSUi("wink", "1f609"),
                EmojiNCSUi("blush", "1f60a"),
                EmojiNCSUi("innocent", "1f607"),
                EmojiNCSUi("heart_eyes", "1f60d"),
                EmojiNCSUi("heart_kiss", "1f618"),
            ),
            onEmojiClick = {},
        )
    }
}
