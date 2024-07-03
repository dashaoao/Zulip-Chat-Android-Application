package com.example.coursework

import com.example.channels.presentation.ChannelsFragment
import com.example.chat.presentation.ChatFragment
import com.example.common.ui.TopicId
import com.example.coursework.navbar.NavBarFragment
import com.example.users.presentation.own_profile.OwnProfileFragment
import com.example.users.presentation.people.PeopleFragment
import com.example.users.presentation.profile.ProfileFragment
import com.github.terrakok.cicerone.androidx.FragmentScreen

object Screens {
    fun NavBar() = FragmentScreen() { NavBarFragment() }
    fun Channels() = FragmentScreen { ChannelsFragment() }
    fun Profile(userId: String) = FragmentScreen("Profile_$userId") { ProfileFragment.newInstance(userId) }
    fun OwnProfile() = FragmentScreen() { OwnProfileFragment() }
    fun People() = FragmentScreen() { PeopleFragment() }
    fun Chat(topicId: TopicId) = FragmentScreen("Profile_$topicId") { ChatFragment.newInstance(topicId) }
}
