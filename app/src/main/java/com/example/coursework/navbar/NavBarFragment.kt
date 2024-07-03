package com.example.coursework.navbar

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import com.example.common.ui.state
import com.example.common.ui.theme.ChatTheme
import com.example.coursework.R
import com.example.coursework.databinding.NavbarLayoutBinding
import com.example.coursework.di.navbar.BottomNavigation
import com.example.coursework.di.navbar.DaggerBottomNavigationComponent
import com.example.coursework.navbar.model.NavBarEffect
import com.example.coursework.navbar.model.NavBarEvent
import com.example.coursework.navbar.model.NavBarRoute
import com.example.coursework.navbar.ui.NavBar
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.androidx.AppNavigator
import vivid.money.elmslie.android.base.ElmFragment
import vivid.money.elmslie.android.storeholder.LifecycleAwareStoreHolder
import javax.inject.Inject

class NavBarFragment() : ElmFragment<NavBarEvent, NavBarEffect, NavBarRoute>() {

    override val initEvent: NavBarEvent = NavBarEvent.UI.OpenDefaultTab

    @Inject
    lateinit var factory: NavBarStoreFactory
    override val storeHolder = LifecycleAwareStoreHolder(lifecycle) { factory.create() }

    @Inject
    @BottomNavigation
    lateinit var navigatorHolder: NavigatorHolder

    private val navigator by lazy {
        AppNavigator(
            requireActivity(),
            R.id.main_container,
            childFragmentManager
        )
    }

    private var _binding: NavbarLayoutBinding? = null
    private val binding: NavbarLayoutBinding
        get() = _binding ?: throw RuntimeException("NavbarLayoutBinding = null")

    override fun onAttach(context: Context) {
        super.onAttach(context)
        DaggerBottomNavigationComponent.factory().create().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = NavbarLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.composeView.setContent {
            ChatTheme {
                val state by store.state()
                NavBar(
                    screens = listOf(NavBarRoute.Channels, NavBarRoute.People, NavBarRoute.Profile),
                    selected = state,
                    onClick = {
                        store.accept(NavBarEvent.UI.NavBarNavClick(it))
                    }
                )
            }
        }
    }

    override fun render(state: NavBarRoute) = Unit

    override fun onResume() {
        super.onResume()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }

}
