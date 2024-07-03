package presentation.util

import okhttp3.mockwebserver.MockResponse

interface MapDispatcher {
    fun returnsForPath(path: String, response: MockResponse.() -> MockResponse)
}
