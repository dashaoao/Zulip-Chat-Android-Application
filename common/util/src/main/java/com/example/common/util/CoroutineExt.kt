package com.example.common.util

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

inline fun <R> runCatchingNonCancellation(block: () -> R): Result<R> {
    return try {
        Result.success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        println("Exception ${e.message}")
        e.printStackTrace()
        Result.failure(e)
    }
}

fun <T> Flow<T>.catchNonCancellationAndEmit(onError: T): Flow<T> = flow {
    try {
        collect { value -> emit(value) }
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        println("Exception ${e.message}")
        e.printStackTrace()
        emit(onError)
    }
}
