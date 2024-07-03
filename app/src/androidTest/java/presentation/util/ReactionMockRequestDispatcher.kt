package presentation.util

import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest

class ReactionMockRequestDispatcher : Dispatcher(), MapDispatcher {

    private val responses: MutableMap<String, MockResponse> = mutableMapOf()
    private var reactionAdded = false
    private var reactionDeleted = false

    override fun dispatch(request: RecordedRequest): MockResponse {
        val path = request.path?.substringBefore("?")
        return when (path) {
            "/messages" -> {
                if (reactionAdded) {
                    MockResponse().setBody(loadFromAssets("messages_list_with_reactions.json"))
                }
                else if (reactionDeleted){
                    MockResponse().setBody(loadFromAssets("messages_list_without_reactions.json"))
                }
                else {
                    MockResponse().setBody(loadFromAssets("messages_list.json"))
                }
            }

            "/events" -> {
                if (reactionAdded) {
                    MockResponse().setBody(loadFromAssets("add_reaction_event_list.json"))
                }
                else if (reactionDeleted){
                    MockResponse().setBody(loadFromAssets("delete_reaction_event_list.json"))
                }
                else {
                    MockResponse().setBody(loadFromAssets("empty_event_list.json"))
                }
            }

            else -> responses[path] ?: MockResponse().setResponseCode(404)
        }
    }

    fun addReaction() {
        reactionAdded = true
    }

    fun deleteReaction() {
        reactionDeleted = true
    }

    override fun returnsForPath(path: String, response: MockResponse.() -> MockResponse) {
        responses[path] = response(MockResponse())
    }
}

