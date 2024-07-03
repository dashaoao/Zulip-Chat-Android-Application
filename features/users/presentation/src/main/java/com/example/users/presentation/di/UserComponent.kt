package com.example.users.presentation.di

import com.example.users.presentation.own_profile.OwnProfileFragment
import com.example.users.presentation.people.PeopleFragment
import com.example.users.presentation.profile.ProfileFragment
import dagger.Component

@UserScope
@Component(
    dependencies = [UserPresentationDeps::class]
)
interface UserComponent {
    fun inject(fragment: PeopleFragment)
    fun inject(fragment: ProfileFragment)
    fun inject(fragment: OwnProfileFragment)

    @Component.Factory
    interface Factory {
        fun create(userPresentationDeps: UserPresentationDeps): UserComponent
    }
}
