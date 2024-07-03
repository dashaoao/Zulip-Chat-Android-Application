package presentation.screens

import androidx.compose.ui.semantics.SemanticsNode
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import com.example.channels.presentation.ui.LazyListItemPosition
import com.example.channels.presentation.ui.topicNameTestTag
import com.example.channels.presentation.ui.topicsListTestTag
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode
import io.github.kakaocup.compose.node.element.lazylist.KLazyListItemNode
import io.github.kakaocup.compose.node.element.lazylist.KLazyListNode

class KaspressoChannelsScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<KaspressoChannelsScreen>(semanticsProvider = semanticsProvider) {
    val topicList = KLazyListNode(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = {
            hasTestTag(topicsListTestTag)
        },
        itemTypeBuilder = {
            itemType(::TopicLazyListItemNode)
        },
        positionMatcher = { position -> SemanticsMatcher.expectValue(LazyListItemPosition, position) }
    )
}

class TopicLazyListItemNode(
    semanticsNode: SemanticsNode,
    semanticsProvider: SemanticsNodeInteractionsProvider
) : KLazyListItemNode<TopicLazyListItemNode>(semanticsNode, semanticsProvider) {
    val name: KNode = child {
        useUnmergedTree = true
        hasTestTag(topicNameTestTag)
    }
}
