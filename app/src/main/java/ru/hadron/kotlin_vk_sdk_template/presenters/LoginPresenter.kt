package ru.hadron.kotlin_vk_sdk_template.presenters

import android.content.Intent
import android.os.Handler
import com.vk.sdk.VKAccessToken
import com.vk.sdk.VKCallback
import com.vk.sdk.VKSdk
import com.vk.sdk.api.VKError
import moxy.InjectViewState
import moxy.MvpPresenter
import ru.hadron.kotlin_vk_sdk_template.R
import ru.hadron.kotlin_vk_sdk_template.views.LoginView

@InjectViewState
class LoginPresenter: MvpPresenter<LoginView>() {
    fun login(isSuccess: Boolean) {
        viewState.startLoading()
        /*симулируем отдачу логина без сервера*/
        Handler().postDelayed({
            viewState.endLoading()
            if (isSuccess) {
                viewState.openFriends()
            } else {
                viewState.showError(textResource = R.string.login_error_credentials)
            }
        }, 500)

    }

    fun loginVk(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, object :
                VKCallback<VKAccessToken> {
                override fun onResult(res: VKAccessToken?) {
                    viewState.openFriends()
                }

                override fun onError(error: VKError?) {
                    viewState.showError(textResource = R.string.login_error_credentials)
                }
            })) {
            return false
        }

        return true
    }
}