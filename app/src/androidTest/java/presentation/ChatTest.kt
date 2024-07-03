package presentation


import android.content.Context
import android.os.Bundle
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.performSemanticsAction
import androidx.compose.ui.test.performTextInput
import androidx.fragment.app.testing.FragmentScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.chat.presentation.ChatFragment
import com.example.chat.presentation.ChatFragment.Companion.TOPIC_ID
import com.example.common.ui.TopicId
import com.example.coursework.MainActivity
import com.example.coursework.R
import com.example.coursework.getAppComponent
import com.kaspersky.components.composesupport.config.withComposeSupport
import com.kaspersky.kaspresso.kaspresso.Kaspresso
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import io.github.kakaocup.compose.node.element.ComposeScreen.Companion.onComposeScreen
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.BeforeClass
import org.junit.ClassRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import presentation.screens.KaspressoChannelsScreen
import presentation.screens.KaspressoChatScreen
import presentation.screens.KaspressoReactionDialogScreen
import presentation.screens.TopicLazyListItemNode
import presentation.util.MapDispatcher
import presentation.util.MessageMockRequestDispatcher
import presentation.util.MockRequestDispatcher
import presentation.util.ReactionMockRequestDispatcher
import presentation.util.loadFromAssets

@RunWith(AndroidJUnit4::class)
class ChatTest : TestCase(kaspressoBuilder = Kaspresso.Builder.withComposeSupport()) {

    companion object {

        private const val BASE_DELAY = 500L
        private const val LONGER_DELAY = 1500L

        @get:ClassRule
        @JvmField
        val mockServer = MockWebServer()

        @BeforeClass
        @JvmStatic
        fun setupClass() {
            ApplicationProvider.getApplicationContext<Context>().getAppComponent().apply {
                apiUrlProvider().url = mockServer.url("/").toString()
            }
        }
    }

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @After
    fun tearDown() {
        mockServer.shutdown()
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun openFirstChatTest() = run {

        mockServer.dispatcher = MockRequestDispatcher().apply {
            baseMockConfig()
        }

        step("Стандартная навигация до экрана чата") {
            onComposeScreen<KaspressoChannelsScreen>(composeRule) {
                topicList {
                    firstChild<TopicLazyListItemNode> {
                        performClick()
                    }
                }
            }

            composeRule.delay(BASE_DELAY)

            onComposeScreen<KaspressoChannelsScreen>(composeRule) {
                topicList {
                    childAt<TopicLazyListItemNode>(1) {
                        performClick()
                    }
                }
            }
        }

        composeRule.delay(BASE_DELAY)

        step("Проверка отображения сообщений"){
            onComposeScreen<KaspressoChatScreen>(composeRule) {
                messageList.assertIsDisplayed()
            }
        }

    }

    @Test
    fun checkFragmentArgumentsTest() = run {

        mockServer.dispatcher = MockRequestDispatcher().apply {
            baseMockConfig()
        }

        val fragmentArgs = Bundle().apply {
            putParcelable(TOPIC_ID,
                TopicId("123", "TestName")
            )
        }
        val scenario = FragmentScenario.launchInContainer(ChatFragment::class.java, fragmentArgs)

        scenario.onFragment { fragment ->
            val args = fragment.arguments
            val topicId = args?.getParcelable<TopicId>(TOPIC_ID)
            assertNotNull(topicId)
            assertEquals("123", topicId?.streamId)
            assertEquals("TestName", topicId?.topicName)
        }
    }

    @Test
    fun gettingEmptyMessageList() = run {

        mockServer.dispatcher = MockRequestDispatcher().apply {
            baseMockConfig()
            returnsForPath("/messages") { setBody(loadFromAssets("empty_message_list.json")) }
        }

        launchInChatScreen()

        val expectedSize = 0
        val nodes = composeRule.onAllNodesWithTag(ChatFragment.chatItemTestTag)
        nodes.assertCountEquals(expectedSize)
    }

    @Test
    fun channelTitleDisplayTest() = run {
        mockServer.dispatcher = MockRequestDispatcher().apply {
            baseMockConfig()
        }

        launchInChatScreen()

        val node = composeRule.onNodeWithTag(ChatFragment.titleTestTag)
        node.assertIsDisplayed()
    }

    @Test
    fun sendMessageTest() = run {

        mockServer.dispatcher = MessageMockRequestDispatcher().apply {
            baseMockConfig()
        }

        launchInChatScreen()

        val messageText = "Test message!"

        step("Написание и отправка сообщения") {
            onComposeScreen<KaspressoChatScreen>(composeRule) {
                composeRule.onNodeWithTag(ChatFragment.messageInputFieldTestTag)
                    .performTextInput(messageText)
                composeRule.onNodeWithTag(ChatFragment.messageSendButtonTestTag).performClick()
            }
        }


        composeRule.delay(LONGER_DELAY)

        onComposeScreen<KaspressoChatScreen>(composeRule) {
            composeRule.onNodeWithTag(ChatFragment.messageListTestTag)
                .performScrollToIndex(0)
        }

        step("Поиск нового сообщения в списке и проверка совпадения текста") {
            val nodes = composeRule
                .onAllNodesWithTag(ChatFragment.chatItemTestTag)

            assert(
                nodes.fetchSemanticsNodes().isNotEmpty()
            ) { "Не найдено ни одного элемента с указанным тегом" }

            val firstNode = nodes.fetchSemanticsNodes().first()

            val messageTextFromCompose =
                firstNode.config.getOrNull(SemanticsProperties.ContentDescription)
                    ?.joinToString { it }

            assert(!messageTextFromCompose.isNullOrEmpty()) { "Message text is empty or null" }

            assert(messageTextFromCompose == messageText) { "Текст сообщения не совпадает: " +
                    "$messageTextFromCompose != $messageText" }
        }
    }

    @Test
    fun openReactionDialogTest() = run {

        mockServer.dispatcher = ReactionMockRequestDispatcher().apply {
            baseMockConfig()
        }

        launchInChatScreen()

        step("Открытие диалога реакций") {
            composeRule.onAllNodesWithTag(ChatFragment.chatItemTestTag).onFirst()
                .performSemanticsAction(SemanticsActions.OnLongClick)
        }

        composeRule.delay(LONGER_DELAY)

        step("Проверка открытия диалога реакций") {
            onComposeScreen<KaspressoReactionDialogScreen>(composeRule) {
                reactionList.assertIsDisplayed()
            }
        }

    }

    @Test
    fun addReactionTest() = run {
        mockServer.dispatcher = ReactionMockRequestDispatcher().apply {
            baseMockConfig()
        }

        launchInChatScreen()

        var oldReactionNumber: Int? = 0

        step("Фиксирование изначального кол-ва реакций") {
            val nodes = composeRule.onAllNodesWithTag(ChatFragment.chatItemTestTag)
            val firstNode = nodes.fetchSemanticsNodes().first()

            val textFromCompose =
                firstNode.config.getOrNull(SemanticsProperties.ContentDescription)
                    ?.joinToString { it }

            oldReactionNumber = textFromCompose?.substringAfter("?count=")?.toIntOrNull()
        }

        step("Открытие диалога реакций") {
            composeRule.onAllNodesWithTag(ChatFragment.chatItemTestTag).onFirst()
                .performSemanticsAction(SemanticsActions.OnLongClick)

            composeRule.delay(BASE_DELAY)

            onComposeScreen<KaspressoReactionDialogScreen>(composeRule) {
                reactionList.assertIsDisplayed()
            }
        }

        step("Добавление реакции") {
            composeRule.onAllNodesWithTag(ChatFragment.reactionTestTag).onFirst().performClick()
            (mockServer.dispatcher as ReactionMockRequestDispatcher).addReaction()
        }

        composeRule.delay(LONGER_DELAY)

        step("Проверка количества реакций") {
            val newNodes = composeRule.onAllNodesWithTag(ChatFragment.chatItemTestTag)
            val newFirstNode = newNodes.fetchSemanticsNodes().first()

            val newTextFromCompose =
                newFirstNode.config.getOrNull(SemanticsProperties.ContentDescription)
                    ?.joinToString { it }

            val newReactionNumber = newTextFromCompose?.substringAfter("?count=")?.toInt()

            oldReactionNumber?.let {
                assert(newReactionNumber == it + 1)
            }
        }
    }


    @Test
    fun deleteReactionTest() = run {
        mockServer.dispatcher = ReactionMockRequestDispatcher().apply {
            baseMockConfig()
        }

        launchInChatScreen()

        var oldReactionNumber: Int? = 0

        step("Фиксирование изначального кол-ва реакций") {
            val nodes = composeRule.onAllNodesWithTag(ChatFragment.chatItemTestTag)
            val secondNode = nodes.fetchSemanticsNodes()[2]

            val textFromCompose =
                secondNode.config.getOrNull(SemanticsProperties.ContentDescription)
                    ?.joinToString { it }

            oldReactionNumber = textFromCompose?.substringAfter("?count=")?.toIntOrNull()

        }

        step("Открытие диалога реакций") {
            composeRule.onAllNodesWithTag(ChatFragment.chatItemTestTag).onFirst()
                .performSemanticsAction(SemanticsActions.OnLongClick)

            composeRule.delay(BASE_DELAY)

            onComposeScreen<KaspressoReactionDialogScreen>(composeRule) {
                reactionList.assertIsDisplayed()
            }
        }

        composeRule.delay(BASE_DELAY)

        step("Удаление реакции") {
            composeRule.onAllNodesWithTag(ChatFragment.reactionTestTag).onFirst().performClick()
            (mockServer.dispatcher as ReactionMockRequestDispatcher).deleteReaction()
        }

        composeRule.delay(LONGER_DELAY)

        step("Проверка количества реакций") {
            val newNodes = composeRule.onAllNodesWithTag(ChatFragment.chatItemTestTag)
            val newSecondNode = newNodes.fetchSemanticsNodes()[2]

            val newTextFromCompose =
                newSecondNode.config.getOrNull(SemanticsProperties.ContentDescription)
                    ?.joinToString { it }

            val newReactionNumber = newTextFromCompose?.substringAfter("?count=")?.toInt()

            oldReactionNumber?.let {
                assert(newReactionNumber == it - 1)
            }
        }
    }

    private fun MapDispatcher.baseMockConfig() {
        returnsForPath("/messages") { setBody(loadFromAssets("messages_list.json")) }
        returnsForPath("/register") { setBody(loadFromAssets("register_response.json")) }
        returnsForPath("/events") { setBody(loadFromAssets("empty_event_list.json")) }
        returnsForPath("/streams") { setBody(loadFromAssets("streams.json")) }
        returnsForPath("/users/me/subscriptions") { setBody(loadFromAssets("subscribed_channels.json")) }
        returnsForPath("/streams/432915") { setBody(loadFromAssets("stream.json")) }
        returnsForPath("/users/me/432915/topics") { setBody(loadFromAssets("stream_topics.json")) }
        returnsForPath("/users") { setBody(loadFromAssets("users.json")) }
        returnsForPath("/users/me") { setBody(loadFromAssets("own_user.json")) }
        returnsForPath("/realm/presence") { setBody(loadFromAssets("all_presences.json")) }
        returnsForPath("/users/708884/presence") { setBody(loadFromAssets("user_presence.json")) }
    }

    private fun launchInChatScreen() {
        composeRule.activity.runOnUiThread {
            val fragment = ChatFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(TOPIC_ID,
                        TopicId("123", "TestName")
                    )
                }
            }
            composeRule.activity.supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_view, fragment)
                .commitNow()
        }
    }

    private fun ComposeTestRule.delay(millis: Long) {
        val startTime = System.nanoTime()
        while (true) {
            if (mainClock.autoAdvance) {
                mainClock.advanceTimeByFrame()
            }
            Thread.sleep(10)
            if (System.nanoTime() - startTime > millis * 1_000_000L) {
                break
            }
        }
    }
}
