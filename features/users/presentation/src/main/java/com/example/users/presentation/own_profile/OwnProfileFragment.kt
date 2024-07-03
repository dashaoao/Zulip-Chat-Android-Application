package com.example.users.presentation.own_profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.example.common.ui.effect
import com.example.common.ui.state
import com.example.common.ui.theme.ChatTheme
import com.example.users.presentation.di.UsersComponentViewModel
import com.example.users.presentation.own_profile.model.OwnProfileEffect
import com.example.users.presentation.own_profile.model.OwnProfileEvent
import com.example.users.presentation.own_profile.model.OwnProfileUIState
import com.example.users.presentation.own_profile.ui.OwnProfileLayout
import vivid.money.elmslie.android.base.ElmFragment
import vivid.money.elmslie.android.storeholder.LifecycleAwareStoreHolder
import javax.inject.Inject

class OwnProfileFragment : ElmFragment<OwnProfileEvent, OwnProfileEffect, OwnProfileUIState>() {

    override val initEvent: OwnProfileEvent = OwnProfileEvent.UI.Initialize

    @Inject
    lateinit var factory: OwnProfileStoreFactory
    override val storeHolder = LifecycleAwareStoreHolder(lifecycle) { factory.create() }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        ViewModelProvider(this).get<UsersComponentViewModel>().usersComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                ChatTheme {
                    val state by store.state()
                    val effect by store.effect()
                    OwnProfileLayout(
                        state = state,
                        effect = effect,
                    )
                }
            }
        }
    }

    override fun render(state: OwnProfileUIState) = Unit
}
