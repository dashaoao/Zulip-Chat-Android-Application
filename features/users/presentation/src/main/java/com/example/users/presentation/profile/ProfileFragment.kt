package com.example.users.presentation.profile

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
import com.example.users.presentation.profile.model.ProfileEffect
import com.example.users.presentation.profile.model.ProfileEvent
import com.example.users.presentation.profile.model.ProfileUIState
import com.example.users.presentation.profile.ui.ProfileLayout
import vivid.money.elmslie.android.base.ElmFragment
import vivid.money.elmslie.android.storeholder.LifecycleAwareStoreHolder
import javax.inject.Inject


class ProfileFragment() :
    ElmFragment<ProfileEvent, ProfileEffect, ProfileUIState>() {

    override val initEvent: ProfileEvent = ProfileEvent.UI.Initialize

    private val userId by lazy { requireArguments().getString(USER_ID) }

    @Inject
    lateinit var factory: ProfileStoreFactory.Factory
    override val storeHolder: LifecycleAwareStoreHolder<ProfileEvent, ProfileEffect, ProfileUIState> =
        LifecycleAwareStoreHolder(lifecycle) { factory.create(userId).create() }

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
                    ProfileLayout(
                        state = state,
                        effect = effect,
                        onArrowBackClick = { store.accept(ProfileEvent.UI.OnArrowBackClick) }
                    )
                }
            }
        }
    }

    override fun render(state: ProfileUIState) = Unit

    companion object {
        private const val USER_ID = "userId"
        fun newInstance(userId: String): ProfileFragment {
            return ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(USER_ID, userId)
                }
            }
        }
    }
}
