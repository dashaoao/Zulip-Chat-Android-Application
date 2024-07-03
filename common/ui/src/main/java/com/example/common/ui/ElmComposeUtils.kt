package com.example.common.ui

import androidx.compose.runtime.Composable
import vivid.money.elmslie.compose.EffectWithKey
import vivid.money.elmslie.compose.util.subscribeAsState
import vivid.money.elmslie.core.store.Store

@Composable
fun <Event, Effect, State> Store<Event, Effect, State>.state() =
    ::states.subscribeAsState(initial = currentState)

@Composable
fun <Event, Effect, State> Store<Event, Effect, State>.effect() =
    ::effects.subscribeAsState(::EffectWithKey, initial = null)
