package ru.hadron.kotlin_vk_sdk_template.views

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType
import ru.hadron.kotlin_vk_sdk_template.models.FriendModel

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface FriendsView: MvpView {

    /*параметр int, а не string, т.к. передаем не строку, а контекст - ссылку на ресурс*/
    fun showError(textResource: Int)
    fun setupEmptyList()
    fun setupFriendsList(friendsList: ArrayList<FriendModel>)
    fun startLoading()
    fun endLoading()
}