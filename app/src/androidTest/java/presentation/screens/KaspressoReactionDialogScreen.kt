package presentation.screens

import androidx.compose.ui.semantics.SemanticsNode
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import com.example.channels.presentation.ui.LazyListItemPosition
import com.example.chat.presentation.ChatFragment
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode
import io.github.kakaocup.compose.node.element.lazylist.KLazyListItemNode
import io.github.kakaocup.compose.node.element.lazylist.KLazyListNode

class KaspressoReactionDialogScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<KaspressoReactionDialogScreen>(semanticsProvider = semanticsProvider) {
    val reactionList = KLazyListNode(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = {
            hasTestTag(ChatFragment.reactionListTestTag)
        },
        itemTypeBuilder = {
            itemType(::ReactionLazyListItemNode)
        },
        positionMatcher = { position ->
            SemanticsMatcher.expectValue(
                LazyListItemPosition,
                position
            )
        }
    )
}

class ReactionLazyListItemNode(
    semanticsNode: SemanticsNode,
    semanticsProvider: SemanticsNodeInteractionsProvider
) : KLazyListItemNode<ReactionLazyListItemNode>(semanticsNode, semanticsProvider) {
    val reactions = KNode(semanticsProvider) {
        hasTestTag(ChatFragment.chatItemTestTag)
    }
}
