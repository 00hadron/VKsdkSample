package ru.hadron.kotlin_vk_sdk_template.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.github.rahatarmanahmed.cpv.CircularProgressView
import com.vk.sdk.VKScope
import com.vk.sdk.VKSdk
import moxy.MvpAppCompatActivity
import moxy.presenter.InjectPresenter
import ru.hadron.kotlin_vk_sdk_template.R
import ru.hadron.kotlin_vk_sdk_template.presenters.LoginPresenter
import ru.hadron.kotlin_vk_sdk_template.views.LoginView

class LoginActivity : MvpAppCompatActivity(), LoginView {
    // private val TAG: String = LoginActivity::class.java.simpleName
    private lateinit var mTxtHello: TextView
    private lateinit var mBtnEnter: Button
    private lateinit var mCpvWait: CircularProgressView

    /*подключить presenter*/
    @InjectPresenter
    lateinit var loginPresenter: LoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        //Подтянуть визуальные компоненты в activity
        this.mTxtHello = findViewById(R.id.txt_login_hello)
        this.mBtnEnter = findViewById(R.id.btn_login_enter)
        this.mCpvWait = findViewById(R.id.cpv_login)
        //presenter начинает отрабатывать момент нажатия на кнопку
        this.mBtnEnter.setOnClickListener{
            VKSdk.login(this@LoginActivity, VKScope.FRIENDS)
            /*  this.loginPresenter.login(isSuccess = true)
              //true - симуляция, что открыли*/
        }
        /*sdk vk отпечаток
        val fingerprints = VKUtil.getCertificateFingerprint(this, this.packageName)
        Log.e(TAG, "fingerprint ${fingerprints[0]}")*/
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (!loginPresenter.loginVk(requestCode = requestCode, resultCode = resultCode, data = data)) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun startLoading() {
        this.mBtnEnter.visibility = View.GONE
        this.mCpvWait.visibility = View.VISIBLE
    }

    override fun endLoading() {
        this.mBtnEnter.visibility = View.VISIBLE
        this.mCpvWait.visibility = View.GONE
    }

    override fun showError(textResource: Int) {
        Toast.makeText(applicationContext, getString(textResource), Toast.LENGTH_SHORT).show()
    }

    override fun openFriends() {
        startActivity(Intent(applicationContext, FriendsActivity::class.java))
    }
}
