package ru.hadron.kotlin_vk_sdk_template.helpers
/*
обработка невалидного access token
файл добавлен в манифест, иначе игнорируется тк нигде не вызывается
 */
import android.app.Application
import com.vk.sdk.VKSdk

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        VKSdk.initialize(applicationContext)
    }
}
