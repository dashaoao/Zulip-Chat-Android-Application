package com.example.users.presentation.di

import androidx.lifecycle.ViewModel
import kotlin.properties.Delegates.notNull

internal class UsersComponentViewModel : ViewModel() {
    val usersComponent: UserComponent = DaggerUserComponent.factory().create(UsersDepsProvider.deps)
}

interface UsersDepsProvider {
    val deps: UserPresentationDeps

    companion object : UsersDepsProvider by UsersDepsStore
}

object UsersDepsStore : UsersDepsProvider {
    override var deps: UserPresentationDeps by notNull()
}
