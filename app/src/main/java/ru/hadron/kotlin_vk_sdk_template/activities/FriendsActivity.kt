package ru.hadron.kotlin_vk_sdk_template.activities

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.rahatarmanahmed.cpv.CircularProgressView
import moxy.MvpAppCompatActivity
import moxy.presenter.InjectPresenter
import ru.hadron.kotlin_vk_sdk_template.R
import ru.hadron.kotlin_vk_sdk_template.adapters.FriendsAdapter
import ru.hadron.kotlin_vk_sdk_template.models.FriendModel
import ru.hadron.kotlin_vk_sdk_template.presenters.FriendsPresenter
import ru.hadron.kotlin_vk_sdk_template.views.FriendsView

class FriendsActivity : MvpAppCompatActivity(), FriendsView {

    private lateinit var mAdapter: FriendsAdapter

    @InjectPresenter
    lateinit var friendsPresenter: FriendsPresenter

    private lateinit var mRvFriends: RecyclerView
    private lateinit var mTxtNoItems: TextView
    private lateinit var cpvWait: CircularProgressView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friends)
        //подключаем ресурсы
        mRvFriends = findViewById(R.id.recycler_friends)
        mTxtNoItems = findViewById(R.id.txt_friends_no_items)
        cpvWait = findViewById(R.id.cpv_friends)
        //подключаем EditText, вешаем listener
        val mTxtSearch: EditText = findViewById(R.id.txt_friends_search)
        mTxtSearch.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                mAdapter.filter(query = s.toString())
            }
        })
//
        friendsPresenter.loadFriends()
        mAdapter = FriendsAdapter()
        mRvFriends.adapter = mAdapter
        mRvFriends.layoutManager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
        mRvFriends.setHasFixedSize(true)
    }

   /*FriendsView implementation*/
    override fun showError(textResource: Int) {
        //использовать файловые строки удобно для локализации
        this.mTxtNoItems.text  = getString(textResource)
    }

    override fun setupEmptyList() {
        mRvFriends.visibility = View.GONE
        mTxtNoItems.visibility = View.VISIBLE
    }

    override fun setupFriendsList(friendsList: ArrayList<FriendModel>) {
        mRvFriends.visibility = View.VISIBLE
        mTxtNoItems.visibility = View.GONE

        mAdapter.setupFriends(friendList = friendsList)
    }

    override fun startLoading() {
        mRvFriends.visibility = View.GONE
        mTxtNoItems.visibility = View.GONE
        cpvWait.visibility = View.VISIBLE
    }

    override fun endLoading() {
        cpvWait.visibility = View.GONE
    }
}