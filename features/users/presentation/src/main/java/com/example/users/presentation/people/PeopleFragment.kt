package com.example.users.presentation.people

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.lifecycle.viewModelScope
import com.example.common.ui.effect
import com.example.common.ui.state
import com.example.common.ui.theme.ChatTheme
import com.example.users.presentation.di.UsersComponentViewModel
import com.example.users.presentation.people.model.PeopleEffect
import com.example.users.presentation.people.model.PeopleEvent
import com.example.users.presentation.people.model.PeopleUIState
import com.example.users.presentation.people.ui.PeopleLayout
import vivid.money.elmslie.android.base.ElmFragment
import vivid.money.elmslie.android.storeholder.LifecycleAwareStoreHolder
import javax.inject.Inject

class PeopleFragment : ElmFragment<PeopleEvent, PeopleEffect, PeopleUIState>() {

    override val initEvent: PeopleEvent = PeopleEvent.UI.Initialize
    private lateinit var componentViewModel: UsersComponentViewModel

    @Inject
    lateinit var factory: PeopleStoreFactory.Factory
    override val storeHolder = LifecycleAwareStoreHolder(lifecycle) {
        factory.create(componentViewModel.viewModelScope).create()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        componentViewModel = ViewModelProvider(this).get<UsersComponentViewModel>()
        componentViewModel.usersComponent.inject(this)
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
                    PeopleLayout(
                        state = state,
                        effect = effect,
                        onUserClick = { store.accept(PeopleEvent.UI.UserClick(it)) },
                        performSearch = { store.accept(PeopleEvent.UI.SearchTextChanged(it)) }
                    )
                }
            }
        }
    }

    override fun render(state: PeopleUIState) = Unit
}
