//package com.wordpuzzle.app.main.ui.play_game_screen.adapter
//
//import android.content.Context
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ArrayAdapter
//import android.widget.TextView
//import androidx.annotation.LayoutRes
//import com.wordpuzzle.app.R
//import com.wordpuzzle.app.commons.DurationFormatter.fromInteger
//import com.wordpuzzle.app.domain.model.GameRound
//
///**
// * Created by abdularis on 20/07/17.
// */
//class GameRoundInfoAdapter(context: Context, @param:LayoutRes private val mResId: Int) :
//    ArrayAdapter<GameRound.Info?>(context, mResId) {
//    private var mDeleteItemClickListener: OnDeleteItemClickListener? = null
//    fun setOnDeleteItemClickListener(listener: OnDeleteItemClickListener?) {
//        mDeleteItemClickListener = listener
//    }
//
//    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
//        var view = convertView
//        val holder: Holder
//        if (view == null) {
//            view = LayoutInflater.from(context).inflate(mResId, parent, false)
//            holder = Holder(view)
//            view.tag = holder
//        } else {
//            holder = view.tag as Holder
//        }
//        val dt = getItem(position)
//        dt?.let { setHolderData(holder, it) }
//        return view!!
//    }
//
//    private fun setHolderData(holder: Holder, info: GameRound.Info) {
//        holder.textName.text = info.name
//        holder.textDuration.text = fromInteger(info.duration)
//        if (holder.deleteItemClick == null) {
//            holder.deleteItemClick = DeleteItemClick(info)
//            holder.viewDeleteItem.setOnClickListener(holder.deleteItemClick)
//        } else {
//            holder.deleteItemClick!!.info = info
//        }
//    }
//
//    internal inner class Holder(view: View) {
//        //        @BindView(R.id.text_name)
//        var textName: TextView
//
//        //        @BindView(R.id.text_duration)
//        var textDuration: TextView
//
//        //        @BindView(R.id.delete_list_item)
//        var viewDeleteItem: View
//        var deleteItemClick: DeleteItemClick? = null
//
//        init {
////            ButterKnife.bind(this, view);
//            textName = view.findViewById(R.id.text_name)
//            textDuration = view.findViewById(R.id.text_duration)
//            viewDeleteItem = view.findViewById(R.id.delete_list_item)
//        }
//    }
//
//    interface OnDeleteItemClickListener {
//        fun onDeleteItemClick(info: GameRound.Info?)
//    }
//
//    internal inner class DeleteItemClick internal constructor(var info: GameRound.Info) :
//        View.OnClickListener {
//        override fun onClick(v: View) {
//            if (mDeleteItemClickListener != null) {
//                mDeleteItemClickListener!!.onDeleteItemClick(info)
//            }
//        }
//    }
//}
