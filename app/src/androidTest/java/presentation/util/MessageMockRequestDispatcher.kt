package presentation.util

import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest

class MessageMockRequestDispatcher : Dispatcher(), MapDispatcher {

    private val responses: MutableMap<String, MockResponse> = mutableMapOf()
    private var messageSent = false

    override fun dispatch(request: RecordedRequest): MockResponse {
        val path = request.path?.substringBefore("?")
        return when (path) {
            "/messages" -> {
                if (request.method == "POST") {
                    messageSent = true
                    MockResponse().setBody(loadFromAssets("updated_messages.json"))
                } else {
                    if (messageSent) {
                        MockResponse().setBody(loadFromAssets("updated_messages.json"))
                    } else {
                        responses[path] ?: MockResponse().setResponseCode(404)
                    }
                }
            }
            "/events" -> {
                if (messageSent) {
                    MockResponse().setBody(loadFromAssets("message_event_list.json"))
                }
                else {
                    MockResponse().setBody(loadFromAssets("empty_event_list.json"))
                }
            }
            else -> responses[path] ?: MockResponse().setResponseCode(404)
        }
    }

    override fun returnsForPath(path: String, response: MockResponse.() -> MockResponse ) {
        responses[path] = response(MockResponse())
    }
}
