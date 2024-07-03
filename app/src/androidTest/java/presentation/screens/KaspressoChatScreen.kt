package presentation.screens

import androidx.compose.ui.semantics.SemanticsNode
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import com.example.channels.presentation.ui.LazyListItemPosition
import com.example.chat.presentation.ChatFragment.Companion.chatItemTestTag
import com.example.chat.presentation.ChatFragment.Companion.messageListTestTag
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode
import io.github.kakaocup.compose.node.element.lazylist.KLazyListItemNode
import io.github.kakaocup.compose.node.element.lazylist.KLazyListNode

class KaspressoChatScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<KaspressoChatScreen>(semanticsProvider = semanticsProvider) {
    val messageList = KLazyListNode(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = {
            hasTestTag(messageListTestTag)
        },
        itemTypeBuilder = {
            itemType(::MessageLazyListItemNode)
        },
        positionMatcher = { position -> SemanticsMatcher.expectValue(LazyListItemPosition, position) }
    )
}

class MessageLazyListItemNode(
    semanticsNode: SemanticsNode,
    semanticsProvider: SemanticsNodeInteractionsProvider
) : KLazyListItemNode<MessageLazyListItemNode>(semanticsNode, semanticsProvider) {
    val message = KNode(semanticsProvider) {
        hasTestTag(chatItemTestTag)
    }
}
