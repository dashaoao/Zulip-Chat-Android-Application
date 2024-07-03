package com.example.common.core.result_state

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.coroutines.cancellation.CancellationException

typealias ResultFlow<T> = Flow<ResultState<T>>

inline fun <R> runCatchingState(block: () -> R): ResultState<R> {
    return try {
        ResultState.Success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        println("Exception ${e.message}")
        e.printStackTrace()
        ResultState.Error(e)
    }
}


fun <T> flowState(block: suspend () -> T) =
    flow {
        emit(ResultState.Loading())
        val response =
            try {
                ResultState.Success(data = block())
            } catch (e: Exception) {
                println("Exception in flowState ${e.message}")
                e.printStackTrace()
                ResultState.Error(e)
            }
        emit(response)
    }

inline fun <T> ResultState<T>.onLoading(action: () -> Unit): ResultState<T> {
    if (this is ResultState.Loading) action()
    return this
}

inline fun <T> ResultState<T>.onSuccess(action: (T) -> Unit): ResultState<T> {
    if (this is ResultState.Success) action(data)
    return this
}

inline fun <T> ResultState<T>.onError(action: (Exception, data: T?) -> Unit): ResultState<T> {
    if (this is ResultState.Error) action(error, data)
    return this
}

fun <T> T.success() = ResultState.Success(this)

inline fun <R, T> ResultState<T>.map(transform: (value: T) -> R): ResultState<R> {
    return when(this) {
        is ResultState.Success -> ResultState.Success(transform(data))
        is ResultState.Error -> ResultState.Error(error, data?.let(transform))
        is ResultState.Loading -> ResultState.Loading(data?.let(transform))
    }
}
