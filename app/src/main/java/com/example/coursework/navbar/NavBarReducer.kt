package com.example.coursework.navbar

import com.example.coursework.navbar.model.NavBarCommand
import com.example.coursework.navbar.model.NavBarEffect
import com.example.coursework.navbar.model.NavBarEvent
import com.example.coursework.navbar.model.NavBarRoute
import vivid.money.elmslie.core.store.Result
import vivid.money.elmslie.core.store.StateReducer

val NavBarReducer: StateReducer<NavBarEvent, NavBarRoute, NavBarEffect, NavBarCommand> =
    StateReducer { event: NavBarEvent, state : NavBarRoute ->
        when(event) {
            is NavBarEvent.UI.NavBarNavClick -> {
                Result(
                    state = state,
                    command = NavBarCommand.SwitchTab(event.route)
                )
            }
            NavBarEvent.UI.OpenDefaultTab -> {
                Result(
                    state = NavBarRoute.Channels,
                    command = NavBarCommand.OpenDefaultTab
                )
            }
            NavBarEvent.Internal.SwitchedToChannels -> {
                Result(
                    state = NavBarRoute.Channels
                )
            }
            NavBarEvent.Internal.SwitchedToPeople -> {
                Result(
                    state = NavBarRoute.People
                )
            }
            NavBarEvent.Internal.SwitchedToProfile -> {
                Result(
                    state = NavBarRoute.Profile
                )
            }
        }
}
