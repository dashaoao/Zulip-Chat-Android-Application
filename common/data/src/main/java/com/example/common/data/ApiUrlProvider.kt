package com.example.common.data

interface ApiUrlProvider {
    var url: String

    class ApiUrlProviderImpl : ApiUrlProvider {
        override var url: String = "https://tinkoff-android-spring-2024.zulipchat.com/api/v1/"
    }
}
