package com.example.coursework.auth

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthHeaderInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain
            .request()
            .newBuilder()
        val token = okhttp3.Credentials.basic("guchkovskaya.dasha@yandex.ru", "1cnGP1toLy0PjgqvSgu38VITqmahJPqJ")

        builder.addHeader("Authorization", token)
        return chain.proceed(builder.build())
    }
}
