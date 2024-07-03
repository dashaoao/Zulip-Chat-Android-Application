package com.example.coursework.navbar

import com.example.coursework.di.navbar.BottomNavigation
import com.example.coursework.navbar.model.NavBarRoute
import com.github.terrakok.cicerone.Router
import vivid.money.elmslie.coroutines.ElmStoreCompat
import javax.inject.Inject

class NavBarStoreFactory @Inject constructor(
    @BottomNavigation private val router: Router
) {
    @Suppress("UNCHECKED_CAST")
    fun create() = ElmStoreCompat(
        initialState = NavBarRoute.Channels,
        reducer = NavBarReducer,
        actor = NavBarActor(
            router = router,
        )
    )
}
