package com.example.coursework.di.app

import com.example.common.data.ApiUrlProvider
import com.example.coursework.auth.AuthHeaderInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideApiUrlProvider(): ApiUrlProvider = ApiUrlProvider.ApiUrlProviderImpl()

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        apiUrlProvider: com.example.common.data.ApiUrlProvider
    ): Retrofit = Retrofit
        .Builder()
        .baseUrl(apiUrlProvider.url)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Singleton
    @Provides
    fun provideHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        authHeaderInterceptor: AuthHeaderInterceptor
    ): OkHttpClient = OkHttpClient
        .Builder()
        .addInterceptor(authHeaderInterceptor)
        .readTimeout(5, TimeUnit.DAYS)
        .writeTimeout(5, TimeUnit.DAYS)
        .connectTimeout(5, TimeUnit.DAYS)
        .addInterceptor(httpLoggingInterceptor)
        .build()

    @Singleton
    @Provides
    fun provideChatApi(retrofit: Retrofit): com.example.chat.data.api.ChatApi = retrofit.create(com.example.chat.data.api.ChatApi::class.java)

    @Singleton
    @Provides
    fun provideChannelsApi(retrofit: Retrofit): com.example.channels.data.api.ChannelsApi =
        retrofit.create(com.example.channels.data.api.ChannelsApi::class.java)

    @Singleton
    @Provides
    fun provideEventsApi(retrofit: Retrofit): com.example.common.data.events.EventsApi = retrofit.create(
        com.example.common.data.events.EventsApi::class.java)

    @Singleton
    @Provides
    fun provideUsersApi(retrofit: Retrofit): com.example.users.data.api.UsersApi = retrofit.create(
        com.example.users.data.api.UsersApi::class.java)

}
