package com.example.channels.presentation.new_channel.di

import com.example.channels.presentation.new_channel.NewChannelViewModel
import dagger.Component

@Component(dependencies = [NewChannelDeps::class])
@NewChannelScope
interface NewChannelComponent {

   @Component.Factory
   interface Factory {
       fun create(newChannelDeps: NewChannelDeps): NewChannelComponent
   }

   fun getViewModel() : NewChannelViewModel
}
