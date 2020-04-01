package ru.hadron.kotlin_vk_sdk_template.providers

import android.os.Handler
import com.google.gson.JsonParser
import com.vk.sdk.api.*
import ru.hadron.kotlin_vk_sdk_template.R
import ru.hadron.kotlin_vk_sdk_template.models.FriendModel
import ru.hadron.kotlin_vk_sdk_template.presenters.FriendsPresenter
import java.util.*


class FriendsProvider(var presenter: FriendsPresenter) {
    /*private val TAG: String = FriendsProvider::class.java.simpleName*/

    /*симуляция задержки при загрузке и друзей*/
    fun testLoadFriends(hasFriends: Boolean) {
        val friendsList: ArrayList<FriendModel> = ArrayList()
        Handler().postDelayed({ if (hasFriends) {
            val friend1 = FriendModel(
                name = "Ivan",
                surname = "Ivanov",
                city = null,
                avatar = "https://upload.wikimedia.org/wikipedia/commons/d/d1/CENA_LENIN_MORENO_%2816217113764%29_%28cropped%29.jpg",
                isOnline = true)
            val friend2 = FriendModel(
                name = "Alexey",
                surname = "Gladkov",
                city = "Tomsk",
                avatar = "https://pp.userapi.com/c837723/v837723005/5eca5/T7p2k_hYvqw.jpg",
                isOnline = false)
            val friend3 = FriendModel(
                name = "Sasha",
                surname = "Sashkov",
                city = "Lyga",
                avatar = null,
                isOnline = true)

            friendsList.add(friend1)
            friendsList.add(friend2)
            friendsList.add(friend3)
        }
            presenter.friendsLoaded(friendsList)
        }, 2000)

    }
    /*
    ничего не возвращает, просто вызывает в презенторе френдслоадед после того,
     как получит ответ от сервера
     */
    fun loadFriends(){
       /*Подготовка запросов
         Отправка запросов execute with listener*/
        val request = VKApi.friends().get(VKParameters.from(
            VKApiConst.COUNT, 300, VKApiConst.FIELDS, "sex, bdate, city, country, photo_100, online"))
        request.executeWithListener(object: VKRequest.VKRequestListener() {
            override fun onComplete(response: VKResponse) {
                super.onComplete(response)
                /*Log.e(TAG, "response ${response.json}")
                конвертировать из json  в библиотечный gson*/
                val jsonParser = JsonParser()
                val parsedJson = jsonParser.parse(response.json.toString()).asJsonObject
                /*Log.e(TAG, "parsed - ${parsedJson}")*/
                val friendsList: ArrayList<FriendModel> = ArrayList()

                /*пройтись по всем элементам массива items*/
                parsedJson.get("response").asJsonObject.getAsJsonArray("items").forEach {
                    /*Log.e(TAG, "element - $it")*/
                    val city = if (it.asJsonObject.get("city") == null) {
                        null
                    } else {
                        it.asJsonObject.get("city").asJsonObject.get("title").asString
                    }

                    val friend = FriendModel(
                        name = it.asJsonObject.get("first_name").asString,
                        surname = it.asJsonObject.get("last_name").asString,
                        city = city,
                        avatar = it.asJsonObject.get("photo_100").asString,
                        isOnline = it.asJsonObject.get("online").asInt == 1)
                    friendsList.add(friend)
                }
                presenter.friendsLoaded(friendsList = friendsList)
            }

            override fun onError(error: VKError?) {
                super.onError(error)
                presenter.showError(textResource = R.string.friends_error_loading)
            }
        })
    }
}

