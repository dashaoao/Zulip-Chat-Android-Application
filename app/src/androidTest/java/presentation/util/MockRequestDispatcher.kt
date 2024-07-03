package presentation.util

import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest

class MockRequestDispatcher : Dispatcher(), MapDispatcher {

    private val responses: MutableMap<String, MockResponse> = mutableMapOf()

    override fun dispatch(request: RecordedRequest): MockResponse {
        val path = request.path
        return responses[path?.substringBefore("?")] ?: MockResponse().setResponseCode(404)
    }

    override fun returnsForPath(path: String, response: MockResponse.() -> MockResponse ) {
        responses[path] = response(MockResponse())
    }
}
