package ru.hadron.kotlin_vk_sdk_template.presenters

import moxy.InjectViewState
import moxy.MvpPresenter
import ru.hadron.kotlin_vk_sdk_template.R
import ru.hadron.kotlin_vk_sdk_template.models.FriendModel
import ru.hadron.kotlin_vk_sdk_template.providers.FriendsProvider
import ru.hadron.kotlin_vk_sdk_template.views.FriendsView

@InjectViewState
class FriendsPresenter: MvpPresenter<FriendsView>() {
    fun loadFriends() {
        viewState.startLoading()
       /* при отладке без сервера
          1. отработать сценарий false
          2. отработать сценарий true
          FriendsProvider(presenter = this).testLoadFriends(hasFriends = true)*/
        FriendsProvider(presenter = this).loadFriends()
    }

    fun friendsLoaded(friendsList: ArrayList<FriendModel>) {
        viewState.endLoading()
        if (friendsList.size == 0) {
            viewState.setupEmptyList()
            viewState.showError(textResource = R.string.friends_no_item)
        } else {
            viewState.setupFriendsList(friendsList = friendsList)
        }
    }

    fun showError(textResource: Int) {
        viewState.showError(textResource = textResource)
    }
}