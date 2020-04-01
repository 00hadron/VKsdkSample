package ru.hadron.kotlin_vk_sdk_template.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import ru.hadron.kotlin_vk_sdk_template.R
import ru.hadron.kotlin_vk_sdk_template.models.FriendModel

class FriendsAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    /*массив, который будет являться source для данных (а менять при ПОИСКЕ будем mfriendlist)*/
    private var mSourceList: ArrayList<FriendModel> = ArrayList()
    private var mFriendList: ArrayList<FriendModel> = ArrayList()

    fun setupFriends(friendList: ArrayList<FriendModel>) {
        /*обновление данных с условного серверв*/
        mSourceList.clear()
        mSourceList.addAll(friendList)
       /*фильтр для пустого запроса на момент сетапа. Эквивалентно показать свех друзей*/
        this.filter(query = "")
    }
    /*
    очищается френдслист, в него закидываются нужные объекты. Поиск по сорслист, который не изменяется.
    */
    fun filter(query: String) {
        mFriendList.clear()
        mSourceList.forEach {
            if (it.name.contains(query, ignoreCase = true) || it.surname.contains(query, ignoreCase = true)) {
                mFriendList.add(it)
            } else {
                it.city?.let { city ->
                    if (city.contains(query, ignoreCase = true)) {
                        mFriendList.add(it)
                    }
                }
            }
        }
        /*чтобы адаптер узнал, что пришли новые данные, обновить*/
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemView = layoutInflater.inflate(R.layout.cell_friend, parent, false)
        return FriendsViewHolder(itemView = itemView)
    }

    override fun getItemCount(): Int {
        return this.mFriendList.count()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is FriendsViewHolder) {
            holder.bind(friendModel = mFriendList[position])
        }
    }

    class FriendsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private var mCivAvatar: CircleImageView = itemView.findViewById(R.id.friend_civ_avatar)
        private var mTxtUserName: TextView = itemView.findViewById(R.id.friend_txt_username)
        private var mTxtCity: TextView = itemView.findViewById(R.id.friend_txt_city)
        private var mImgOnline: View = itemView.findViewById(R.id.friend_img_online)

      /*помещаем данные в ячейку*/
        @SuppressLint("SetTextI18n")
        fun bind(friendModel: FriendModel) {
            /*Загрузить фото, если аватар не null*/
            friendModel.avatar?.let { url ->
                Picasso.get().load(url)
                    .into(mCivAvatar)
            }
            mTxtUserName.text = "${friendModel.name} ${friendModel.surname}"
            mTxtCity.text = itemView.context.getString(R.string.friend_no_city)
            /*проверка на null. Будет присвоено значение, если оно есть.
            friendModel.city?.let{mTxtCity.text = it}*/
            friendModel.city?.let {city -> mTxtCity.text = city}
            if (friendModel.isOnline) {
                mImgOnline.visibility = View.VISIBLE
            } else {
                mImgOnline.visibility = View.GONE
            }
        }
    }
}