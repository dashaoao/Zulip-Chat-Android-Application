package com.example.coursework.navbar

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.coursework.Screens
import com.example.coursework.navbar.model.NavBarCommand
import com.example.coursework.navbar.model.NavBarEvent
import com.example.coursework.navbar.model.NavBarRoute
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import vivid.money.elmslie.coroutines.Actor

class NavBarActor(
    private val router: Router
) : Actor<NavBarCommand, NavBarEvent> {

    private val screens = listOf(NavBarRoute.Channels, NavBarRoute.People, NavBarRoute.Profile)
    private var selectedScreen by mutableStateOf(screens[0])

    override fun execute(command: NavBarCommand): Flow<NavBarEvent> {
        return when(command){
            NavBarCommand.OpenDefaultTab -> onNavBarClick(NavBarRoute.Channels)
            is NavBarCommand.SwitchTab -> onNavBarClick(command.route)
        }
    }

    private fun onNavBarClick(navBarRoute: NavBarRoute) : Flow<NavBarEvent> {
        return flow {
            when (navBarRoute) {
                NavBarRoute.Channels -> {
                    router.replaceScreen(Screens.Channels())
                    emit(NavBarEvent.Internal.SwitchedToChannels)
                }

                NavBarRoute.People -> {
                    router.replaceScreen(Screens.People())
                    emit(NavBarEvent.Internal.SwitchedToPeople)
                }
                NavBarRoute.Profile -> {
                    router.replaceScreen(Screens.OwnProfile())
                    emit(NavBarEvent.Internal.SwitchedToProfile)
                }
            }
            selectedScreen = navBarRoute
        }
    }
}
