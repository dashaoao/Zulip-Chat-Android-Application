package com.example.coursework.navbar.model

import com.example.common.ui.R

sealed class NavBarRoute(
    val route: String,
    val iconResId: Int,
    val titleResId: Int,
) {
    data object Channels : NavBarRoute(
        route = "channels",
        iconResId = R.drawable.ic_channels,
        titleResId = R.string.channels
    )

    data object People : NavBarRoute(
        route = "people",
        iconResId = R.drawable.ic_people,
        titleResId = R.string.people
    )

    data object Profile : NavBarRoute(
        route = "profile",
        iconResId = R.drawable.ic_profile,
        titleResId = R.string.profile
    )
}

sealed class NavBarEvent {

    sealed class UI {
        object OpenDefaultTab : NavBarEvent()
        class NavBarNavClick(val route: NavBarRoute) : NavBarEvent()
    }

    sealed class Internal {
        object SwitchedToChannels : NavBarEvent()
        object SwitchedToPeople : NavBarEvent()
        object SwitchedToProfile : NavBarEvent()
    }
}

sealed class NavBarEffect {

}

sealed class NavBarCommand {
    object OpenDefaultTab : NavBarCommand()
    class SwitchTab(val route: NavBarRoute) : NavBarCommand()
}
