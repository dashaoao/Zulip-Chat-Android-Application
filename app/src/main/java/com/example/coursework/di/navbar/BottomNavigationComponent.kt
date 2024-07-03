package com.example.coursework.di.navbar

import com.example.coursework.navbar.NavBarFragment
import dagger.Component

@BottomNavigationScope
@Component(
    modules = [BottomNavigationModule::class]
)
interface BottomNavigationComponent {

    fun inject(fragment: NavBarFragment)

    @Component.Factory
    interface Factory {
        fun create(): BottomNavigationComponent
    }
}
