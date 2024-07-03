package com.example.coursework

import com.example.channels.presentation.ChannelsNavigation
import com.example.chat.presentation.ChatNavigation
import com.example.common.ui.TopicId
import com.example.users.presentation.people.PeopleNavigation
import com.example.users.presentation.profile.ProfileNavigation
import com.github.terrakok.cicerone.Router
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRouter @Inject constructor() : Router(), ChannelsNavigation, PeopleNavigation,
    ChatNavigation, ProfileNavigation {
    override fun navigateToChat(topicId: TopicId) {
        this.navigateTo(Screens.Chat(topicId))
    }

    override fun navigateToProfile(userId: String) {
        this.navigateTo(Screens.Profile(userId))
    }

    override fun exitChat() {
        this.exit()
    }

    override fun exitProfile() {
        this.exit()
    }

}
